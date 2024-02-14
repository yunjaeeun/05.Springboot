package com.ohgiraffers.section02.annotation.common;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class Squirtle implements Pokemon{
    @Override
    public void attack() {
        System.out.println("꼬부기 물대포 발사");
    }
}
