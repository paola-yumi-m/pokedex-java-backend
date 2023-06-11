package dev.paola.pokedex;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class PokemonControllerTest {

    public static final String URL_POKEMONS = "http://localhost:8080/api/v1/pokemons";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deve_retornar_200_quando_obter_todos_os_pokemons() throws Exception {
        ResultActions result = mockMvc.perform(get(URL_POKEMONS));

        result.andExpect(status().isOk());
    }

    @Test
    public void deve_retornar_200_quando_obter_pokemon_por_id() throws Exception {
        ResultActions result = mockMvc.perform(get(URL_POKEMONS + "/1"));

        result.andExpect(status().isOk());
    }

    @Test
    public void deve_retornar_404_quando_nao_encontrar_o_id_do_pokemon() throws Exception {
        ResultActions result = mockMvc.perform(get(URL_POKEMONS + "/1000"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string("Pokémon not found!"));
    }
}