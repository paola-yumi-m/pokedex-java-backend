package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import dev.paola.pokedex.repository.StarredRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class StarredServiceTest {
    public static final String ERROR_MESSAGE_POKEMON_NOT_FOUND = "Pokémon not found!";
    private StarredService starredService;
    private StarredRepository starredRepository;

    private PokemonRepository pokemonRepository;


    @BeforeEach
    public void setup() {
        starredRepository = mock(StarredRepository.class);
        pokemonRepository = mock(PokemonRepository.class);
        starredService = new StarredService(starredRepository, pokemonRepository);
    }
    @Test
    public void should_return_a_list_of_starred_pokemons() {
        when(starredRepository.findAll()).thenReturn(List.of(aStarredPokemonWith(1, "Purple"),
                aStarredPokemonWith(2, "Red")));

        List<StarredPokemon> starredPokemons = starredService.getAllStarredPokemons();

        verify(starredRepository).findAll();
        assertThat(starredPokemons.size(), is(2));
        assertThat(starredPokemons.get(0).getPokemonId(), is(1));
        assertThat(starredPokemons.get(0).getNickname(), is("Purple"));
        assertThat(starredPokemons.get(1).getPokemonId(), is(2));
        assertThat(starredPokemons.get(1).getNickname(), is("Red"));
    }

    @Test
    public void should_return_a_starred_pokemon_by_its_id() {
        when(starredRepository.findByPokemonId(1)).thenReturn(aStarredPokemonWith(1, "Purple"));

        StarredPokemon starredPokemon = starredService.getStarredPokemonById(1);

        verify(starredRepository).findByPokemonId(1);
        assertThat(starredPokemon.getPokemonId(), is(1));
        assertThat(starredPokemon.getNickname(), is("Purple"));
    }

    @Test
    public void should_throw_an_exception_when_starred_pokemon_is_not_found() {
        when(starredRepository.findByPokemonId(1)).thenReturn(null);

        PokemonNotFoundException exception = assertThrows(PokemonNotFoundException.class, () -> starredService.getStarredPokemonById(1));

        assertThat(exception.getMessage(), is(ERROR_MESSAGE_POKEMON_NOT_FOUND));
    }

    @Test
    public void should_add_pokemon_to_starred_pokemons() {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(1);
        when(pokemonRepository.findByPokemonId(1)).thenReturn(Optional.of(pokemon));
        when(starredRepository.insert(aStarredPokemonWith(1, "Red"))).thenReturn(aStarredPokemonWith(1, "Red"));

        StarredPokemon starredPokemon = starredService.addPokemonBy(1, "Red");

        verify(pokemonRepository).findByPokemonId(1);
        verify(starredRepository).insert(aStarredPokemonWith(1, "Red"));
        assertThat(starredPokemon.getPokemonId(), is(1));
    }

    @Test
    public void should_throw_an_exception_when_pokemon_to_add_to_starred_does_not_exist() {

    }


    private StarredPokemon aStarredPokemonWith(int pokemonId, String nickname) {
        StarredPokemon starredPokemon = new StarredPokemon();
        starredPokemon.setPokemonId(pokemonId);
        starredPokemon.setNickname(nickname);
        return starredPokemon;
    }

}