package dev.paola.pokedex.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "starred")
public class StarredPokemon extends Pokemon {
   private String nickname;

    public StarredPokemon(Pokemon pokemon) {
        setPokemonId(pokemon.getPokemonId());
        setAbilities(pokemon.getAbilities());
        setSprites(pokemon.getSprites());
        setName(pokemon.getName());
        setHeight(pokemon.getHeight());
        setTypes(pokemon.getTypes());
        setWeight(pokemon.getWeight());
    }
}
