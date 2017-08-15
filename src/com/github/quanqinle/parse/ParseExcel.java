package com.github.quanqinle.parse;

import java.io.File;

import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.ExcelReadHelper;

public class ParseExcel {
	
	public static void main(String[] args) {
		tryExcel("20161023.json");
	}
	
	public static void tryExcel(String filename) {
		String file = String.join(File.separator, Constant.RAW_FILE_DIR, filename);
		ExcelReadHelper.excelRead(file, properties, obj);
	}
}
