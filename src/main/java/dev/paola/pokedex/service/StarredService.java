package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import dev.paola.pokedex.repository.StarredRepository;
import dev.paola.pokedex.web.api.payload.StarredPokemonPayload;
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

        validateIfPokemonExists(starredPokemon);

        return starredPokemon;
    }

    public StarredPokemon addPokemonBy(StarredPokemonPayload payload) {
        Optional<Pokemon> pokemon = pokemonRepository.findByPokemonId(payload.getPokemonId());

        if (pokemon.isEmpty()) {
            throw new PokemonNotFoundException();
        }

        StarredPokemon starredPokemon = new StarredPokemon(pokemon.get());
        starredPokemon.setNickname(payload.getNickname());
        return starredRepository.insert(starredPokemon);
    }

    public void deletePokemonBy(Integer pokemonId) {
        StarredPokemon pokemon = starredRepository.findByPokemonId(pokemonId);

        validateIfPokemonExists(pokemon);

        starredRepository.delete(pokemon);
    }

    public StarredPokemon editNicknameOf(Integer pokemonId, String nickname) {
        StarredPokemon pokemon = starredRepository.findByPokemonId(pokemonId);

        validateIfPokemonExists(pokemon);

        pokemon.setNickname(nickname);
        return pokemon;
    }

    private void validateIfPokemonExists(StarredPokemon starredPokemon) {
        if (isNull(starredPokemon)) throw new PokemonNotFoundException();
    }
}
