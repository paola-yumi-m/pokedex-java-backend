package dev.paola.pokedex;

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
    public ResponseEntity<List<Pokemon>> getAllPokemons() {
        return new ResponseEntity<>(pokemonService.getAllPokemons(), HttpStatus.OK);
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<Optional<Pokemon>> getPokemonById(@PathVariable int pokemonId) {
        return new ResponseEntity<>(pokemonService.getPokemonById(pokemonId), HttpStatus.OK);
    }

}
