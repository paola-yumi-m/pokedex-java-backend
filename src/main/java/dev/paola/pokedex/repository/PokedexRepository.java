package dev.paola.pokedex.repository;

import dev.paola.pokedex.dto.Pokedex;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokedexRepository extends MongoRepository<Pokedex, ObjectId> {
    Pokedex findByPokemonId(int pokemonId);
}
