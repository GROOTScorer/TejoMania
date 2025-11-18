package io.github.grootscorer.tejomania.interfaces;

import io.github.grootscorer.tejomania.estado.EstadoPartida;

public interface ControladorJuegoRed {
    void onGol(int direccion); // 1 = izquierda anotó, -1 = derecha anotó
    void onMoverMazo(int numeroJugador, float velocidadX, float velocidadY);
    void onIniciarJuego();
    void onConectar(int numeroJugador, float tiempoRestante, boolean jugandoPorTiempo,
                    boolean jugandoPorPuntaje, int puntajeGanador, boolean conObstaculos,
                    boolean conTirosEspeciales, boolean conModificadores, String cancha);
    void onActualizarPosicionDisco(float x, float y, float velX, float velY);
    void onActualizarPosicionMazo(int numeroJugador, float x, float y);
    void onActualizarPuntaje(int puntaje1, int puntaje2);
    void onFinalizarJuego(int ganador);
    void onVolverAlMenu();
    void onActualizarTiempo(float tiempoRestante);
}
