package dev.paola.pokedex.web.api;

import dev.paola.pokedex.dto.StarredPokemon;
import dev.paola.pokedex.service.StarredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/starred")
public class StarredController {

    @Autowired
    private StarredService starredService;

    @GetMapping
    public ResponseEntity<List<StarredPokemon>> getAllStarredPokemons() {
        return new ResponseEntity<>(starredService.getAllStarredPokemons(), HttpStatus.OK);
    }
}
