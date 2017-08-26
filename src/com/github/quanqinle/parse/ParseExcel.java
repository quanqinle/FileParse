package com.github.quanqinle.parse;

import java.io.File;

import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.ExcelReadHelper;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * a demo demonstrate how to parse a excel file.<br>
 *
 * @author quanql
 */
public class ParseExcel {

    public static void main(String[] args) {
        tryExcel("demo.xlsx");
        officialExample("demo.xlsx");
    }

    public static void tryExcel(String filename) {
        String file = String.join(File.separator, Constant.RAW_FILE_DIR, filename);
//		ExcelReadHelper.excelRead(file, properties, obj);
    }


    /**
     * 官方示例
     * Demonstrates how to read excel styles for cells
     * <br>
     * https://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/ss/examples/CellStyleDetails.java
     */
    private static void officialExample(String filename) {

        String file = String.join(File.separator, Constant.RAW_FILE_DIR, filename);
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(new File(file));
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
            System.out.print(color);
        }
        if (font instanceof XSSFFont) {
            color = renderColor(((XSSFFont) font).getXSSFColor());
            System.out.print(color);
        }
        return color;
    }
}
