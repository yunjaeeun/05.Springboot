package com.ohgiraffers.section02.annotation.common;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

// @Component(value = "pikachu")
@Component      // value를 작성하지 않으면 클래스명이 bean의 이름이 됨
public class Pikachu implements Pokemon {

    @Override
    public void attack() {
        System.out.println("피카츄 백만볼트");
    }
}
