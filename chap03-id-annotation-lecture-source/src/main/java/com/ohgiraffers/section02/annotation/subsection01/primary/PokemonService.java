package com.ohgiraffers.section02.annotation.subsection01.primary;

import com.ohgiraffers.section02.annotation.common.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "pokemonServicePrimary")
public class PokemonService {

    private Pokemon pokemon;

    /* 설명.
     *  @Autowired는 생략하면 매개변수 있는 생성자에 자동으로 작성된다.(자동으로 생성자 주입)
     *  현재와 같이 Pokemon 타입의 빈이 2개 이상일 경우 @Primary 어노테이션을 통해 하나의 빈만 주입되게 할 수 있다.
    * */
    @Autowired                                      // 생성자를 통한 컨테이너
    public PokemonService(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public void pokemonAttack() {                   // 불러올 bean 중에 @Primary로 우선순위를 정해주지 않으면 오류 발생
        pokemon.attack();
    }
}
