package com.ohgiraffers.common;

import java.util.Date;

public class Bread extends Product{
    private java.util.Date bakeDate;        // 생산시간
    public Bread() {
    }
    public Bread(String name, int price, Date bakeDate) {
        super(name, price);
        this.bakeDate = bakeDate;
    }

    @Override
    public String toString() {
        return "Bread{" +
                "bakeDate=" + bakeDate +
                "} " + super.toString();
    }
}
