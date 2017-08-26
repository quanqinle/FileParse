package com.github.quanqinle.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import com.github.quanqinle.entity.ExcelDemo;
import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.ExcelReadHelper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * a demo demonstrate how to parse a excel file.<br>
 *
 * @author quanql
 */
public class ParseExcel {

    public static void main(String[] args) {
        String filePath = String.join(File.separator, Constant.RAW_FILE_DIR, "demo.xlsx");

        System.out.println("---- method readExcel2Obj() ----");
        readExcel2Obj(filePath);

        System.out.println("---- method officialExample() ----");
        officialExample(filePath);

        try {
            System.out.println("---- method readExcelSheet1ThenPrint() ----");
            List<List<Object>> lists = readExcelSheet1ThenPrint(filePath);
            for (List<Object> list : lists) {
                System.out.println(list);
            }
        } catch (Exception e) {

        }

    }

    /**
     * 读取excel内容存入object中
     *
     * @param filePath
     */
    public static void readExcel2Obj(String filePath) {
        String[] properties = {"id", "name", "age", "birthday", "salary", "happy", "creattime"};
        try {
            List<Object> list = ExcelReadHelper.excelRead(filePath, properties, ExcelDemo.class);
            System.out.println("list size=" + list.size());
            for (int i = 0; i <= list.size(); i++) {
                ExcelDemo demo = (ExcelDemo) list.get(i);
                System.out.println("row:" + i + " conent:" + demo.toString());
                System.out.print(demo.getId() + " ");
                System.out.print(demo.getName() + " ");
                System.out.print(demo.getAge() + " ");
                System.out.print(demo.getBirthday() + " ");
                System.out.print(demo.getSalary() + " ");
                System.out.print(demo.getHappy() + " ");
                System.out.println(demo.getCreattime());
            }
        } catch (Exception e) {
        }
    }


    /**
     * 官方示例
     * Demonstrates how to read excel styles for cells
     * <br>
     * https://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/ss/examples/CellStyleDetails.java
     */
    private static void officialExample(String filePath) {

        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(new File(filePath));
        } catch (Exception e) {
            System.out.println("fail to open excel");
            return;
        }

        DataFormatter formatter = new DataFormatter();

        for (int sn = 0; sn < workbook.getNumberOfSheets(); sn++) {
            Sheet sheet = workbook.getSheetAt(sn);
            System.out.println("Sheet #" + sn + " : " + sheet.getSheetName());

            for (Row row : sheet) {
                System.out.println("  Row " + row.getRowNum());

                for (Cell cell : row) {
                    CellReference ref = new CellReference(cell);
                    System.out.print("    " + ref.formatAsString());
                    System.out.print(" (" + cell.getColumnIndex() + ") ");

                    CellStyle style = cell.getCellStyle();
                    System.out.print("Format=" + style.getDataFormatString() + " ");
                    System.out.print("FG=" + renderColor(style.getFillForegroundColorColor()) + " ");
                    System.out.print("BG=" + renderColor(style.getFillBackgroundColorColor()) + " ");

                    Font font = workbook.getFontAt(style.getFontIndex());
                    System.out.print("Font=" + font.getFontName() + " ");
                    System.out.print("FontColor=" + fontColor(font, workbook));

                    System.out.println();
                    System.out.println("        " + formatter.formatCellValue(cell));
                }
            }

            System.out.println();
        }

        try {
            workbook.close();
        } catch (Exception e) {

        }
    }

    private static String renderColor(Color color) {
        if (color instanceof HSSFColor) {
            return ((HSSFColor) color).getHexString();
        } else if (color instanceof XSSFColor) {
            return ((XSSFColor) color).getARGBHex();
        } else {
            return "(none)";
        }
    }

    private static String fontColor(Font font, Workbook wb) {
        String color = "";
        if (font instanceof HSSFFont) {
            color = renderColor(((HSSFFont) font).getHSSFColor((HSSFWorkbook) wb));
        }
        if (font instanceof XSSFFont) {
            color = renderColor(((XSSFFont) font).getXSSFColor());
        }
        return color;
    }

    /** 官方示例 end **/

    /**
     * 读取excel第一页并打印
     * <p>
     * https://github.com/T5750/poi
     * <p>
     * http://blog.csdn.net/evangel_z/article/details/7312050
     */
    public static List<List<Object>> readExcelSheet1ThenPrint(String filePath) throws IOException {
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(new File(filePath))); // xlsx
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(new FileInputStream(new File(filePath))); // xls
        }

        // 读取excel第一个Sheet页内容
        Sheet sheet = workbook.getSheetAt(0);
        Row row;
        Cell cell;

        List<List<Object>> list = new LinkedList<List<Object>>();
        Object value;
        int counter = 0;
        for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            } else {
                counter++;
            }
            List<Object> linked = new LinkedList<>();
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                DecimalFormat df = new DecimalFormat("0");// 格式化 number String
                DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串

                String cellType;
                String dataFormat = cell.getCellStyle().getDataFormatString();;
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        cellType = "String";
                        value = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        cellType = "Number";
                        dataFormat = cell.getCellStyle().getDataFormatString();

                        if ("@".equals(dataFormat)) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(dataFormat)) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        }

                        break;
                    case BOOLEAN:
                        cellType = "Boolean";
                        value = cell.getBooleanCellValue();
                        break;
                    case BLANK:
                        cellType = "Blank";
                        value = "";
                        break;
                    default:
                        cellType = "default";
                        value = cell.toString();
                }
                System.out.printf("%d行%d列, type[%7s], value[%s], DateFormat[%s]\n", i, j, cellType, value, dataFormat);

//                if (value == null || "".equals(value)) {
//                    continue; // 空值，跳过
//                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }
}
