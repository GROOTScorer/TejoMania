package io.github.grootscorer.tejomania.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Mazo {
    private int posicionX, posicionY;
    private int velocidadX = 0, velocidadY = 0;
    private Texture textura;

    private final int RADIO_MAZO = 30;

    public int getVelocidadX() {
        return this.velocidadX;
    }

    public int getVelocidadY() {
        return this.velocidadY;
    }

    public void setVelocidadX(int velocidadX) {
        this.velocidadX = velocidadX;
    }

    public void setVelocidadY(int velocidadY) {
        this.velocidadY = velocidadY;
    }

    public int getPosicionX() {
        return this.posicionX;
    }

    public int getPosicionY() {
        return this.posicionY;
    }

    public void setPosicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }

    public int getRadioMazo() {
        return this.RADIO_MAZO;
    }

    public void actualizarPosicion(int limiteIzquierdo, int limiteDerecho, int limiteInferior, int limiteSuperior) {
        this.posicionX += velocidadX;
        this.posicionY += velocidadY;

        this.posicionX = Math.max(limiteIzquierdo, Math.min(this.posicionX, limiteDerecho - (RADIO_MAZO * 2)));
        this.posicionY = Math.max(limiteInferior, Math.min(this.posicionY, limiteSuperior - (RADIO_MAZO * 2)));
    }

    public void dibujar(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(posicionX + RADIO_MAZO, posicionY + RADIO_MAZO, RADIO_MAZO);
    }

    public void dibujarConTextura(SpriteBatch batch) {
        if (textura != null) {
            int tamaño = RADIO_MAZO * 2;

            batch.draw(textura, posicionX, posicionY, tamaño, tamaño);
        }
    }

    public Texture getTextura() {
        return this.textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }
}
