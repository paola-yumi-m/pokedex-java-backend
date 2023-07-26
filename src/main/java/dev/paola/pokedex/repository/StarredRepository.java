package dev.paola.pokedex.repository;

import dev.paola.pokedex.dto.StarredPokemon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StarredRepository extends MongoRepository<StarredPokemon, ObjectId> {
    Optional<StarredPokemon> findByPokemonId(int pokemonId);
}
