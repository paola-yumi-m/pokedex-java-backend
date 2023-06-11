package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokedex;
import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokedexRepository;
import dev.paola.pokedex.repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class PokedexService {
    @Autowired
    private PokedexRepository pokedexRepository;
    @Autowired
    private PokemonRepository pokemonRepository;

    public List<Pokedex> findAll() {
        return pokedexRepository.findAll();
    }

    public Pokedex addPokemon(int pokemonId) {
        Optional<Pokemon> pokemon = pokemonRepository.findByPokemonId(pokemonId);

        if (pokemon.isPresent()) {
            validateIfPokemonIsAlreadyRegistered(pokemonId);
            return pokedexRepository.insert(new Pokedex(pokemon.get()));
        }
        throw new PokemonNotFoundException();
    }

    private void validateIfPokemonIsAlreadyRegistered(int pokemonId) {
        Pokedex pokedexPokemon = pokedexRepository.findPokemonById(pokemonId);
        if (nonNull(pokedexPokemon)) {
            throw new PokemonAlreadyRegisteredException();
        }
    }
}