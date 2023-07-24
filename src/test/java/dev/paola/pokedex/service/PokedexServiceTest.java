package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokedex;
import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.PokemonPayload;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokedexRepository;
import dev.paola.pokedex.repository.PokemonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class PokedexServiceTest {
    private PokedexRepository pokedexRepository;
    private PokedexService pokedexService;
    private PokemonRepository pokemonRepository;

    @BeforeEach
    public void setup() {
        pokedexRepository = mock(PokedexRepository.class);
        pokemonRepository = mock(PokemonRepository.class);
        pokedexService = new PokedexService(pokedexRepository, pokemonRepository);
    }

    @Test
    public void should_return_pokemons_from_pokedex() {
        when(pokedexRepository.findAll()).thenReturn(List.of(aPokedexPokemonWith(1), aPokedexPokemonWith(2)));

        List<Pokedex> pokedexPokemons = pokedexService.findAll();

        assertThat(pokedexPokemons.size(), is(2));
        assertThat(pokedexPokemons.get(0).getPokemonId(), is(1));
        assertThat(pokedexPokemons.get(1).getPokemonId(), is(2));
    }

    @Test
    public void should_add_a_pokemon_to_the_pokedex_given_a_pokemonId() {
        Pokemon pokemon = aPokemonWithId();
        when(pokemonRepository.findByPokemonId(1)).thenReturn(Optional.of(pokemon));
        when(pokedexRepository.insert(new Pokedex(pokemon))).thenReturn(aPokedexPokemonWith(1));

        Pokedex pokedex = pokedexService.addPokemon(aPokemonPayload());

        assertThat(pokedex.getPokemonId(), is(1));
        verify(pokedexRepository).findByPokemonId(1);
        verify(pokedexRepository).insert(new Pokedex(pokemon));
    }

    @Test
    public void should_throw_an_exception_when_pokemon_is_not_found() {
        PokemonNotFoundException exception = assertThrows(PokemonNotFoundException.class, () -> pokedexService.addPokemon(aPokemonPayload()));

        assertThat(exception.getMessage(), is("Pokémon not found!"));
    }

    @Test
    public void should_throw_an_exception_when_pokemon_is_already_registered_in_pokedex() {
        when(pokemonRepository.findByPokemonId(1)).thenReturn(Optional.of(aPokemonWithId()));
        when(pokedexRepository.findByPokemonId(1)).thenReturn(aPokedexPokemonWith(1));

        PokemonAlreadyRegisteredException exception = assertThrows(PokemonAlreadyRegisteredException.class, () -> pokedexService.addPokemon(aPokemonPayload()));

        assertThat(exception.getMessage(), is("This pokémon is already registered here!"));
    }

    private PokemonPayload aPokemonPayload() {
        PokemonPayload payload = new PokemonPayload();
        payload.setPokemonId(1);
        return payload;
    }

    private Pokemon aPokemonWithId() {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(1);
        return pokemon;
    }

    private Pokedex aPokedexPokemonWith(int pokemonId) {
        Pokedex pokedexPokemon = new Pokedex();
        pokedexPokemon.setPokemonId(pokemonId);
        return pokedexPokemon;
    }

}
