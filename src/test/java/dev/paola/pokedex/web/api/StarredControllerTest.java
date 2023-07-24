package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.service.StarredService;
import dev.paola.pokedex.web.api.payload.StarredPokemonPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class StarredControllerTest {
    private static final String URL_STARRED = "http://localhost:8080/api/v1/starred";
    private static final String JSON_POKEMON_ID_1_WITH_NICKNAME_PURPLE = "{\"pokemonId\": \"1\", \"nickname\": \"Purple\"}";
    private static final String ERROR_MESSAGE_POKEMON_NOT_FOUND = "Pokémon not found!";
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
        when(starredService.getAllStarredPokemons()).thenReturn(List.of(aStarredPokemonWithNickname(1), aStarredPokemonWithNickname(2)));

        ResultActions result = mockMvc.perform(get(URL_STARRED));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.[0].pokemonId", is(1)));
        result.andExpect(jsonPath("$.[1].pokemonId", is(2)));

    }

    @Test
    public void should_return_200_when_retrieves_favorite_pokemon_by_id() throws Exception {
        when(starredService.getStarredPokemonById(1)).thenReturn(aStarredPokemonWithNickname(1));

        ResultActions result = mockMvc.perform(get(URL_STARRED + "/1"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.pokemonId", is(1)));

    }

    @Test
    public void should_return_404_when_favorite_pokemon_does_not_exist() throws Exception {
        doThrow(new PokemonNotFoundException()).when(starredService).getStarredPokemonById(1);

        ResultActions result = mockMvc.perform(get(URL_STARRED + "/1"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string(ERROR_MESSAGE_POKEMON_NOT_FOUND));
    }

    @Test
    public void should_return_201_when_adding_new_favorite_pokemon_successfully() throws Exception {
        when(starredService.addPokemonBy(aPayloadWithNickname())).thenReturn(aStarredPokemonWithNickname(1));

        ResultActions result = mockMvc.perform(post(URL_STARRED).contentType(MediaType.APPLICATION_JSON).content(JSON_POKEMON_ID_1_WITH_NICKNAME_PURPLE));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.pokemonId", is(1)));
    }

    @Test
    public void should_return_404_when_pokemon_to_add_to_favorites_does_not_exist() throws Exception {
        doThrow(new PokemonNotFoundException()).when(starredService).addPokemonBy(aPayloadWithNickname());

        ResultActions result = mockMvc.perform(post(URL_STARRED).contentType(MediaType.APPLICATION_JSON).content(JSON_POKEMON_ID_1_WITH_NICKNAME_PURPLE));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string(ERROR_MESSAGE_POKEMON_NOT_FOUND));
    }

    @Test
    public void should_return_422_when_pokemon_is_already_in_favorite_pokemons() throws Exception {
        doThrow(new PokemonAlreadyRegisteredException()).when(starredService).addPokemonBy(aPayloadWithNickname());

        ResultActions result = mockMvc.perform(post(URL_STARRED).contentType(MediaType.APPLICATION_JSON).content(JSON_POKEMON_ID_1_WITH_NICKNAME_PURPLE));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(content().string("This pokémon is already registered here!"));
    }

    @Test
    public void should_return_200_when_deleting_a_pokemon_from_favorites_successfully() throws Exception {
        ResultActions result = mockMvc.perform(delete(URL_STARRED + "/1"));

        result.andExpect(status().isOk());
        result.andExpect(content().string("Pokémon deleted from your starred pokémons!"));
    }

    @Test
    public void should_return_404_when_pokemon_to_delete_from_favorites_does_not_exist() throws Exception {
        doThrow(new PokemonNotFoundException()).when(starredService).deletePokemonBy(1);

        ResultActions result = mockMvc.perform(delete(URL_STARRED + "/1"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string(ERROR_MESSAGE_POKEMON_NOT_FOUND));
    }

    @Test
    public void should_return_204_when_editing_pokemon_nickname_successfully() throws Exception {
        when(starredService.editNicknameOf(1, "Purple")).thenReturn(aStarredPokemonWithNickname(1));

        ResultActions result = mockMvc.perform(patch(URL_STARRED).contentType(MediaType.APPLICATION_JSON).content(JSON_POKEMON_ID_1_WITH_NICKNAME_PURPLE));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.pokemonId", is(1)));
        result.andExpect(jsonPath("$.nickname", is("Purple")));
    }

    @Test
    public void should_return_404_when_pokemon_to_change_nickname_does_not_exist() throws Exception {
        doThrow(new PokemonNotFoundException()).when(starredService).editNicknameOf(1, "Purple");

        ResultActions result = mockMvc.perform(patch(URL_STARRED).contentType(MediaType.APPLICATION_JSON).content(JSON_POKEMON_ID_1_WITH_NICKNAME_PURPLE));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string(ERROR_MESSAGE_POKEMON_NOT_FOUND));
    }

    private StarredPokemon aStarredPokemonWithNickname(int pokemonId) {
        StarredPokemon pokemon = new StarredPokemon();
        pokemon.setPokemonId(pokemonId);
        pokemon.setNickname("Purple");
        return pokemon;
    }

    private StarredPokemonPayload aPayloadWithNickname() {
        StarredPokemonPayload payload = new StarredPokemonPayload();
        payload.setPokemonId(1);
        payload.setNickname("Purple");
        return payload;
    }

}
