package dev.paola.pokedex.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Setter
@Getter
@EqualsAndHashCode
public class PokemonPayload {
    private Integer pokemonId;
}
