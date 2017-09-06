package com.github.quanqinle.util;

import com.google.common.base.Strings;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

/**
 * 读取excel，并将行数据按照对象存储
 *
 * @author quanql
 */
public class ReadExcel {

    public static List<Object> excelRead(String filePath, Class obj, Map<String, String> headerRowMap) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("指定的文件不存在");
        }

        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(file));     //解析2003
        } catch (Exception e) {
            workbook = new HSSFWorkbook(new FileInputStream(file));      //解析2007
        }

        List<Object> resultList = new ArrayList<>();        //结果
        Map<String, Method> methodMap = getObjectSetterMethod(obj);
        Map<String, Field> fieldMap = getObjectField(obj);
        Map<Integer, String> titleMap = new HashMap<>();

        for (int sn = 0; sn < workbook.getNumberOfSheets(); sn++) {
            if (1 == sn) {
                break;
            }
            Sheet sheet = workbook.getSheetAt(sn);
            LogUtil.debug("Sheet #" + sn + " : " + sheet.getSheetName());

            for (Row row : sheet) {
                int rowNum = row.getRowNum(); // current row num, from 0
                LogUtil.debug( "Row " + rowNum );

                if (0 == rowNum) {
                    titleMap = mapCellNameWithIndex(row, headerRowMap);
                    LogUtil.info("Header Row : " + titleMap.toString());
                    continue;
                }

                Object object = obj.newInstance();
                for (Cell cell : row) {
//                    String cellValue = getValue(cell);
                    int cellIndex = cell.getColumnIndex();
                    String headerTitle = titleMap.get(cellIndex);

                    Field field = fieldMap.get(headerTitle);    //该property在object对象中对应的属性
                    Method method = methodMap.get(headerTitle);  //该property在object对象中对应的setter方法
                    setObjectPropertyValueByCell(object, field, method, cell);
                }

                resultList.add(object);
            }

        }

        return resultList;
    }

    /**
     * 根据 excel表头 和 {预期表头实际名称, entity class属性名称}，得到 映射{column id, entity class属性名}。<br>
     * 另，excel列的排序没有限制。
     *
     * @param header    excel表头（即，第1行）
     * @param headerRow {预期表头实际名称, entity class属性名称}，例如{姓名, name}
     * @return 映射{column id, entity class属性名}，例如{2, name}
     * @author quanql
     */
    private static Map<Integer, String> mapCellNameWithIndex(Row header, Map<String, String> headerRow) {
        Map<Integer, String> headerMap = new HashMap<>();
        if (header.getLastCellNum() != headerRow.size()) {
            LogUtil.error("header row not match!");
        }

        for (Cell cell : header) {
            int index = cell.getColumnIndex();
            String val = cell.getStringCellValue();

            if (Strings.isNullOrEmpty(val)) {
                LogUtil.error("bad style on header row, cell index:" + index);
            }
            val = val.trim().toLowerCase();

            if (!headerRow.containsKey(val)) {
                LogUtil.error("header row not match! result[" + val + "] not exist");
            }
            headerMap.put(index, headerRow.get(val).toLowerCase());
        }
        return headerMap;
    }

    /**
     * 获取object对象所有属性的Setter方法，并构建map对象，结构为Map<'field','method'>
     *
     * @param object object对象
     * @return object所有setter方法的Map(fieldName, method)
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, Method> getObjectSetterMethod(Class object) {
        Field[] fields = object.getDeclaredFields();       //获取object对象的所有属性
        Method[] methods = object.getDeclaredMethods();    //获取object对象的所有方法
        Map<String, Method> methodMap = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            for (Method method : methods) {
                String methodName = method.getName();
                //匹配set方法
                if (methodName != null && "set".equals(methodName.substring(0, 3)) &&
                        Modifier.isPublic(method.getModifiers()) &&
                        ("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1)).equals(methodName)) {
                    methodMap.put(fieldName.toLowerCase(), method);       //将匹配的setter方法加入map对象中
                    break;
                }
            }
        }

        return methodMap;
    }

    /**
     * 获取object对象的所有属性，并构建map对象，对象结果为Map<'field','field'>
     *
     * @param object object对象
     * @return object所有属性的Map(fieldName, field)
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, Field> getObjectField(Class object) {
        Field[] fields = object.getDeclaredFields();       //获取object对象的所有属性
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            fieldMap.put(fieldName.toLowerCase(), field);
        }
        return fieldMap;
    }

    /**
     * 读取cell，将值以string类型返回
     *
     * @param cell 单元格
     * @return 单元格里的值
     */
    private static String getValue(Cell cell) {
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            return NumberToTextConverter.toText(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 根据指定属性的的setter方法给object对象设置值
     * @param obj    object对象
     * @param field  object对象的属性
     * @param method object对象属性的相对应的方法
     * @param cell  单元格
     * @throws Exception 异常
     */
    private static void setObjectPropertyValueByCell(Object obj, Field field, Method method, Cell cell) throws Exception {
        Object[] oo = new Object[1];
        String type = field.getType().getName();
        if (("java.util.Date".equals(type) || "Date".equals(type)) && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            oo[0] = date;
        } else {
            oo[0] = transformValueToField(type, getValue(cell));
        }

        try {
            method.invoke(obj, oo);
        } catch (Exception e) {
            System.out.println("Wrong value");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 单元格value转为obj field值
     * @param type object的变量类型
     * @param value 单元格值
     * @return object的变量值
     */
    private static Object transformValueToField(String type, String value) {
        Object oo;
        if (value.length() <= 0) {
            oo = value;
        } else if ("java.lang.String".equals(type) || "String".equals(type)) {
            oo = value;
        } else if ("java.lang.Integer".equals(type) || "java.lang.int".equals(type) || "Integer".equals(type) || "int".equals(type)) {
            oo = Integer.valueOf(value);
        } else if ("java.lang.Float".equals(type) || "java.lang.float".equals(type) || "Float".equals(type) || "float".equals(type)) {
            oo = Float.valueOf(value);
        } else if ("java.lang.Double".equals(type) || "java.lang.double".equals(type) || "Double".equals(type) || "double".equals(type)) {
            oo = Double.valueOf(value);
        } else if ("java.math.BigDecimal".equals(type) || "BigDecimal".equals(type)) {
            oo = new BigDecimal(value);
        } else if ("java.util.Date".equals(type) || "Date".equals(type)) {
            //当长度为19(yyyy-MM-dd HH24:mm:ss)或者为14(yyyyMMddHH24mmss)时Date格式转换为yyyyMMddHH24mmss
            if (value.length() == 19 || value.length() == 14) {
                oo = DateUtils.string2Date(value, "yyyyMMddHH24mmss");
            } else {     //其余全部转换为yyyyMMdd格式
                oo = DateUtils.string2Date(value, "yyyyMMdd");
            }
        } else if ("java.sql.Timestamp".equals(type)) {
            oo = DateFormatUtils.formatDate(value, "yyyyMMddHH24mmss");
        } else if ("java.lang.Boolean".equals(type) || "Boolean".equals(type)) {
            oo = Boolean.valueOf(value);
        } else if ("java.lang.Long".equals(type) || "java.lang.long".equals(type) || "Long".equals(type) || "long".equals(type)) {
            oo = Long.valueOf(value);
        } else {
            oo = value;
        }
        return oo;
    }

}
