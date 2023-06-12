package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.service.StarredService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class StarredControllerTest {
    public static final String URL_STARRED = "http://localhost:8080/api/v1/starred";
    @InjectMocks
    private StarredController starredController;
    private MockMvc mockMvc;
    @Mock
    private StarredService starredService;

    @BeforeEach
    public void setup() {
        openMocks(this);
        this.mockMvc = standaloneSetup(starredController).build();
    }

    @Test
    public void should_return_200_when_retrieves_all_favorite_pokemons() throws Exception {
        when(starredService.getAllStarredPokemons()).thenReturn(List.of(aStarredPokemonWith(1), aStarredPokemonWith(2)));

        ResultActions result = mockMvc.perform(get(URL_STARRED));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.[0].pokemonId", is(1)));
        result.andExpect(jsonPath("$.[1].pokemonId", is(2)));

    }

    @Test
    public void should_return_200_when_retrieves_favorite_pokemon_by_id() {

    }

    @Test
    public void should_return_404_when_favorite_pokemon_does_not_exist() {

    }

    @Test
    public void should_return_201_when_adding_new_favorite_pokemon_successfully() {

    }

    @Test
    public void should_return_404_when_pokemon_to_add_to_favorites_does_not_exist() {

    }

    @Test
    public void should_return_422_when_pokemon_is_already_in_favorite_pokemons() {

    }

    @Test
    public void should_return_200_when_deleting_a_pokemon_from_favorites_successfully() {

    }

    @Test
    public void should_return_404_when_pokemon_to_delete_from_favorites_does_not_exist() {

    }

    @Test
    public void should_return_204_when_editing_pokemon_successfully() {

    }


    private StarredPokemon aStarredPokemonWith(int pokemonId) {
        StarredPokemon pokemon = new StarredPokemon();
        pokemon.setPokemonId(pokemonId);
        return pokemon;
    }


}