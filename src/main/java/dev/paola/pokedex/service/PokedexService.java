package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokedex;
import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.PokemonPayload;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokedexRepository;
import dev.paola.pokedex.repository.PokemonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokedexService {

    private final PokedexRepository pokedexRepository;
    private final PokemonRepository pokemonRepository;

    public PokedexService(PokedexRepository pokedexRepository, PokemonRepository pokemonRepository) {
        this.pokedexRepository = pokedexRepository;
        this.pokemonRepository = pokemonRepository;
    }

    public List<Pokedex> findAll() {
        return pokedexRepository.findAll();
    }

    public Pokedex findPokedexPokemonById(Integer pokemonId) {
        Optional<Pokedex> pokedexPokemon = pokedexRepository.findByPokemonId(pokemonId);

        return pokedexPokemon.orElseThrow(PokemonNotFoundException::new);
    }

    public Pokedex addPokemon(PokemonPayload payload) {
        Optional<Pokemon> pokemon = pokemonRepository.findByPokemonId(payload.getPokemonId());

            validateIfPokemonIsAlreadyRegistered(payload.getPokemonId());
            return pokedexRepository.insert(new Pokedex(pokemon.orElseThrow(PokemonNotFoundException::new)));
    }

    public void deletePokemonBy(Integer pokemonId) {
        Optional<Pokedex> pokedexPokemon = pokedexRepository.findByPokemonId(pokemonId);

        pokedexRepository.delete(pokedexPokemon.orElseThrow(PokemonNotFoundException::new));
    }

    private void validateIfPokemonIsAlreadyRegistered(int pokemonId) {
        Optional<Pokedex> pokedexPokemon = pokedexRepository.findByPokemonId(pokemonId);

        if (pokedexPokemon.isPresent()) {
            throw new PokemonAlreadyRegisteredException();
        }
    }

    public void deleteAll() {
        pokedexRepository.deleteAll();
    }
}
