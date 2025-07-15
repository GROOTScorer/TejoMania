package io.github.grootscorer.tejomania.enums;

public enum DificultadCPU {
    FACIL("Facil"), INTERMEDIO("Intermedio"), DIFICIL("Dificil");

    private final String nombre;

    DificultadCPU(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
