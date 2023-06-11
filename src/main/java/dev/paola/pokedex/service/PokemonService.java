package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {
    @Autowired
    private PokemonRepository pokemonRepository;

    public List<Pokemon> getAllPokemons() {
        return pokemonRepository.findAll();
    }

    public Optional<Pokemon> getPokemonById(int pokemonId) {
        Optional<Pokemon> pokemon = pokemonRepository.findByPokemonId(pokemonId);

        if (pokemon.isEmpty()) {
            throw new PokemonNotFoundException();
        }
        return pokemon;
    }
}
