package com.github.quanqinle.parse;

import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.FileIOUtils;
import com.github.quanqinle.util.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

/**
 * 解析html的demo<br>
 * 依赖库 https://github.com/jhy/jsoup
 *
 * @author qinle.quan
 */
public class ParseHtml {

    public static void main(String[] args) {
        String file = String.join(File.separator, Constant.RAW_FILE_DIR, "AGOT.txt");
        FileIOUtils.writeFileFromString(file, genAGOT());
    }

    /**
     * 从网站解析并保存一本书
     *
     * @return
     */
    private static String genAGOT() {
        String strResult = "";
        String url = "http://readclassical.github.io/novel/A-Game-of-Thrones-{index}.html";
        int maxIndex = 73; //max index in url
        Document doc;
        Element body;

        for (int i = 1; i <= maxIndex; i++) {
            try {
                doc = Jsoup.connect(url.replace("{index}", String.valueOf(i))).get();
                LogUtil.info(String.format("connected index[%d]", i));

                body = doc.body();
                Elements origins = body.getElementsByTag("origin");
                Elements translates = body.getElementsByTag("translate");

                int originSize = origins.size();
                int translateSize = translates.size();
                int maxLineNum = originSize > translateSize ? originSize : translateSize;

                for (int j = 0; j < maxLineNum; j++) {
                    String line;
                    if (originSize >= j) {
                        line = origins.get(j).text();
                        if (0 == j) { // add one more null line in new chapter
                            line = '\n' + line;
                        }
                        strResult = new StringBuffer(strResult).append(line).append('\n').toString();
                    }
                    if (translateSize >= j) {
                        line = translates.get(j).text();
                        strResult = new StringBuffer(strResult).append(line).append('\n').toString();
                    }
                }
            } catch (Exception e) {
                strResult = new StringBuffer(strResult).append(String.format("error at index[%s]", i)).append('\n').toString();
                LogUtil.error(String.format("error at index[%s]", i));
                e.printStackTrace();
            }
        }
        return strResult;
    }

}
