package dev.paola.pokedex.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "my-pokedex")
@Data
@NoArgsConstructor
public class Pokedex extends Pokemon {
    public Pokedex(Pokemon pokemon) {
        setPokemonId(pokemon.getPokemonId());
        setAbilities(pokemon.getAbilities());
        setSprites(pokemon.getSprites());
        setName(pokemon.getName());
        setHeight(pokemon.getHeight());
        setTypes(pokemon.getTypes());
        setWeight(pokemon.getWeight());
    }
}
