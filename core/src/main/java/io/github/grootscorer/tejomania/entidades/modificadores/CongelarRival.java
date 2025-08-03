package io.github.grootscorer.tejomania.entidades.modificadores;

import io.github.grootscorer.tejomania.entidades.Jugador;

public class CongelarRival extends Modificador {
    private Jugador jugador1, jugador2;
    public void congelarRival() {
        if(isActivo()) {
            if(getJugadorSinPosesion() == jugador1) {
                //jugador1.setPosicion(jugador1.getPosicionX(), jugador1.getPosicionY());
            }
        }
    }
}
