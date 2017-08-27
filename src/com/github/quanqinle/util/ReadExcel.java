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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<Object> resultList = new ArrayList<>();        //初始化结果解
        Map<String, Method> methodMap = getObjectSetterMethod(obj);
        Map<String, Field> fieldMap = getObjectField(obj);
        Map<Integer, String> titleMap = new HashMap<>();

        for (int sn = 0; sn < workbook.getNumberOfSheets(); sn++) {
            if (1 == sn) {
                break;
            }
            Sheet sheet = workbook.getSheetAt(sn);
            System.out.println("Sheet #" + sn + " : " + sheet.getSheetName());
            if (sheet == null) { // skip empty sheet
                System.out.println("This is a empty Sheet");
                continue;
            }

            for (Row row : sheet) {
                int rowNum = row.getRowNum(); // current row num, from 0
//                LogUtil.info( "Row " + row.getRowNum() );
                System.out.println("Row " + rowNum);

                if (row == null) { // skip empty row
                    System.out.println("This is a empty row");
                    continue;
                }

                if (0 == rowNum) {
                    titleMap = mapCellNameWithIndex(row, headerRowMap);
                    System.out.println("Header Row : " + titleMap.toString());

                    continue;
                }

                Object object = obj.newInstance();
                for (Cell cell : row) {
                    String cellValue = getValue(cell);
                    int cellIndex = cell.getColumnIndex();
                    String headerTitle = titleMap.get(cellIndex);

                    Field field = fieldMap.get(headerTitle);    //该property在object对象中对应的属性
                    Method method = methodMap.get(headerTitle);  //该property在object对象中对应的setter方法
                    setObjectPropertyValue(object, field, method, cellValue);

                    System.out.println("cell[" + cellIndex + "] = " + cellValue);
                }

                resultList.add(object);
                System.out.println("row end");
            }

        }

        return resultList;
    }

    /**
     * 根据 excel表头 和 {预期表头实际名称, entity class属性名称}，得到 映射{entity class属性名, column id}。<br>
     * 另，excel列的排序没有限制。
     *
     * @param header    excel表头（即，第1行）
     * @param headerRow {预期表头实际名称, entity class属性名称}，例如{姓名, name}
     * @return 映射{entity class属性名, column id}，例如{name, 2}
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
            headerMap.put(index, headerRow.get(val));
        }
        return headerMap;
    }

    /**
     * 获取object对象所有属性的Setter方法，并构建map对象，结构为Map<'field','method'>
     *
     * @param object object对象
     * @return
     * @autor:chenssy
     * @date:2014年8月9日
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Method> getObjectSetterMethod(Class object) {
        Field[] fields = object.getDeclaredFields();       //获取object对象的所有属性
        Method[] methods = object.getDeclaredMethods();    //获取object对象的所有方法
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for (Field field : fields) {
            String attri = field.getName();
            for (Method method : methods) {
                String meth = method.getName();
                //匹配set方法
                if (meth != null && "set".equals(meth.substring(0, 3)) &&
                        Modifier.isPublic(method.getModifiers()) &&
                        ("set" + Character.toUpperCase(attri.charAt(0)) + attri.substring(1)).equals(meth)) {
                    methodMap.put(attri.toLowerCase(), method);       //将匹配的setter方法加入map对象中
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
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Field> getObjectField(Class object) {
        Field[] fields = object.getDeclaredFields();       //获取object对象的所有属性
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        for (Field field : fields) {
            String attri = field.getName();
            fieldMap.put(attri.toLowerCase(), field);
        }
        return fieldMap;
    }

    public static String getValue(Cell cell) {
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
     *
     * @param obj    object对象
     * @param field  object对象的属性
     * @param method object对象属性的相对应的方法
     * @param value  需要设置的值
     * @throws Exception
     * @autor:chenssy
     * @date:2014年8月10日
     */
    public static void setObjectPropertyValue(Object obj, Field field, Method method, String value) throws Exception {
        Object[] oo = new Object[1];

        String type = field.getType().getName();
        if ("java.lang.String".equals(type) || "String".equals(type)) {
            oo[0] = value;
        } else if ("java.lang.Integer".equals(type) || "java.lang.int".equals(type) || "Integer".equals(type) || "int".equals(type)) {
            if (value.length() > 0)
                oo[0] = Integer.valueOf(value);
        } else if ("java.lang.Float".equals(type) || "java.lang.float".equals(type) || "Float".equals(type) || "float".equals(type)) {
            if (value.length() > 0)
                oo[0] = Float.valueOf(value);
        } else if ("java.lang.Double".equals(type) || "java.lang.double".equals(type) || "Double".equals(type) || "double".equals(type)) {
            if (value.length() > 0)
                oo[0] = Double.valueOf(value);
        } else if ("java.math.BigDecimal".equals(type) || "BigDecimal".equals(type)) {
            if (value.length() > 0)
                oo[0] = new BigDecimal(value);
        } else if ("java.util.Date".equals(type) || "Date".equals(type)) {
            if (value.length() > 0) {//当长度为19(yyyy-MM-dd HH24:mm:ss)或者为14(yyyyMMddHH24mmss)时Date格式转换为yyyyMMddHH24mmss
                if (value.length() == 19 || value.length() == 14) {
                    oo[0] = DateUtils.string2Date(value, "yyyyMMddHH24mmss");
                } else {     //其余全部转换为yyyyMMdd格式
                    oo[0] = DateUtils.string2Date(value, "yyyyMMdd");
                }
            }
        } else if ("java.sql.Timestamp".equals(type)) {
            if (value.length() > 0)
                oo[0] = DateFormatUtils.formatDate(value, "yyyyMMddHH24mmss");
        } else if ("java.lang.Boolean".equals(type) || "Boolean".equals(type)) {
            if (value.length() > 0)
                oo[0] = Boolean.valueOf(value);
        } else if ("java.lang.Long".equals(type) || "java.lang.long".equals(type) || "Long".equals(type) || "long".equals(type)) {
            if (value.length() > 0)
                oo[0] = Long.valueOf(value);
        }
        try {
            method.invoke(obj, oo);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}