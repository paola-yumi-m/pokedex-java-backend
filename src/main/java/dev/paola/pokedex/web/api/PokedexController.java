package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.Pokedex;
import dev.paola.pokedex.dto.PokemonPayload;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.service.PokedexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokedex")
public class PokedexController extends BaseController {

    @Autowired
    private PokedexService pokedexService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Pokedex>> getPokemons() {
        return new ResponseEntity<>(pokedexService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pokedex> addPokemon(@RequestBody @Validated PokemonPayload payload) {
        return new ResponseEntity<>(pokedexService.addPokemon(payload), HttpStatus.CREATED);
    }

    @DeleteMapping("/{pokemonId}")
    public void deletePokemon(@PathVariable Integer pokemonId) {
        pokedexService.deletePokemonBy(pokemonId);
    }

    @ExceptionHandler(PokemonAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handlePokemonAlreadyRegisteredException(PokemonAlreadyRegisteredException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }
}
