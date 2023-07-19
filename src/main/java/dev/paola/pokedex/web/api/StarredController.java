package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.exception.PokemonNotFoundException;
import dev.paola.pokedex.service.StarredService;
import dev.paola.pokedex.web.api.payload.StarredPokemonPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/starred")
public class StarredController {

    @Autowired
    private StarredService starredService;

    @GetMapping
    public ResponseEntity<List<StarredPokemon>> getAllStarredPokemons() {
        return new ResponseEntity<>(starredService.getAllStarredPokemons(), HttpStatus.OK);
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<StarredPokemon> getStarredPokemonBy(@PathVariable Integer pokemonId) {
        return new ResponseEntity<>(starredService.getStarredPokemonById(pokemonId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StarredPokemon> addStarredPokemonBy(@Validated @RequestBody StarredPokemonPayload payload)  {
        return new ResponseEntity<>(starredService.addPokemonBy(payload), HttpStatus.CREATED);
    }

    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<String> deleteStarredPokemonBy(@PathVariable Integer pokemonId) {
        starredService.deletePokemonBy(pokemonId);
        return new ResponseEntity<>("Pokémon deleted from your starred pokémons!", HttpStatus.OK);
    }

    @PatchMapping("/{pokemonId}")
    public ResponseEntity<StarredPokemon> editNicknameOf(@PathVariable Integer pokemonId, @RequestBody Map<String, String> payload) {
        return new ResponseEntity<>(starredService.editNicknameOf(pokemonId, payload.get("nickname")), HttpStatus.OK);
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
