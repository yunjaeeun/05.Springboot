package com.ohgiraffers.section02.annotation.common;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary            // 같은 타입의 불러올 bean이 여러개가 있을 때 이 bean만 불러옴
public class Charmandar implements Pokemon{
    @Override
    public void attack() {
        System.out.println("파이리 불꽃 공격");
    }
}
