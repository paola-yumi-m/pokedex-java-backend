package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class PokemonServiceTest {
    private static final String PIKACHU = "Pikachu";
    private PokemonRepository pokemonRepository;
    private PokemonService pokemonService;

    @BeforeEach
    public void setup() {
        pokemonRepository = mock(PokemonRepository.class);
        pokemonService = new PokemonService(pokemonRepository);
    }

    @Test
    public void should_return_a_list_of_pokemons() {
        when(pokemonRepository.findAll()).thenReturn(List.of(aPokemonWithId(1), aPokemonWithId(2)));

        List<Pokemon> pokemons = pokemonService.getAllPokemons();

        assertThat(pokemons.size(), is(2));
        assertThat(pokemons.get(0).getPokemonId(), is(1));
        assertThat(pokemons.get(1).getPokemonId(), is(2));
    }

    @Test
    public void should_return_a_pokemon_by_its_name() {
        when(pokemonRepository.findByNameRegex(PIKACHU)).thenReturn(List.of(aPikachu()));

        List<Pokemon> pokemonsByName = pokemonService.getPokemonsByName(PIKACHU);

        assertThat(pokemonsByName.get(0).getName(), is("Pikachu"));
    }

    @Test
    public void should_return_a_pokemon_by_its_id() {
        when(pokemonRepository.findByPokemonId(1)).thenReturn(Optional.of(aPokemonWithId(1)));

        Optional<Pokemon> pokemon = pokemonService.getPokemonById(1);

        Assertions.assertTrue(pokemon.isPresent());
        assertThat(pokemon.get().getPokemonId(), is(1));
    }

    @Test
    public void should_throw_an_exception_when_pokemon_is_not_found_by_id() {
        assertThrows(PokemonNotFoundException.class, () -> pokemonService.getPokemonById(1000));
    }

    private Pokemon aPokemonWithId(int pokemonId) {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(pokemonId);
        return pokemon;
    }

    private Pokemon aPikachu() {
        Pokemon pokemon = new Pokemon();
        pokemon.setName(PIKACHU);
        return pokemon;
    }
}
