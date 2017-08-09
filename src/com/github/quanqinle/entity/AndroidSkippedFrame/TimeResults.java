package com.github.quanqinle.entity.AndroidSkippedFrame;

/**
 * 
 * @author qinle.quan
 *
 */
public class TimeResults {

	private Long avg;
	private Long count;
	private Long invalideCount;
	private TimeResultsDetail rconfig = new TimeResultsDetail();
	
	public Long getAvg() {
		return avg;
	}
	public void setAvg(Long avg) {
		this.avg = avg;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getInvalideCount() {
		return invalideCount;
	}
	public void setInvalideCount(Long invalideCount) {
		this.invalideCount = invalideCount;
	}
	public TimeResultsDetail getRconfig() {
		return rconfig;
	}
	public void setRconfig(TimeResultsDetail rconfig) {
		this.rconfig = rconfig;
	}

}
