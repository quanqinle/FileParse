package com.github.quanqinle.entity.excelentity;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ExcelDemo {
    private int id;
    private String name;
    private int age;
    private Data birthday;
    private double salary;
    private String happy;
    private Data creattime;

    private static final Map<String, String> headerRow;

    static {
        headerRow = new HashMap<String, String>();
        headerRow.put("id", "id");
        headerRow.put("姓名", "name");
        headerRow.put("年龄", "age");
        headerRow.put("生日", "birthday");
        headerRow.put("收入", "salary");
        headerRow.put("是否幸福", "happy");
        headerRow.put("创建日期", "creattime");
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

    public Data getBirthday() {
        return birthday;
    }

    public void setBirthday(Data birthday) {
        this.birthday = birthday;
    }

    public Data getCreattime() {
        return creattime;
    }

    public void setCreattime(Data creattime) {
        this.creattime = creattime;
    }

    @Override
    public String toString() {
        return String.join(", ", String.valueOf(this.getId()), this.getName(), String.valueOf(this.getAge()), this.getBirthday().toString(), String.valueOf(this.getSalary()), this.getHappy(), this.getCreattime().toString());
    }

}
