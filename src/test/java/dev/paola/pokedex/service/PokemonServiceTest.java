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
    private PokemonRepository pokemonRepository;
    private PokemonService pokemonService;

    @BeforeEach
    public void setup() {
        pokemonRepository = mock(PokemonRepository.class);
        pokemonService = new PokemonService(pokemonRepository);
    }

    @Test
    public void deve_retornar_uma_lista_de_pokemons() {
        when(pokemonRepository.findAll()).thenReturn(List.of(aPokemonWith(1), aPokemonWith(2)));

        List<Pokemon> pokemons = pokemonService.getAllPokemons();

        assertThat(pokemons.size(), is(2));
        assertThat(pokemons.get(0).getPokemonId(), is(1));
        assertThat(pokemons.get(1).getPokemonId(), is(2));
    }

    @Test
    public void deve_retornar_um_pokemon_pelo_seu_id() {
        when(pokemonRepository.findByPokemonId(1)).thenReturn(Optional.of(aPokemonWith(1)));

        Optional<Pokemon> pokemon = pokemonService.getPokemonById(1);

        Assertions.assertTrue(pokemon.isPresent());
        assertThat(pokemon.get().getPokemonId(), is(1));
    }

    @Test
    public void deve_lancar_excecao_quando_nao_encontrar_o_pokemon() {
        PokemonNotFoundException exception = assertThrows(PokemonNotFoundException.class, () -> pokemonService.getPokemonById(1000));

        assertThat(exception.getMessage(), is("Pokémon not found!"));
    }

    private Pokemon aPokemonWith(int pokemonId) {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(pokemonId);
        return pokemon;
    }
}