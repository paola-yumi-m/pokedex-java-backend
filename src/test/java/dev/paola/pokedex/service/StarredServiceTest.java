package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.StarredRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class StarredServiceTest {
    public static final String ERROR_MESSAGE_POKEMON_NOT_FOUND = "Pokémon not found!";
    private StarredService starredService;
    private StarredRepository starredRepository;


    @BeforeEach
    public void setup() {
        starredRepository = mock(StarredRepository.class);
        starredService = new StarredService(starredRepository);
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
        doThrow(new PokemonNotFoundException()).when(starredRepository).findByPokemonId(1);

        PokemonNotFoundException exception = Assert.assertThrows(PokemonNotFoundException.class, () -> starredService.getStarredPokemonById(1));

        assertThat(exception.getMessage(), is(ERROR_MESSAGE_POKEMON_NOT_FOUND));
    }


    private StarredPokemon aStarredPokemonWith(int pokemonId, String nickname) {
        StarredPokemon starredPokemon = new StarredPokemon();
        starredPokemon.setPokemonId(pokemonId);
        starredPokemon.setNickname(nickname);
        return starredPokemon;
    }

}