package io.github.grootscorer.tejomania.entidades.modificadores;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Jugador;

import java.util.Random;

public abstract class Modificador {
    protected int radio = 20;
    protected float posicionX, posicionY;
    protected Jugador jugadorConPosesion;
    protected Jugador jugadorSinPosesion;
    protected boolean colision, activo = false;
    protected float tiempoVida = 0;
    protected final float TIEMPO_VIDA_MAXIMO = 10.0f;
    protected Circle hitboxModificador;
    protected Texture texturaPowerUp;
    protected Image imagenPowerUp;
    protected Disco disco;
    protected Random random = new Random();

    public Modificador() {
        this.hitboxModificador = new Circle();
    }

    public void inicializar(float xCancha, float yCancha, float CANCHA_ANCHO, float CANCHA_ALTO,
                            float mazo1X, float mazo1Y, float mazo2X, float mazo2Y,
                            float discoX, float discoY, float radioMazo, float radioDisco) {
        boolean posicionValida = false;
        int intentos = 0;
        final int MAX_INTENTOS = 50;

        while (!posicionValida && intentos < MAX_INTENTOS) {
            this.posicionX = xCancha + random.nextFloat() * (CANCHA_ANCHO - radio * 2);
            this.posicionY = yCancha + random.nextFloat() * (CANCHA_ALTO - radio * 2);

            float distanciaMazo1 = distancia(this.posicionX + radio, this.posicionY + radio,
                mazo1X + radioMazo, mazo1Y + radioMazo);
            float distanciaMazo2 = distancia(this.posicionX + radio, this.posicionY + radio,
                mazo2X + radioMazo, mazo2Y + radioMazo);
            float distanciaDisco = distancia(this.posicionX + radio, this.posicionY + radio,
                discoX + radioDisco, discoY + radioDisco);

            float distanciaMinima = Math.max(radioMazo, radioDisco) + radio + 30;

            if (distanciaMazo1 > distanciaMinima && distanciaMazo2 > distanciaMinima &&
                distanciaDisco > distanciaMinima) {
                posicionValida = true;
            }
            intentos++;
        }

        this.hitboxModificador.setPosition(posicionX + radio, posicionY + radio);
        this.hitboxModificador.setRadius(radio);
    }

    private float distancia(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void actualizar(float delta) {
        tiempoVida += delta;

        if (disco != null && !activo) {
            verificarColision();
        }
    }

    public void verificarColision() {
        if (disco != null && hitboxModificador.overlaps(disco.getHitbox())) {
            activarModificador();
        }
    }

    protected void activarModificador() {
        this.activo = true;
        ejecutarEfecto();
    }

    protected abstract void ejecutarEfecto();

    public void desactivarModificador() {
        this.activo = false;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean debeDesaparecer() {
        return (tiempoVida >= TIEMPO_VIDA_MAXIMO && !activo) || debeDesaparecerEspecifico();
    }

    protected boolean debeDesaparecerEspecifico() {
        return false;
    }

    public void dibujar(SpriteBatch batch) {
        if (texturaPowerUp != null && !activo) {
            batch.draw(texturaPowerUp, posicionX, posicionY, radio * 2, radio * 2);
        }
    }

    public Jugador getJugadorSinPosesion() {
        return jugadorSinPosesion;
    }

    public Jugador getJugadorConPosesion() {
        return jugadorConPosesion;
    }

    public void setDisco(Disco disco) {
        this.disco = disco;
    }

    public float getPosicionX() {
        return posicionX;
    }

    public float getPosicionY() {
        return posicionY;
    }

    public Circle getHitbox() {
        return hitboxModificador;
    }

    public void dispose() {
        if (texturaPowerUp != null) {
            texturaPowerUp.dispose();
        }
    }
}
