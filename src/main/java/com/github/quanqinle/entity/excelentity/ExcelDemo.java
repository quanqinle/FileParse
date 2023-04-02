package com.github.quanqinle.entity.excelentity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * excel每行就是一个对象
 */
public class ExcelDemo {
    private int id;
    private String name;
    private int age;
    private Date birthday;
    private double salary;
    private String happy;
    private Date createTime;
    private Date bedTime;

    private static final Map<String, String> headerRow;

    static { // 不限大小写
        headerRow = new HashMap<>();
        headerRow.put("id", "id");
        headerRow.put("姓名", "name");
        headerRow.put("年龄", "age");
        headerRow.put("生日", "birthday");
        headerRow.put("收入", "salary");
        headerRow.put("是否幸福", "happy");
        headerRow.put("创建日期", "createTime");
        headerRow.put("睡觉时间", "bedTime");
    }

    public static Map<String, String> getHeaderRow() {
        return headerRow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getHappy() {
        return happy;
    }

    public void setHappy(String happy) {
        this.happy = happy;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBedTime() {
        return bedTime;
    }

    public void setBedTime(Date bedTime) {
        this.bedTime = bedTime;
    }

    @Override
    public String toString() {
        return String.join(", ", String.valueOf(this.getId()), this.getName(), String.valueOf(this.getAge()), String.valueOf(this.getBirthday()), String.valueOf(this.getSalary()), this.getHappy(), String.valueOf(this.getCreateTime()), String.valueOf(this.getBedTime()));
    }

}
