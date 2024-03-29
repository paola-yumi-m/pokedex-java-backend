package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.Pokedex;
import dev.paola.pokedex.dto.PokemonPayload;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.service.PokedexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
class PokedexControllerTest {
    private static final String URL_POKEDEX = "http://localhost:8080/api/v1/pokedex";
    private MockMvc mockMvc;
    @InjectMocks
    private PokedexController pokedexController;
    @Mock
    private PokedexService pokedexService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = standaloneSetup(pokedexController).build();
    }

    @Test
    public void should_return_200_when_retrieves_all_pokemons_from_the_pokedex() throws Exception {
        when(pokedexService.findAll()).thenReturn(List.of(aPokedexWithId(1), aPokedexWithId(2)));

        ResultActions result = mockMvc.perform(get(URL_POKEDEX));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.size()").value(2));
        result.andExpect(jsonPath("$.[0].pokemonId", is(1)));
        result.andExpect(jsonPath("$.[1].pokemonId", is(2)));
    }

    @Test
    public void should_return_200_when_retrieves_all_pokemon_from_the_pokedex_by_id() throws Exception {
        when(pokedexService.findPokedexPokemonById(1)).thenReturn(aPokedexWithId(1));

        ResultActions result = mockMvc.perform(get(URL_POKEDEX + "/1"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.pokemonId", is(1)));
    }

    @Test
    public void should_return_201_when_adding_a_new_pokemon_to_the_pokedex_successfully() throws Exception {
        when(pokedexService.addPokemon(aPokemonPayloadWithId(2))).thenReturn(aPokedexWithId(2));

        ResultActions result = mockMvc.perform(post(URL_POKEDEX).contentType(MediaType.APPLICATION_JSON).content("{\"pokemonId\": 2}"));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.pokemonId", is(2)));
    }

    @Test
    public void should_return_404_when_pokemon_to_add_does_not_exist() throws Exception {
        doThrow(new PokemonNotFoundException()).when(pokedexService).addPokemon(aPokemonPayloadWithId(1001));

        ResultActions result = mockMvc.perform(post(URL_POKEDEX).contentType(MediaType.APPLICATION_JSON).content("{\"pokemonId\": 1001}"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string("Pokémon not found!"));
    }

    @Test
    public void should_return_422_when_pokemon_is_already_in_the_pokedex() throws Exception {
        doThrow(new PokemonAlreadyRegisteredException()).when(pokedexService).addPokemon(aPokemonPayloadWithId(1));

        ResultActions result = mockMvc.perform(post(URL_POKEDEX).contentType(MediaType.APPLICATION_JSON).content("{\"pokemonId\": 1}"));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(content().string("This pokémon is already registered here!"));
    }

    @Test
    public void should_return_200_when_pokemon_is_successfully_deleted_from_pokedex() throws Exception {
        ResultActions result = mockMvc.perform(delete(URL_POKEDEX + "/1"));

        result.andExpect(status().isOk());
        result.andExpect(content().string("Pokémon deleted from your Pokédex!"));
    }

    @Test
    public void should_return_200_when_all_pokemons_are_successfully_deleted_from_pokedex() throws Exception {
        ResultActions result = mockMvc.perform(delete(URL_POKEDEX));

        result.andExpect(status().isOk());
        result.andExpect(content().string("All pokémons deleted from your Pokédex!"));
    }

    @Test
    public void should_return_404_when_pokemon_to_be_deleted_is_not_found_in_pokedex() throws Exception {
        doThrow(new PokemonNotFoundException()).when(pokedexService).deletePokemonBy(1);

        ResultActions result = mockMvc.perform(delete(URL_POKEDEX + "/1"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string("Pokémon not found!"));
    }

    private PokemonPayload aPokemonPayloadWithId(int pokemonId) {
        PokemonPayload payload = new PokemonPayload();
        payload.setPokemonId(pokemonId);
        return payload;
    }

    private Pokedex aPokedexWithId(int pokemonId) {
        Pokedex pokedex = new Pokedex();
        pokedex.setPokemonId(pokemonId);
        return pokedex;
    }
}
