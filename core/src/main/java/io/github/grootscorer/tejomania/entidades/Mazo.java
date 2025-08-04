package io.github.grootscorer.tejomania.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Mazo {
    private float posicionX, posicionY;
    private float velocidadX, velocidadY;
    private final int RADIO_MAZO = 25;
    private Texture textura;
    private Texture texturaEncendida;

    private Texture spritesheet;
    private TextureRegion[] frames;
    private final int SPRITE_WIDTH = 512;
    private final int SPRITE_HEIGHT = 512;
    private final int TOTAL_FRAMES = 4;
    private float tiempoFrame = 0;
    private final float DURACION_FRAME = 0.1f;
    private boolean animandoSprites = false;
    private int frameActual = 0;
    private boolean direccionIda = true;

    private boolean estaEncendido = false;
    private float tiempoEncendido = 0;
    private final float DURACION_ENCENDIDO = 0.5f;

    public Mazo() {
        this.posicionX = 0;
        this.posicionY = 0;
        this.velocidadX = 0;
        this.velocidadY = 0;
    }

    public void setSpritesheet(Texture spritesheet) {
        this.spritesheet = spritesheet;
        if (spritesheet != null) {
            frames = new TextureRegion[TOTAL_FRAMES];
            for (int i = 0; i < TOTAL_FRAMES; i++) {
                frames[i] = new TextureRegion(spritesheet, i * SPRITE_WIDTH, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
            }
        }
    }

    public void actualizarPosicion(int limiteIzq, int limiteDer, int limiteInf, int limiteSup) {
        posicionX += velocidadX;
        posicionY += velocidadY;

        if (posicionX < limiteIzq) {
            posicionX = limiteIzq;
        }
        if (posicionX + (RADIO_MAZO * 2) > limiteDer) {
            posicionX = limiteDer - (RADIO_MAZO * 2);
        }
        if (posicionY < limiteInf) {
            posicionY = limiteInf;
        }
        if (posicionY + (RADIO_MAZO * 2) > limiteSup) {
            posicionY = limiteSup - (RADIO_MAZO * 2);
        }
    }

    public void actualizarAnimacion(float delta) {
        if (estaEncendido) {
            tiempoEncendido += delta;
            if (tiempoEncendido >= DURACION_ENCENDIDO) {
                estaEncendido = false;
                tiempoEncendido = 0;
            }
        }

        if (animandoSprites && frames != null) {
            tiempoFrame += delta;

            while (tiempoFrame >= DURACION_FRAME) {
                tiempoFrame -= DURACION_FRAME;

                if (direccionIda) {
                    frameActual++;
                    if (frameActual >= TOTAL_FRAMES - 1) {
                        frameActual = TOTAL_FRAMES - 1;
                        direccionIda = false;
                    }
                } else {
                    frameActual--;
                    if (frameActual <= 0) {
                        frameActual = 0;
                        animandoSprites = false;
                        direccionIda = true;
                        tiempoFrame = 0;
                    }
                }
            }
        }
    }

    public void iniciarAnimacionSprites() {
        if (frames != null && !animandoSprites) {
            animandoSprites = true;
            tiempoFrame = 0;
            frameActual = 0;
            direccionIda = true;
        }
    }

    public void dibujar(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(this.posicionX + RADIO_MAZO, this.posicionY + RADIO_MAZO, RADIO_MAZO);
    }

    public void dibujarConTextura(SpriteBatch batch) {
        int tamanio = RADIO_MAZO * 2;
        boolean dibujoExitoso = false;

        if (animandoSprites && frames != null && frameActual >= 0 && frameActual < frames.length && frames[frameActual] != null) {
            batch.draw(frames[frameActual], posicionX, posicionY, tamanio, tamanio);
            dibujoExitoso = true;
        }

        if (!dibujoExitoso) {
            Texture texturaActual = (estaEncendido && texturaEncendida != null) ? texturaEncendida : textura;

            if (texturaActual != null) {
                batch.draw(texturaActual, posicionX, posicionY, tamanio, tamanio);
                dibujoExitoso = true;
            }
        }

        if (!dibujoExitoso && textura != null) {
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

    public int getRadioMazo() {
        return this.RADIO_MAZO;
    }

    public Texture getTextura() {
        return this.textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }

    public void setTexturaEncendida(Texture texturaEncendida) {
        this.texturaEncendida = texturaEncendida;
    }

    public void activarEncendido() {
        estaEncendido = true;
        tiempoEncendido = 0;
        iniciarAnimacionSprites();
    }

    public boolean estaEncendido() {
        return estaEncendido;
    }

    public boolean estaAnimandoSprites() {
        return animandoSprites;
    }
}
