package dev.paola.pokedex.service;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.repository.PokemonRepository;
import dev.paola.pokedex.repository.StarredRepository;
import dev.paola.pokedex.web.api.payload.StarredPokemonPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class StarredServiceTest {
    private StarredService starredService;
    @Mock
    private StarredRepository starredRepository;
    @Mock
    private PokemonRepository pokemonRepository;
    @Captor
    private ArgumentCaptor<StarredPokemon> pokemonCaptor;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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
        when(starredRepository.findByPokemonId(1)).thenReturn(Optional.of(aStarredPokemonWith(1, "Purple")));

        StarredPokemon starredPokemon = starredService.getStarredPokemonById(1);

        verify(starredRepository).findByPokemonId(1);
        assertThat(starredPokemon.getPokemonId(), is(1));
        assertThat(starredPokemon.getNickname(), is("Purple"));
    }

    @Test
    public void should_throw_an_exception_when_starred_pokemon_is_not_found() {
      assertThrows(PokemonNotFoundException.class, () -> starredService.getStarredPokemonById(1));
    }

    @Test
    public void should_add_pokemon_to_starred_pokemons() {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(1);
        when(pokemonRepository.findByPokemonId(1)).thenReturn(Optional.of(pokemon));
        when(starredRepository.insert(aStarredPokemonWith(1, "Red"))).thenReturn(aStarredPokemonWith(1, "Red"));

        StarredPokemon starredPokemon = starredService.addPokemonBy(aPayloadWith("Red"));

        verify(pokemonRepository).findByPokemonId(1);
        verify(starredRepository).insert(aStarredPokemonWith(1, "Red"));
        assertThat(starredPokemon.getPokemonId(), is(1));
    }

    @Test
    public void should_throw_an_exception_when_pokemon_to_add_to_starred_does_not_exist() {
        when(pokemonRepository.findByPokemonId(1)).thenReturn(Optional.empty());

        assertThrows(PokemonNotFoundException.class, () -> starredService.addPokemonBy(aPayloadWith(
            "Purple")));
    }

    @Test
    public void should_delete_pokemon_from_starred_pokemons() {
        when(starredRepository.findByPokemonId(1)).thenReturn(Optional.of(aStarredPokemonWith(1)));

        starredService.deletePokemonBy(1);

        verify(starredRepository).findByPokemonId(1);
        verify(starredRepository).delete(pokemonCaptor.capture());
        assertThat(pokemonCaptor.getValue().getPokemonId(), is(1));
    }

    @Test
    public void should_throw_an_exception_when_starred_pokemon_to_delete_is_not_found() {
        assertThrows(PokemonNotFoundException.class, () -> starredService.deletePokemonBy(1));
    }

    @Test
    public void should_edit_the_nickname_when_given_an_id() {
        when(starredRepository.findByPokemonId(1)).thenReturn(Optional.of(aStarredPokemonWith(1, "Purple")));

        StarredPokemon editedPokemon = starredService.editNicknameOf(1, "Red");

        verify(starredRepository).findByPokemonId(1);
        verify(starredRepository).save(aStarredPokemonWith(1, "Red"));
        assertThat(editedPokemon.getPokemonId(), is(1));
        assertThat(editedPokemon.getNickname(), is("Red"));
    }

    @Test
    public void should_throw_an_exception_when_pokemon_to_edit_nickname_is_not_found() {
        assertThrows(PokemonNotFoundException.class, () -> starredService.editNicknameOf(1, "Purple"));
    }


    private StarredPokemon aStarredPokemonWith(int pokemonId, String nickname) {
        StarredPokemon starredPokemon = aStarredPokemonWith(pokemonId);
        starredPokemon.setNickname(nickname);
        return starredPokemon;
    }

    private StarredPokemon aStarredPokemonWith(int pokemonId) {
        StarredPokemon starredPokemon = new StarredPokemon();
        starredPokemon.setPokemonId(pokemonId);
        return starredPokemon;
    }

    private StarredPokemonPayload aPayloadWith(String nickname) {
        StarredPokemonPayload payload = new StarredPokemonPayload();
        payload.setPokemonId(1);
        payload.setNickname(nickname);
        return payload;
    }
}
