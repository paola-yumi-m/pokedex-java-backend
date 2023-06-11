package dev.paola.pokedex.exception;

public class PokemonAlreadyRegisteredException extends RuntimeException {
    public PokemonAlreadyRegisteredException() {
        super("This pokémon is already in your pokédex!");
    }
}
