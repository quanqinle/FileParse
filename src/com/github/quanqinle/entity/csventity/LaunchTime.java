package com.github.quanqinle.entity.csventity;

public class LaunchTime {

    private long app_create;
    private long home_create;
    private long home_show;
    private long total;

/*	"eventValue":{"kRecordAppColdStartTime":"1.755859375"}}*/
//	private double kRecordAppColdStartTime; //s
//	private String router;

    public long getApp_create() {
        return app_create;
    }

    public void setApp_create(long app_create) {
        this.app_create = app_create;
    }

    public long getHome_create() {
        return home_create;
    }

    public void setHome_create(long home_create) {
        this.home_create = home_create;
    }

    public long getHome_show() {
        return home_show;
    }

    public void setHome_show(long home_show) {
        this.home_show = home_show;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
