package dev.paola.pokedex.exception;

public class PokemonNotFoundException extends RuntimeException {
    public PokemonNotFoundException() {
        super("Pokémon not found!");
    }
}
