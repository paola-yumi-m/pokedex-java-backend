package dev.paola.pokedex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "pokemons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {
    @Id
    private ObjectId id;
//    private List<PokemonAbility> abilities;
    private int height;
    private String name;
    private int weight;
//    private List<PokemonType> types;

//    private List<Sprite> sprites;
}
