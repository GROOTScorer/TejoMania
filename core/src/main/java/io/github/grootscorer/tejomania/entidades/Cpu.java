package io.github.grootscorer.tejomania.entidades;

public class Cpu extends Jugador{
    private int dificultad;
    private Mazo mazo;

    public Cpu(String nombre, int dificultad) {
        super(nombre);
        this.dificultad = dificultad;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }
}
