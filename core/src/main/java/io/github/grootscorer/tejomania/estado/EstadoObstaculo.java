package io.github.grootscorer.tejomania.estado;

public class EstadoObstaculo {
    private boolean hayObstaculo = false;
    private float posicionX;
    private float posicionY;
    private int tipo;
    private float tiempoVida;
    private float tiempoSinObstaculo;

    public boolean isHayObstaculo() {
        return hayObstaculo;
    }

    public void setHayObstaculo(boolean hayObstaculo) {
        this.hayObstaculo = hayObstaculo;
    }

    public float getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(float posicionX) {
        this.posicionX = posicionX;
    }

    public float getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(float posicionY) {
        this.posicionY = posicionY;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public float getTiempoVida() {
        return tiempoVida;
    }

    public void setTiempoVida(float tiempoVida) {
        this.tiempoVida = tiempoVida;
    }

    public float getTiempoSinObstaculo() {
        return tiempoSinObstaculo;
    }

    public void setTiempoSinObstaculo(float tiempoSinObstaculo) {
        this.tiempoSinObstaculo = tiempoSinObstaculo;
    }
}
