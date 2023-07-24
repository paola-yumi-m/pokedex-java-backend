package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

@SpringBootTest
public class PokemonControllerTest {

    private static final String PIKACHU = "Pikachu";
    private static final String URL_POKEMONS = "http://localhost:8080/api/v1/pokemons";
    @InjectMocks
    private PokemonController pokemonController;
    @Mock
    private PokemonService pokemonService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(pokemonController).build();
    }

    @Test
    public void should_return_200_when_retrieves_all_pokemons() throws Exception {
        when(pokemonService.getAllPokemons()).thenReturn(List.of(aPokemonWith(1), aPokemonWith(2)));

        ResultActions result = mockMvc.perform(get(URL_POKEMONS));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.size()").value(2));
        result.andExpect(jsonPath("$.[0].pokemonId", is(1)));
        result.andExpect(jsonPath("$.[1].pokemonId", is(2)));
    }

    @Test
    public void should_return_200_when_retrieves_the_pokemon_by_its_id() throws Exception {
        when(pokemonService.getPokemonById(1)).thenReturn(Optional.of(aPokemonWith(1)));

        ResultActions result = mockMvc.perform(get(URL_POKEMONS + "/1"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.pokemonId", is(1)));
    }

    @Test
    public void should_return_404_when_pokemon_not_found() throws Exception {
        doThrow(new PokemonNotFoundException()).when(pokemonService).getPokemonById(1000);

        ResultActions result = mockMvc.perform(get(URL_POKEMONS + "/1000"));

        result.andExpect(status().isNotFound());
        result.andExpect(content().string("Pokémon not found!"));
    }

    @Test
    public void should_filter_pokemons_by_name() throws Exception {
        when(pokemonService.getPokemonsByName(PIKACHU)).thenReturn(List.of(aPikachu()));

        ResultActions result = mockMvc.perform(get(URL_POKEMONS).param("name", PIKACHU));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.[0].name", is("Pikachu")));
    }

    private Pokemon aPikachu() {
        Pokemon pokemon = aPokemonWith(1);
        pokemon.setName(PIKACHU);
        return pokemon;
    }


    private Pokemon aPokemonWith(int pokemonId) {
        Pokemon pokemon = new Pokemon();
        pokemon.setPokemonId(pokemonId);
        return pokemon;
    }
}
