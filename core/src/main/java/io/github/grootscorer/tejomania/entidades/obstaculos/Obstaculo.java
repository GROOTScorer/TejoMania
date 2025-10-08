package io.github.grootscorer.tejomania.entidades.obstaculos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

public class Obstaculo {
    private float posicionX, posicionY;
    private int tipo;
    private float tiempoVida = 8.0f;

    private final int escalaX = (int) (Gdx.graphics.getWidth() / 640f);
    private final float radio = 30 * escalaX;

    private Circle hitboxCirculo;
    private Rectangle hitboxRectangulo;
    private Polygon hitboxTriangulo;

    public Obstaculo(float posicionX, float posicionY, int tipo) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.tipo = tipo;

        inicializarHitboxes();
    }

    private void inicializarHitboxes() {
        switch (tipo) {
            case 0:
                hitboxCirculo = new Circle(posicionX, posicionY, radio);
                break;
            case 1:
                hitboxRectangulo = new Rectangle(posicionX - radio, posicionY - radio, radio * 2, radio * 2);
                break;
            case 2:
                hitboxTriangulo = new Polygon();
                float[] vertices = {
                        posicionX, posicionY + radio,
                        posicionX - radio, posicionY - radio,
                        posicionX + radio, posicionY - radio
                };
                hitboxTriangulo.setVertices(vertices);
                break;
        }
    }

    public void actualizar(float delta) {
        tiempoVida -= delta;
    }

    public boolean debeEliminar() {
        return tiempoVida <= 0;
    }

    public boolean colisionaConMazo(Mazo mazo) {
        Circle hitboxMazo = new Circle(mazo.getPosicionX() + mazo.getRadioMazo(),
                mazo.getPosicionY() + mazo.getRadioMazo(),
                mazo.getRadioMazo());

        switch (tipo) {
            case 0:
                return hitboxCirculo.overlaps(hitboxMazo);
            case 1:
                return Intersector.overlaps(hitboxMazo, hitboxRectangulo);
            case 2:
                return Intersector.overlaps(hitboxMazo, hitboxTriangulo.getBoundingRectangle());
            default:
                return false;
        }
    }

    public boolean colisionaConDisco(Disco disco) {
        Circle hitboxDisco = new Circle(disco.getPosicionX() + disco.getRadioDisco(),
                disco.getPosicionY() + disco.getRadioDisco(),
                disco.getRadioDisco());

        switch (tipo) {
            case 0:
                return hitboxCirculo.overlaps(hitboxDisco);
            case 1:
                return Intersector.overlaps(hitboxDisco, hitboxRectangulo);
            case 2:
                return Intersector.overlaps(hitboxDisco, hitboxTriangulo.getBoundingRectangle());
            default:
                return false;
        }
    }

    public void manejarColisionConMazo(Mazo mazo) {
        ManejoDeAudio.activarSonido((String.valueOf(Gdx.files.internal("audio/sonidos/sonido_golpe_pared.mp3"))));

        float centroMazoX = mazo.getPosicionX() + mazo.getRadioMazo();
        float centroMazoY = mazo.getPosicionY() + mazo.getRadioMazo();

        float vectorX = centroMazoX - posicionX;
        float vectorY = centroMazoY - posicionY;
        float longitud = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        if (longitud > 0) {
            vectorX /= longitud;
            vectorY /= longitud;

            float distanciaSeparacion = radio + mazo.getRadioMazo() + 5;
            float nuevoCentroMazoX = posicionX + vectorX * distanciaSeparacion;
            float nuevoCentroMazoY = posicionY + vectorY * distanciaSeparacion;

            mazo.setPosicion((int)(nuevoCentroMazoX - mazo.getRadioMazo()),
                    (int)(nuevoCentroMazoY - mazo.getRadioMazo()));
        }

        mazo.setVelocidadX(0);
        mazo.setVelocidadY(0);
    }

    public void manejarColisionConDisco(Disco disco) {
        ManejoDeAudio.activarSonido((String.valueOf(Gdx.files.internal("audio/sonidos/sonido_golpe_pared.mp3"))));

        float centroDiscoX = disco.getPosicionX() + disco.getRadioDisco();
        float centroDiscoY = disco.getPosicionY() + disco.getRadioDisco();

        float vectorNormalX = 0;
        float vectorNormalY = 0;

        if (tipo == 0) {
            vectorNormalX = centroDiscoX - posicionX;
            vectorNormalY = centroDiscoY - posicionY;
        } else if (tipo == 1) {
            float puntoX = Math.max(posicionX - radio, Math.min(centroDiscoX, posicionX + radio));
            float puntoY = Math.max(posicionY - radio, Math.min(centroDiscoY, posicionY + radio));

            vectorNormalX = centroDiscoX - puntoX;
            vectorNormalY = centroDiscoY - puntoY;
        } else if (tipo == 2) {
            float[] vertices = hitboxTriangulo.getVertices();
            float menorDistancia = Float.MAX_VALUE;
            float aristaMasCercanaX = 0;
            float aristaMasCercanaY = 0;

            for (int i = 0; i < vertices.length; i += 2) {
                int nextIdx = (i + 2) % vertices.length;

                float x1 = vertices[i];
                float y1 = vertices[i + 1];
                float x2 = vertices[nextIdx];
                float y2 = vertices[nextIdx + 1];

                float aristaX = x2 - x1;
                float aristaY = y2 - y1;
                float longitudArista = (float) Math.sqrt(aristaX * aristaX + aristaY * aristaY);

                if (longitudArista > 0) {
                    float t = Math.max(0, Math.min(1,
                            ((centroDiscoX - x1) * aristaX + (centroDiscoY - y1) * aristaY) / (longitudArista * longitudArista)));

                    float puntoX = x1 + t * aristaX;
                    float puntoY = y1 + t * aristaY;

                    float dx = centroDiscoX - puntoX;
                    float dy = centroDiscoY - puntoY;
                    float distancia = (float) Math.sqrt(dx * dx + dy * dy);

                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        aristaMasCercanaX = dx;
                        aristaMasCercanaY = dy;
                    }
                }
            }

            vectorNormalX = aristaMasCercanaX;
            vectorNormalY = aristaMasCercanaY;
        }

        float longitud = (float) Math.sqrt(vectorNormalX * vectorNormalX + vectorNormalY * vectorNormalY);

        if (longitud > 0.1f) {
            vectorNormalX /= longitud;
            vectorNormalY /= longitud;

            float separacion = disco.getRadioDisco() + 12;
            float nuevaPosX = (posicionX + vectorNormalX * separacion) - disco.getRadioDisco();
            float nuevaPosY = (posicionY + vectorNormalY * separacion) - disco.getRadioDisco();

            if (tipo != 0) {
                float puntoContactoX = posicionX;
                float puntoContactoY = posicionY;

                if (tipo == 1) {
                    puntoContactoX = Math.max(posicionX - radio, Math.min(centroDiscoX, posicionX + radio));
                    puntoContactoY = Math.max(posicionY - radio, Math.min(centroDiscoY, posicionY + radio));
                }

                nuevaPosX = (puntoContactoX + vectorNormalX * separacion) - disco.getRadioDisco();
                nuevaPosY = (puntoContactoY + vectorNormalY * separacion) - disco.getRadioDisco();
            }

            disco.setPosicion(nuevaPosX, nuevaPosY);

            float velX = disco.getVelocidadX();
            float velY = disco.getVelocidadY();
            float dotProduct = velX * vectorNormalX + velY * vectorNormalY;

            if (dotProduct < 0) {
                float nuevaVelX = velX - 2 * dotProduct * vectorNormalX;
                float nuevaVelY = velY - 2 * dotProduct * vectorNormalY;

                disco.setVelocidadX(nuevaVelX * 0.85f);
                disco.setVelocidadY(nuevaVelY * 0.85f);
            }
        }
    }

    public void dibujar(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BROWN);

        switch (tipo) {
            case 0:
                shapeRenderer.circle(posicionX, posicionY, radio);
                break;
            case 1:
                shapeRenderer.rect(posicionX - radio, posicionY - radio, radio * 2, radio * 2);
                break;
            case 2:
                shapeRenderer.triangle(
                        posicionX, posicionY + radio,
                        posicionX - radio, posicionY - radio,
                        posicionX + radio, posicionY - radio
                );
                break;
        }
    }

    public float getPosicionX() {
        return posicionX;
    }

    public float getPosicionY() {
        return posicionY;
    }

    public int getTipo() {
        return tipo;
    }

    public float getTiempoVida() {
        return tiempoVida;
    }

    public void setTiempoVida(float tiempoVida) {
        this.tiempoVida = tiempoVida;
    }
}
