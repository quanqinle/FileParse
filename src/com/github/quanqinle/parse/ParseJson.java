package com.github.quanqinle.parse;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.quanqinle.entity.AndroidSkippedFrame.SkippedFrame;
import com.github.quanqinle.entity.AndroidSkippedFrame.TimeResults;
import com.github.quanqinle.util.Constant;
import com.github.quanqinle.util.FileUtil;
import com.github.quanqinle.util.LogUtil;

/**
 * 解析json的demo
 *
 * @author qinle.quan
 *
 */
public class ParseJson {

	public static void main(String[] args) {
		skippedFrame("20161023.json");
	}

	/**
	 * 解析每日掉帧数json。<BR>
	 * @param filename
	 */
	public static void skippedFrame(String filename) {

		String file = String.join(File.separator, Constant.RAW_FILE_DIR, filename);
		List<String> strJson = FileUtil.readFileByLines(file);

		LogUtil.info("version,page,model,avg,count,invalidCount");

		for (String str : strJson) {
			SkippedFrame skippedFrame = JSON.parseObject(str, SkippedFrame.class);
			List<TimeResults> timeResults = skippedFrame.getTime_results();
			for (TimeResults result : timeResults) {
				String version = result.getRconfig().getVersion();
				String page = result.getRconfig().getPage();
				String model = result.getRconfig().getDevice_model();

				String avg = result.getAvg() + "";
				String count = result.getCount() + "";
				String invalidCount = result.getInvalideCount() + "";
				String log = String.join(",", version,page,model,avg,count,invalidCount);
				LogUtil.info(log);
			}
		}
	}

}
