package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import dev.paola.pokedex.repository.StarredRepository;
import dev.paola.pokedex.web.api.payload.StarredPokemonPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<StarredPokemon> starredPokemon = starredRepository.findByPokemonId(pokemonId);

        return starredPokemon.orElseThrow(PokemonNotFoundException::new);
    }

    public StarredPokemon addPokemonBy(StarredPokemonPayload payload) {
        validateIfStarredPokemonAlreadyRegistered(payload);

        Optional<Pokemon> pokemon = pokemonRepository.findByPokemonId(payload.getPokemonId());
        StarredPokemon newStarredPokemon = new StarredPokemon(pokemon.orElseThrow(PokemonNotFoundException::new));
        newStarredPokemon.setNickname(payload.getNickname());

        return starredRepository.insert(newStarredPokemon);
    }

    private void validateIfStarredPokemonAlreadyRegistered(StarredPokemonPayload payload) {
        Optional<StarredPokemon> starredPokemon = starredRepository.findByPokemonId(payload.getPokemonId());

        if (starredPokemon.isPresent()) throw new PokemonAlreadyRegisteredException();
    }

    public void deletePokemonBy(Integer pokemonId) {
        Optional<StarredPokemon> pokemon = starredRepository.findByPokemonId(pokemonId);

        starredRepository.delete(pokemon.orElseThrow(PokemonNotFoundException::new));
    }

    public StarredPokemon editNicknameOf(Integer pokemonId, String nickname) {
        Optional<StarredPokemon> pokemon = starredRepository.findByPokemonId(pokemonId);

        pokemon.orElseThrow(PokemonNotFoundException::new).setNickname(nickname);

        return pokemon.get();
    }

    public void deleteAll() {
        starredRepository.deleteAll();
    }
}
