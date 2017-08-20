package com.github.quanqinle.parse;

import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.FileIOUtils;
import com.github.quanqinle.util.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * 解析html的demo
 *
 * @author qinle.quan
 */
public class ParseHtml {

    public static void main(String[] args) {
        String file = String.join(File.separator, Constant.RAW_FILE_DIR, "AGOT.txt");
        FileIOUtils.writeFileFromString(file, genAGOT());
    }

    private static String genAGOT() {
        String strResult = "";
        String url = "http://readclassical.github.io/novel/A-Game-of-Thrones-{index}.html";
        Document doc;
        Element body;
        String line;
        for (int i = 1; i <= 73; i++) {
            try {
                doc = Jsoup.connect(url.replace("{index}", String.valueOf(i))).get();
                body = doc.body();
                Elements origins = body.getElementsByTag("origin");
                Elements translates = body.getElementsByTag("translate");

                int originSize = origins.size();
                int translateSize = translates.size();
                int maxLineNum = originSize > translateSize ? originSize : translateSize;

                for (int j = 0; j < maxLineNum; j++) {
                    if (originSize >= j) {
                        line = origins.get(j).text();
                         if (0 == j) { // new chapter add one more null line
                            line = '\n' + line;
                        }
                        strResult = new StringBuffer(strResult).append(line).append('\n').toString();
                    }
                    if (translateSize >= j) {
                        line = translates.get(j).text();
                        strResult = new StringBuffer(strResult).append(line).append('\n').toString();
                    }

                }

            } catch (IOException e) {
                LogUtil.info(String.format("error on cha[%s]", i));
                e.printStackTrace();
            }
        }
        return strResult;
    }

}
