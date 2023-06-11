package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.Pokedex;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.service.PokedexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
class PokedexControllerTest {
    public static final String URL_POKEDEX = "http://localhost:8080/api/v1/pokedex";
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
    public void deve_retornar_200_quando_devolver_todos_os_pokemons_da_pokedex() throws Exception {
        when(pokedexService.findAll()).thenReturn(List.of(aPokedexWith(1), aPokedexWith(2)));

        ResultActions result = mockMvc.perform(get(URL_POKEDEX));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.size()").value(2));
        result.andExpect(jsonPath("$.[0].pokemonId", is(1)));
        result.andExpect(jsonPath("$.[1].pokemonId", is(2)));
    }

    @Test
    public void deve_retornar_201_quando_adicionar_pokemon_na_pokedex() throws Exception {
        when(pokedexService.addPokemon(2)).thenReturn(aPokedexWith(2));

        ResultActions result = mockMvc.perform(post(URL_POKEDEX).contentType(MediaType.APPLICATION_JSON).content("{\"pokemonId\": 2}"));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.pokemonId", is(2)));
    }

    @Test
    public void deve_retornar_404_quando_nao_encontrar_o_pokemon_informado_para_adicionar_na_pokedex() throws Exception {
        doThrow(new PokemonNotFoundException()).when(pokedexService).addPokemon(1001);

        ResultActions result = mockMvc.perform(post(URL_POKEDEX).contentType(MediaType.APPLICATION_JSON).content("{\"pokemonId\": 1001}"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string("Pokémon not found!"));
    }

    @Test
    public void deve_retornar_422_quando_pokemon_ja_existir_na_pokedex() throws Exception {
        doThrow(new PokemonAlreadyRegisteredException()).when(pokedexService).addPokemon(1);

        ResultActions result = mockMvc.perform(post(URL_POKEDEX).contentType(MediaType.APPLICATION_JSON).content("{\"pokemonId\": 1}"));

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(content().string("This pokémon is already in your pokédex!"));
    }

    private Pokedex aPokedexWith(int pokemonId) {
        Pokedex pokedex = new Pokedex();
        pokedex.setPokemonId(pokemonId);
        return pokedex;
    }
}