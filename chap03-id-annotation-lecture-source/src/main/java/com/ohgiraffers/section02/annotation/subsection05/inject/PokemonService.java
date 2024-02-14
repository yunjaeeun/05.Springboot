package com.ohgiraffers.section02.annotation.subsection05.inject;

import com.ohgiraffers.section02.annotation.common.Pokemon;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.stereotype.Service;

@Service(value = "pokemonServiceInject")
public class PokemonService {

    /* 설명. Inject 라이브러리를 활용한 필드 주입 */
//    @Inject
//    @Named("pikachu")
    private Pokemon pokemon;

    /* 설명 2. 생성자 주입 */
//    @Inject
//    public PokemonService(@Named("pikachu") Pokemon pokemon) {
//        this.pokemon = pokemon;
//    }

    /* 설명. 3. setter 주입 */
    @Inject
    public void setPokemon(@Named("pikachu") Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public void pokemonAttack() {
        pokemon.attack();
    }
}
