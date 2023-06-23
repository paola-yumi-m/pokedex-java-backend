package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.PokemonFilter;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public List<Pokemon> getAllPokemons(PokemonFilter filter) {
        return pokemonRepository.findAll();
    }

    public Optional<Pokemon> getPokemonById(int pokemonId) {
        Optional<Pokemon> pokemon = pokemonRepository.findByPokemonId(pokemonId);

        validateIfPokemonExists(pokemon);
        return pokemon;
    }

    private void validateIfPokemonExists(Optional<Pokemon> pokemon) {
        if (pokemon.isEmpty()) {
            throw new PokemonNotFoundException();
        }
    }
}
