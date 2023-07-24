package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.exception.PokemonAlreadyRegisteredException;
import dev.paola.pokedex.service.StarredService;
import dev.paola.pokedex.web.api.payload.StarredPokemonPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/starred")
public class StarredController extends BaseController {

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

    @PatchMapping
    public ResponseEntity<StarredPokemon> editNicknameOf(@RequestBody @Validated StarredPokemonPayload payload) {
        return new ResponseEntity<>(starredService.editNicknameOf(payload.getPokemonId(), payload.getNickname()), HttpStatus.OK);
    }

    @ExceptionHandler(PokemonAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handlePokemonAlreadyRegisteredException(PokemonAlreadyRegisteredException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }
}
