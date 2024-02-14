package com.ohgiraffers.section02.annotation.subsection03.collection;

import com.ohgiraffers.section02.annotation.common.Pokemon;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "pokemonServiceCollection")
public class PokemonService {

    /* 필기. 같은 타입의 빈이 2개 이상일 경우 List 또는 Map 형태의 컬렉션 형태로 주입 받을 수 있다. */

    /* 설명. 1. 같은 타입의 빈들을 List 형태로 주입 받을 수 있다. */
//    private List<Pokemon> pokemonList;
//
//    @Autowired
//    public PokemonService(List<Pokemon> pokemonList) {          // Pokemon 타입이 여러개면 List로 받아 관리
//        this.pokemonList = pokemonList;
//    }
//
//    /* 설명. bean이름의 사전순으로 불러옴. */
//    public void pokemonAttack() {
//        pokemonList.forEach(Pokemon::attack);                   // Pokemon 타입에 정의되어 있는 attack
//    }

    /* 설명. 2. 같은 타입의 빈들을 map 형태로도 주입 받을 수 있다. */
    private Map<String, Pokemon> pokemonMap;

    @Autowired
    public PokemonService(Map<String, Pokemon> pokemonMap) {
        this.pokemonMap = pokemonMap;
    }

    public void pokemonAttack() {
        pokemonMap.forEach((k, v) -> {
            System.out.println("key: " + k);            // key = bean 객체의 id
            System.out.println("value: " + v);          // value = bean 객체의 주소값
            v.attack();
        });
    }
}
