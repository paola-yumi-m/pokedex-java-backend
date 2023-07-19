package dev.paola.pokedex.web.api.payload;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@EqualsAndHashCode
public class StarredPokemonPayload {
    private Integer pokemonId;
    private String nickname;
}
