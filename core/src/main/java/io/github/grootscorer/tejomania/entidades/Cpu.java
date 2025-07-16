package io.github.grootscorer.tejomania.entidades;

public class Cpu extends Jugador{
    private int dificultad;

    public Cpu(String nombre, int dificultad) {
        super(nombre);
        this.dificultad = dificultad;
    }
}
