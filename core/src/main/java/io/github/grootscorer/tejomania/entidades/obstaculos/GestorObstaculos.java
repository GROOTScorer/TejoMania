package io.github.grootscorer.tejomania.entidades.obstaculos;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.estado.EstadoObstaculo;

import java.util.Random;

public class GestorObstaculos {
    private Obstaculo obstaculoActivo;
    private float tiempoSinObstaculo = 0;
    private float probabilidadAparicion = 0.1f;
    private Random random = new Random();

    private float xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO;
    private Mazo mazo1, mazo2;
    private Disco disco, discoSecundario;
    private long ultimaColisionObstaculo = 0;

    public GestorObstaculos(float xCancha, float yCancha, float CANCHA_ANCHO, float CANCHA_ALTO,
                            Mazo mazo1, Mazo mazo2, Disco disco) {
        this.xCancha = xCancha;
        this.yCancha = yCancha;
        this.CANCHA_ANCHO = CANCHA_ANCHO;
        this.CANCHA_ALTO = CANCHA_ALTO;
        this.mazo1 = mazo1;
        this.mazo2 = mazo2;
        this.disco = disco;
    }

    public void setDiscoSecundario(Disco discoSecundario) {
        this.discoSecundario = discoSecundario;
    }

    public void actualizar(float delta) {
        if (obstaculoActivo != null) {
            obstaculoActivo.actualizar(delta);
            if (obstaculoActivo.debeEliminar()) {
                obstaculoActivo = null;
            }
        } else {
            tiempoSinObstaculo += delta;
            if (tiempoSinObstaculo >= 1.0f) {
                if (random.nextFloat() < probabilidadAparicion) {
                    crearObstaculo();
                }
                tiempoSinObstaculo = 0;
            }
        }
    }

    private void crearObstaculo() {
        int tipo = random.nextInt(3);
        int intentos = 0;
        int maxIntentos = 50;

        while (intentos < maxIntentos) {
            float x = xCancha + 50 + random.nextFloat() * (CANCHA_ANCHO - 100);
            float y = yCancha + 50 + random.nextFloat() * (CANCHA_ALTO - 100);

            if (esPosicionValida(x, y, tipo)) {
                obstaculoActivo = new Obstaculo(x, y, tipo);
                break;
            }
            intentos++;
        }
    }

    private boolean esPosicionValida(float x, float y, int tipo) {
        float radioSemicirculo = CANCHA_ALTO / 4.5f;
        float centroSemicirculoY = yCancha + CANCHA_ALTO / 2f;

        float radioObstaculo = 30;
        float margenSeguridad = 40;

        if (distancia(x, y, mazo1.getPosicionX() + mazo1.getRadioMazo(),
            mazo1.getPosicionY() + mazo1.getRadioMazo()) < radioObstaculo + mazo1.getRadioMazo() + margenSeguridad) {
            return false;
        }

        if (distancia(x, y, mazo2.getPosicionX() + mazo2.getRadioMazo(),
            mazo2.getPosicionY() + mazo2.getRadioMazo()) < radioObstaculo + mazo2.getRadioMazo() + margenSeguridad) {
            return false;
        }

        if (distancia(x, y, disco.getPosicionX() + disco.getRadioDisco(),
            disco.getPosicionY() + disco.getRadioDisco()) < radioObstaculo + disco.getRadioDisco() + margenSeguridad) {
            return false;
        }

        if (discoSecundario != null) {
            if (distancia(x, y, discoSecundario.getPosicionX() + discoSecundario.getRadioDisco(),
                discoSecundario.getPosicionY() + discoSecundario.getRadioDisco()) < radioObstaculo + discoSecundario.getRadioDisco() + margenSeguridad) {
                return false;
            }
        }

        if (y >= centroSemicirculoY - radioSemicirculo - margenSeguridad &&
            y <= centroSemicirculoY + radioSemicirculo + margenSeguridad) {
            if (x <= xCancha + radioSemicirculo + margenSeguridad ||
                x >= xCancha + CANCHA_ANCHO - radioSemicirculo - margenSeguridad) {
                return false;
            }
        }

        return true;
    }

    private float distancia(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public boolean hayColisionConMazo(Mazo mazo) {
        if (obstaculoActivo == null) return false;
        return obstaculoActivo.colisionaConMazo(mazo);
    }

    public boolean hayColisionConDisco(Disco disco) {
        if (obstaculoActivo == null) return false;
        return obstaculoActivo.colisionaConDisco(disco);
    }

    public void manejarColisionConMazo(Mazo mazo) {
        if (obstaculoActivo != null) {
            obstaculoActivo.manejarColisionConMazo(mazo);
        }
    }

    public void manejarColisionConDisco(Disco disco) {
        if (obstaculoActivo != null) {
            obstaculoActivo.manejarColisionConDisco(disco);
        }
    }

    public void dibujar(ShapeRenderer shapeRenderer) {
        if (obstaculoActivo != null) {
            obstaculoActivo.dibujar(shapeRenderer);
        }
    }

    public void eliminarObstaculo() {
        obstaculoActivo = null;
        tiempoSinObstaculo = 0;
    }

    public void guardarEstado(EstadoObstaculo estadoObstaculo) {
        if (obstaculoActivo != null) {
            estadoObstaculo.setHayObstaculo(true);
            estadoObstaculo.setPosicionX(obstaculoActivo.getPosicionX());
            estadoObstaculo.setPosicionY(obstaculoActivo.getPosicionY());
            estadoObstaculo.setTipo(obstaculoActivo.getTipo());
            estadoObstaculo.setTiempoVida(obstaculoActivo.getTiempoVida());
        } else {
            estadoObstaculo.setHayObstaculo(false);
        }
        estadoObstaculo.setTiempoSinObstaculo(tiempoSinObstaculo);
    }

    public void restaurarEstado(EstadoObstaculo estadoObstaculo) {
        if (estadoObstaculo.isHayObstaculo()) {
            obstaculoActivo = new Obstaculo(estadoObstaculo.getPosicionX(),
                estadoObstaculo.getPosicionY(),
                estadoObstaculo.getTipo());
            obstaculoActivo.setTiempoVida(estadoObstaculo.getTiempoVida());
        } else {
            obstaculoActivo = null;
        }
        tiempoSinObstaculo = estadoObstaculo.getTiempoSinObstaculo();
    }

    public long getUltimaColisionObstaculo() {
        return this.ultimaColisionObstaculo;
    }

    public void actualizarUltimaColision(long tiempo) {
        this.ultimaColisionObstaculo = tiempo;
    }

    public int getTipoObstaculoActivo() {
        if (obstaculoActivo != null) {
            return obstaculoActivo.getTipo();
        }
        return -1;
    }
}
