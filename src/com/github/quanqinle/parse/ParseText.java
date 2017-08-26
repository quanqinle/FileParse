package com.github.quanqinle.parse;

import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.FileIOUtils;
import com.google.common.base.Strings;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 处理纯文本
 *
 * @author quanql
 */
public class ParseText {

    public static void main(String[] args) {
        String newFilePath = String.join(File.separator, Constant.RAW_FILE_DIR, "AnimalFarm-4.txt");
        FileIOUtils.writeFileFromString(newFilePath, trimTrailings("AnimalFarm.txt"));
    }

    /**
     * 处理文件内容，删除不恰当的换行
     *
     * @param filename
     */
    public static String trimTrailings(String filename) {
        String file = String.join(File.separator, Constant.RAW_FILE_DIR, filename);
        List<String> strs = FileIOUtils.readFile2List(file);
        String strResult = "";
        for (String line : strs) {
            if (!Strings.isNullOrEmpty(line)) {
                if (NumberUtils.isDigits(line)) {
                    continue;
                }

                strResult = new String(strResult).concat(handleEndChar(line));
//                strResult += handleEndChar(line); // low efficient
            }
        }
        return strResult;
    }

    /**
     * 处理该行是否需要换行<br>
     * 当前仅适用于英文
     *
     * @param line
     * @return
     */
    public static String handleEndChar(String line) {

        String[] romanNums = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL", "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVII", "XLIX", "L"};
        String[] punctuations = {".", "'", ":", "’"};

        if (Arrays.asList(romanNums).contains(line)) {
            return '\n' + line + '\n';
        }

        String endChar = line.substring(line.length() - 1);
        if (line.endsWith("-")) {
            return line.substring(0, line.length() - 1);
        } else if (Arrays.asList(punctuations).contains(endChar)) {
            return line + '\n';
        }

        return line + " ";
    }
}
