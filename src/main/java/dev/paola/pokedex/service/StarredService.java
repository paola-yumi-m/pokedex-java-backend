package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import dev.paola.pokedex.repository.StarredRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class StarredService {
    @Autowired
    private final StarredRepository starredRepository;
    private final PokemonRepository pokemonRepository;

    public StarredService(StarredRepository starredRepository, PokemonRepository pokemonRepository) {
        this.starredRepository = starredRepository;
        this.pokemonRepository = pokemonRepository;
    }

    public List<StarredPokemon> getAllStarredPokemons() {
        return starredRepository.findAll();
    }

    public StarredPokemon getStarredPokemonById(Integer pokemonId) {
        StarredPokemon starredPokemon = starredRepository.findByPokemonId(pokemonId);

        if (isNull(starredPokemon)) throw new PokemonNotFoundException();

        return starredPokemon;
    }

    public StarredPokemon addPokemonBy(Integer pokemonId, String nickname) {
        Optional<Pokemon> pokemon = pokemonRepository.findByPokemonId(pokemonId);
        StarredPokemon starredPokemon = new StarredPokemon(pokemon.get());
        starredPokemon.setNickname(nickname);

        return starredRepository.insert(starredPokemon);
    }

    public void deletePokemonBy(Integer pokemonId) {

    }

    public StarredPokemon editNicknameOf(Integer pokemonId, String nickname) {
        return null;
    }
}
