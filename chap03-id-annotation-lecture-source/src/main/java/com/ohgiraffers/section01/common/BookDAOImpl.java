package com.ohgiraffers.section01.common;


import org.springframework.stereotype.Repository;

import java.util.*;
/* 필기. BookDAO라는 Interface를 만든 후 BookDAOImpl에게 상속시켜 타입을 숨긴다(유지보수, 확장성) */

@Repository
public class BookDAOImpl implements BookDAO{
    private Map<Integer, BookDTO> bookList;


    public BookDAOImpl() {
        bookList = new HashMap<>();
        bookList.put(1, new BookDTO(1, 123456, "자바의 정석", "김자바", "커피출판", new Date()));
        bookList.put(2, new BookDTO(2, 789012, "칭찬은 고래도 춤춘다", "우영우", "변호출판", new Date()));
    }

    @Override
    public List<BookDTO> findAllBook() {

        /* 설명. HashMap의 value들만 봅아 ArrayList 형태로 변환. */
        return new ArrayList<>(bookList.values());
    }

    @Override
    public BookDTO searchBookBySequence(int sequence) {
        return bookList.get(sequence);
    }
}
