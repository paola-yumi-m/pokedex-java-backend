package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.repository.StarredRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StarredService {
    @Autowired
    private final StarredRepository starredRepository;

    public StarredService(StarredRepository starredRepository) {
        this.starredRepository = starredRepository;
    }

    public List<StarredPokemon> getAllStarredPokemons() {
        return starredRepository.findAll();
    }

    public StarredPokemon getStarredPokemonById(Integer pokemonId) {
        return starredRepository.findByPokemonId(pokemonId);
    }

    public StarredPokemon addPokemonBy(Integer pokemonId) {
        return null;
    }

    public void deletePokemonBy(Integer pokemonId) {

    }

    public StarredPokemon editNicknameOf(Integer pokemonId, String nickname) {
        return null;
    }
}
