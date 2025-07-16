package io.github.grootscorer.tejomania.entidades;

public class Jugador {
    private String nombre;
    private int posicionX, posicionY;
    private int velocidadX = 0, velocidadY = 0;

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getVelocidadX() {
        return velocidadX;
    }

    public int getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadX(int velocidadX) {
        this.velocidadX = velocidadX;
    }

    public void setVelocidadY(int velocidadY) {
        this.velocidadY = velocidadY;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }
}
