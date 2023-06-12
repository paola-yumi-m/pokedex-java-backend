package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.Pokedex;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.service.PokedexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pokedex")
public class PokedexController {

    @Autowired
    private PokedexService pokedexService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Pokedex>> getPokemons() {
        return new ResponseEntity<>(pokedexService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pokedex> addPokemon(@RequestBody Map<String, Integer> payload) {
        return new ResponseEntity<>(pokedexService.addPokemon(payload.get("pokemonId")), HttpStatus.CREATED);
    }

    @DeleteMapping("/{pokemonId}")
    public void deletePokemon(@PathVariable Integer pokemonId) {
        pokedexService.deletePokemonBy(pokemonId);
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePokemonNotFoundException(PokemonNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(PokemonAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handlePokemonAlreadyRegisteredException(PokemonAlreadyRegisteredException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }
}
