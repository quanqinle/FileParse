package com.github.quanqinle.parse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.github.quanqinle.util.Constant;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.alibaba.fastjson.JSON;
import com.github.quanqinle.entity.AndroidLaunch.LaunchTime;
import com.github.quanqinle.util.DoubleUtil;
import com.github.quanqinle.util.HueUtil;
import com.github.quanqinle.util.LogUtil;

/**
 * 解析csv的demo
 *
 * @author qinle.quan
 *
 */
public class ParseCsv {

	public static void main(String[] args) {
		appLaunch("launch_4.8.5.csv");
	}

	/**
	 * android app 启动耗时
	 *
	 * @param filename
	 */
	public static void appLaunch(String filename) {

		String file = String.join(File.separator, Constant.RAW_FILE_DIR, filename);
		String newFileName = HueUtil.modifyFirstLine(file);
		LogUtil.info("app_create,home_create,home_show,total");
		
		long app_create_sum = 0l;
		long home_create_sum = 0l;
		long home_show_sum = 0l;
		long total_sum = 0l;
		long cnt = 0l;
		long invalidCnt = 0l;

		Iterable<CSVRecord> records;
		try {
			Reader in = new FileReader(newFileName);
			records = CSVFormat.RFC4180.withHeader(HueUtil.Headers.class).parse(in);
			boolean isFirstLine = true;
			for (CSVRecord record : records) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}
//				String id = record.get(Headers.ID);
				String kv = record.get(HueUtil.Headers.kv);
//				LogUtil.info(kv);
				LaunchTime launch = JSON.parseObject(kv, LaunchTime.class);
				if (launch.getTotal() > 10000) { // 大于10s，嘈点
					invalidCnt ++;
					continue;
				}
				LogUtil.info(launch.getApp_create() + ","+ launch.getHome_create() +","+ launch.getHome_show() +","+ launch.getTotal());
				app_create_sum += launch.getApp_create();
				home_create_sum += launch.getHome_create();
				home_show_sum += launch.getHome_show();
				total_sum += launch.getTotal();
				cnt++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtil.info("app_create,home_create,home_show,total,count");
		LogUtil.info("总值");
		LogUtil.info(app_create_sum + ","+ home_create_sum +","+ home_show_sum +","+ total_sum + "," + cnt);
		LogUtil.info("均值");
		LogUtil.info(DoubleUtil.div(app_create_sum, cnt, 4) + "," +
				DoubleUtil.div(home_create_sum, cnt, 4) + "," +
				DoubleUtil.div(home_show_sum, cnt, 4) + "," +
				DoubleUtil.div(total_sum, cnt, 4));
		LogUtil.info("过滤嘈点(条):" + invalidCnt);
	}

}
