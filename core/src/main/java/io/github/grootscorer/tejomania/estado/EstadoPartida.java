package io.github.grootscorer.tejomania.estado;

import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;

public class EstadoPartida {
    private String jugador1;
    private String jugador2;
    private TipoJuegoLibre tipoJuegoLibre;
    private int puntaje1;
    private int puntaje2;
    private float tiempoRestante;
    private int puntajeGanador;
    private boolean jugandoPorTiempo;
    private boolean jugandoPorPuntaje;
    private boolean jugarConObstaculos;
    private boolean jugarConTirosEspeciales;
    private boolean jugarConModificadores;

    public String getJugador1() {
        return this.jugador1;
    }

    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    public String getJugador2() {
        return this.jugador2;
    }

    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    public TipoJuegoLibre getTipoJuegoLibre() {
        return this.tipoJuegoLibre;
    }

    public void setTipoJuegoLibre(TipoJuegoLibre tipoJuegoLibre) {
        this.tipoJuegoLibre = tipoJuegoLibre;
    }

    public int getPuntaje1() {
        return this.puntaje1;
    }

    public void setPuntaje1(int puntaje1) {
        this.puntaje1 = puntaje1;
    }

    public int getPuntaje2() {
        return this.puntaje2;
    }

    public void setPuntaje2(int puntaje2) {
        this.puntaje2 = puntaje2;
    }

    public float getTiempoRestante() {
        return this.tiempoRestante;
    }

    public void setTiempoRestante(float tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public void actualizarTiempo(float delta) {
        if (jugandoPorTiempo && tiempoRestante > 0) {
            tiempoRestante -= delta;
            if (tiempoRestante < 0) tiempoRestante = 0;
        }
    }

    public int getPuntajeGanador() {
        return this.puntajeGanador;
    }

    public void setPuntajeGanador(int puntajeGanador) {
        this.puntajeGanador = puntajeGanador;
    }

    public boolean isJugandoPorTiempo() {
        return this.jugandoPorTiempo;
    }

    public void setJugandoPorTiempo(boolean jugandoPorTiempo) {
        this.jugandoPorTiempo = jugandoPorTiempo;
    }

    public boolean isJugandoPorPuntaje() {
        return this.jugandoPorPuntaje;
    }

    public void setJugandoPorPuntaje(boolean jugandoPorPuntaje) {
        this.jugandoPorPuntaje = jugandoPorPuntaje;
    }

    public boolean isJugarConObstaculos() {
        return this.jugarConObstaculos;
    }

    public void setJugarConObstaculos(boolean jugarConObstaculos) {
        this.jugarConObstaculos = jugarConObstaculos;
    }

    public boolean isJugarConTirosEspeciales() {
        return this.jugarConTirosEspeciales;
    }

    public void setJugarConTirosEspeciales(boolean jugarConTirosEspeciales) {
        this.jugarConTirosEspeciales = jugarConTirosEspeciales;
    }

    public boolean isJugarConModificadores() {
        return this.jugarConModificadores;
    }

    public void setJugarConModificadores(boolean jugarConModificadores) {
        this.jugarConModificadores = jugarConModificadores;
    }
}
