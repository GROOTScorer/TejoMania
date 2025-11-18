package io.github.grootscorer.tejomania.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MazoCliente {
    private float posicionX, posicionY;
    private float velocidadX, velocidadY;

    float escalaY = (float) Gdx.graphics.getHeight() / 480f;

    private final int RADIO_MAZO = (int) (18 * escalaY);
    private Texture textura;

    public MazoCliente() {
        this.posicionX = 0;
        this.posicionY = 0;
        this.velocidadX = 0;
        this.velocidadY = 0;
    }

    public void dibujarConTextura(SpriteBatch batch) {
        int tamanio = RADIO_MAZO * 2;

        if (textura != null) {
            batch.draw(textura, posicionX, posicionY, tamanio, tamanio);
        }
    }

    public float getPosicionX() {
        return this.posicionX;
    }

    public float getPosicionY() {
        return this.posicionY;
    }

    public void setPosicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }

    public float getVelocidadX() {
        return this.velocidadX;
    }

    public void setVelocidadX(float velocidadX) {
        this.velocidadX = velocidadX;
    }

    public float getVelocidadY() {
        return this.velocidadY;
    }

    public void setVelocidadY(float velocidadY) {
        this.velocidadY = velocidadY;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }
}
