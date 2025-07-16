package io.github.grootscorer.tejomania.entidades.modificadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Jugador;

public class Modificador {
    private int radio;
    private int posicionX, posicionY;
    private Jugador jugadorConPosesion;
    private Jugador jugadorSinPosesion;
    private boolean colision, activo = false;
    private int tiempo;
    private Circle hitboxModificador;
    Texture texturaPowerUp;
    Image imagenPowerUp;
    private Disco disco;

    public void verificarColision() {
        if(hitboxModificador.overlaps(disco.getHitbox())) {
            activarModificador();
        }
    }

    private void setColision(boolean estado) {
        this.colision = estado;
    }

    private void activarModificador() {
        this.activo = true;
    }

    private void desactivarModificador() {
        this.activo = false;
    }

    public boolean isActivo() {
        return activo;
    }

    public Jugador getJugadorSinPosesion() {
        return jugadorSinPosesion;
    }

    public Jugador getJugadorConPosesion() {
        return jugadorConPosesion;
    }
}
