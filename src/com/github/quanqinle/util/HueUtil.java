package com.github.quanqinle.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * HUE大数据平台工具
 * @author qinle.quan
 *
 */
public class HueUtil {
	/**
	 * rods_bb01_appv2_log_decoded_i_hr表字段名
	 * 
	 * @author qinle.quan
	 *
	 */
	public enum Headers {
		version, imei, imsi, brand, cpu, device_id, device_model, resolution, carrier, access, access_subtype, channel, source, source_detail, userid, phone_number, country, language, os, os_version, sdk_version, event_type, seq, ts, kv, appnm, ip, h5_kv, source_kv, md5, pt, hr
	}

	/**
	 * 修改HUE导出的csv文件，去掉第一行table header中的表名，以便于csv类库函数使用
	 * @author qinle.quan
	 * @param filename 原始csv文件名，全路径
	 * @return 修改后的csv文件名
	 */
	public static String modifyFirstLine(String filename) {
		String newfilename = filename.replace(".csv", "-new.csv");
		int modifyLine = 1;// 要修改的行
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new FileReader(filename));
			out = new PrintWriter(new BufferedWriter(new FileWriter(newfilename)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String line;
		int count = 1;
		try {
			while ((line = in.readLine()) != null) {
				if (count == modifyLine) {
					out.println(line.replace("rods_bb01_appv2_log_decoded_i_hr.", ""));
				} else {
					out.println(line);
				}
				count++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newfilename;
	}
	
}
