package io.github.grootscorer.tejomania.estado;

import io.github.grootscorer.tejomania.enums.DificultadCPU;
import io.github.grootscorer.tejomania.enums.Pais;
import io.github.grootscorer.tejomania.enums.TipoCompetencia;
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
    private String canchaSeleccionada;
    private DificultadCPU dificultadCPU;
    private TipoCompetencia tipoCompetencia;
    private Pais paisSeleccionado;
    private int fechaLiga;
    private String[] fase = {"Octavos de final", "Cuartos de final", "Semifinales", "Final"};
    private int faseActual;
    private GestorTorneo gestorTorneo;
    private Pais rivalActual;
    private GestorLiga gestorLiga;

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

    public void agregarGolJugador1() {
        this.puntaje1++;
    }

    public void agregarGolJugador2() {
        this.puntaje2++;
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

    public String getCanchaSeleccionada() {
        return this.canchaSeleccionada;
    }

    public void setCanchaSeleccionada(String cancha) {
        this.canchaSeleccionada = cancha;
    }

    public DificultadCPU getDificultadCPU() {
        return this.dificultadCPU;
    }

    public void setDificultadCPU(DificultadCPU dificultad) {
        this.dificultadCPU = dificultad;
    }

    public TipoCompetencia getTipoCompetencia() {
        return this.tipoCompetencia;
    }

    public void setTipoCompetencia(TipoCompetencia tipoCompetencia) {
        this.tipoCompetencia = tipoCompetencia;
    }

    public Pais getPaisSelecionad() {
        return this.paisSeleccionado;
    }

    public void setPaisSeleccionado(Pais pais) {
        this.paisSeleccionado = pais;
    }

    public int getFechaLiga() {
        return this.fechaLiga;
    }

    public void setFechaLiga(int fecha) {
        this.fechaLiga = fecha;
    }

    public int getFaseActual() {
        return this.faseActual;
    }

    public void setFaseActual(int ronda) {
        this.faseActual = ronda;
    }

    public String getNombreFase(int ronda) {
        return this.fase[ronda];
    }

    public GestorTorneo getGestorTorneo() {
        return this.gestorTorneo;
    }

    public void setGestorTorneo(GestorTorneo gestorTorneo) {
        this.gestorTorneo = gestorTorneo;
    }

    public Pais getRivalActual() {
        return this.rivalActual;
    }

    public void setRivalActual(Pais rivalActual) {
        this.rivalActual = rivalActual;
    }

    public GestorLiga getGestorLiga() {
        return this.gestorLiga;
    }

    public void setGestorLiga(GestorLiga gestorLiga) {
        this.gestorLiga = gestorLiga;
    }
}
