package dev.paola.pokedex.web.api;

import dev.paola.pokedex.service.PokemonService;
import dev.paola.pokedex.dto.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/pokemons")
public class PokemonController extends BaseController {

    @Autowired
    private PokemonService pokemonService;

    @GetMapping
    public ResponseEntity<List<Pokemon>> getAllPokemons() {
        return new ResponseEntity<>(pokemonService.getAllPokemons(), HttpStatus.OK);
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<Pokemon>> getPokemonByName(@RequestParam(required = false) String name) {
        return new ResponseEntity<>(pokemonService.getPokemonsByName(name), HttpStatus.OK);
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<Optional<Pokemon>> getPokemonById(@PathVariable int pokemonId) {
        return new ResponseEntity<>(pokemonService.getPokemonById(pokemonId), HttpStatus.OK);
    }
}
