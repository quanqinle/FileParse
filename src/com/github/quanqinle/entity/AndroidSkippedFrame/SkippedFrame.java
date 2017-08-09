package com.github.quanqinle.entity.AndroidSkippedFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * Android launch time main body
 * @author qinle.quan
 *
 */
public class SkippedFrame {
	private List<String> time_result_versions = new ArrayList<String>();
	private List<TimeResults> time_results = new ArrayList<TimeResults>();
	
	public List<String> getTime_result_versions() {
		return time_result_versions;
	}
	public void setTime_result_versions(List<String> time_result_versions) {
		this.time_result_versions = time_result_versions;
	}
	public List<TimeResults> getTime_results() {
		return time_results;
	}
	public void setTime_results(List<TimeResults> time_results) {
		this.time_results = time_results;
	}

}
