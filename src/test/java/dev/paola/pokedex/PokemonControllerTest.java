package dev.paola.pokedex;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.service.PokemonService;
import dev.paola.pokedex.web.api.PokemonController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class PokemonControllerTest {

    @InjectMocks
    private PokemonController pokemonController;
    @Mock
    private PokemonService pokemonService;
    private static final String URL_POKEMONS = "http://localhost:8080/api/v1/pokemons";
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(pokemonController).build();
    }

    @Test
    public void deve_retornar_200_quando_obter_todos_os_pokemons() throws Exception {
        when(pokemonService.getAllPokemons()).thenReturn(List.of(aPokemonWith(1), aPokemonWith(2)));

        ResultActions result = mockMvc.perform(get(URL_POKEMONS));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.size()").value(2));
        result.andExpect(jsonPath("$.[0].pokemonId", is(1)));
        result.andExpect(jsonPath("$.[1].pokemonId", is(2)));
    }

    @Test
    public void deve_retornar_200_quando_obter_pokemon_por_id() throws Exception {
        when(pokemonService.getPokemonById(1)).thenReturn(Optional.of(aPokemonWith(1)));

        ResultActions result = mockMvc.perform(get(URL_POKEMONS + "/1"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.pokemonId", is(1)));
    }

    @Test
    public void deve_retornar_404_quando_nao_encontrar_o_id_do_pokemon() throws Exception {
        doThrow(new PokemonNotFoundException()).when(pokemonService).getPokemonById(1000);

        ResultActions result = mockMvc.perform(get(URL_POKEMONS + "/1000"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string("Pokémon not found!"));
    }

    private Pokemon aPokemonWith(int pokemonId) {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(pokemonId);
        return pokemon;
    }
}