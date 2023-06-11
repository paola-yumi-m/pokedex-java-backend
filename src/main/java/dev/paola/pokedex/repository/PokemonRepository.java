package dev.paola.pokedex.repository;

import dev.paola.pokedex.dto.Pokemon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokemonRepository extends MongoRepository<Pokemon, ObjectId> {
    Optional<Pokemon> findByPokemonId(int pokemonId);
}
