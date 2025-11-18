package io.github.grootscorer.tejomania.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class DiscoCliente {
    private float posicionX, posicionY;
    private float velocidadX, velocidadY;

    float escalaY = (float) Gdx.graphics.getHeight() / 480f;

    private final int RADIO_DISCO = (int) (13 * escalaY);
    private  int maxVelocidad = 500;

    private String rutaRelativaSprite = "imagenes/sprites/disco.png";
    private String rutaAbsolutaSprite = Gdx.files.internal(rutaRelativaSprite).file().getAbsolutePath();

    private Texture textura = new Texture(Gdx.files.internal(rutaAbsolutaSprite));

    public DiscoCliente() {
        Random rand = new Random();
        int lado = rand.nextInt(2);
        this.posicionX = (lado == 0) ? 100 : 400;
        this.posicionY = 300;
        this.velocidadX = 0;
        this.velocidadY = 0;
    }

    public void dibujarConTextura(SpriteBatch batch) {
        if (textura != null) {
            int tamanio = RADIO_DISCO * 2;

            batch.draw(textura, posicionX, posicionY, tamanio, tamanio);
        }
    }

    public float getVelocidadX() {
        return this.velocidadX;
    }

    public void setVelocidadX(float velocidadX) {
        this.velocidadX = velocidadX;
        if(this.velocidadX > maxVelocidad) {
            this.velocidadX = maxVelocidad;
        }

        if(this.velocidadX < -maxVelocidad) {
            this.velocidadX = -maxVelocidad;
        }
    }

    public void setVelocidadY(float velocidadY) {
        this.velocidadY = velocidadY;

        if(this.velocidadY > maxVelocidad) {
            this.velocidadY = maxVelocidad;
        }

        if(this.velocidadY < -maxVelocidad) {
            this.velocidadY = -maxVelocidad;
        }
    }

    public float getVelocidadY() {
        return this.velocidadY;
    }

    public float getPosicionX() {
        return this.posicionX;
    }

    public float getPosicionY() {
        return this.posicionY;
    }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }

    public void setPosicion(float x, float y) {
        this.posicionX = x;
        this.posicionY = y;
    }
}
