package io.github.grootscorer.tejomania.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Disco {
    private Jugador jugadorConPosesion, jugadorSinPosesion;
    private int posicionX, posicionY;
    private int velocidadX, velocidadY;
    private Circle hitboxDisco;
    private Texture texturaDisco;
    private Image imagenDisco;

    public Disco(int posicionX, int posicionY, int velocidadX, int velocidadY) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
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

    public Circle getHitbox() {
        return hitboxDisco;
    }

    public Jugador getJugadorSinPosesion() {
        return jugadorSinPosesion;
    }

    public Jugador getJugadorConPosesion() {
        return jugadorConPosesion;
    }

    public Texture getTexturaDisco() {
        return texturaDisco;
    }

    public void reiniciarPosicion() {
        this.posicionX = 500;
        this.posicionY = 500;
    }

    public void setImagenDisco(Texture nuevaTextura) {
        this.imagenDisco = new Image(nuevaTextura);
    }
}
