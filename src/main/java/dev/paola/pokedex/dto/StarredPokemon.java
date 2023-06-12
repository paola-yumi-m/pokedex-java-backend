package dev.paola.pokedex.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "starred")
public class StarredPokemon extends Pokemon {
}
