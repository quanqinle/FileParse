package com.github.quanqinle.entity;

import javax.xml.crypto.Data;

public class ExcelDemo {
    int id;
    String name;
    int age;
    Data birthday;
    double salary;
    String happy;
    Data creattime;

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
