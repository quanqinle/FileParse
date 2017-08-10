package com.github.quanqinle.parse;

import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.FileIOUtils;
import com.google.common.base.Strings;

import java.io.File;
import java.util.List;

/**
 * 处理纯文本
 *
 * @author quanql
 */
public class ParseText {

    public static void main(String[] args) {
        trimTrailings("animalfarm.txt");
    }

    /**
     * @param filename
     */
    public static String trimTrailings(String filename) {
        String file = String.join(File.separator, Constant.RAW_FILE_DIR, filename);
        List<String> strs = FileIOUtils.readFile2List(file);
        String strResult = "";
        for (String line : strs) {
            if (!Strings.isNullOrEmpty(line)) {
                //TODO

            } else if (line.isEmpty()) {
                strResult += line;
            }
        }
        return strResult;
    }
}
