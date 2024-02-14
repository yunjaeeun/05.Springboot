package com.ohgiraffers.section02.annotation.subsection02.qualifier;

import com.ohgiraffers.section02.annotation.common.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "pokemonServiceQualifier")
public class PokemonService {

    /* 설명.
     *  @Qualifier를 통해 원하는 bean 이름(id)으로 하나의 빈을 주입 받을 수 있다.
     *  (@Primary보다 우선순위가 높다)
    * */
//    @Autowired
//    @Qualifier("squirtle")                      // @Primary보다 우선순위가 높음
    private Pokemon pokemon;

    @Autowired
    public PokemonService(@Qualifier("squirtle") Pokemon pokemon) {     // 생성자를 통한 방법
        this.pokemon = pokemon;
    }

    public void pokemonAttack() {                   // 불러올 bean 중에 @Primary로 우선순위를 정해주지 않으면 오류 발생
        pokemon.attack();
    }
}
