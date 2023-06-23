package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.PokemonFilter;
import dev.paola.pokedex.service.PokemonService;
import dev.paola.pokedex.dto.Pokemon;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/pokemons")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;

    @GetMapping
    public ResponseEntity<List<Pokemon>> getAllPokemons(@RequestParam(required = false) PokemonFilter filter) {
        return new ResponseEntity<>(pokemonService.getAllPokemons(filter), HttpStatus.OK);
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<Optional<Pokemon>> getPokemonById(@PathVariable int pokemonId) {
        return new ResponseEntity<>(pokemonService.getPokemonById(pokemonId), HttpStatus.OK);
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePokemonNotFoundException(PokemonNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
