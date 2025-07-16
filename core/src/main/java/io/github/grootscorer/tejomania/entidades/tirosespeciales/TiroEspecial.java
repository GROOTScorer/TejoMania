package io.github.grootscorer.tejomania.entidades.tirosespeciales;

import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Jugador;

public class TiroEspecial {
    private Jugador jugadorConPosesion;
    private boolean activo = false;
    private Disco disco;

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean estadoActivado) {
        this.activo = estadoActivado;
    }

    public Disco getDisco() {
        return disco;
    }
}
