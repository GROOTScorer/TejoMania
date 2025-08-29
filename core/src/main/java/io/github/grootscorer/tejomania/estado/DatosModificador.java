package io.github.grootscorer.tejomania.estado;

public class DatosModificador {
    private String tipo;
    private float posicionX;
    private float posicionY;
    private float tiempoVida;
    private boolean activo;
    private boolean efectoEjecutado;

    public boolean isEfectoEjecutado() {
        return this.efectoEjecutado;
    }

    public void setEfectoEjecutado(boolean efectoEjecutado) {
        this.efectoEjecutado = efectoEjecutado;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public float getTiempoVida() {
        return this.tiempoVida;
    }

    public void setTiempoVida(float tiempoVida) {
        this.tiempoVida = tiempoVida;
    }

    public float getPosicionY() {
        return this.posicionY;
    }

    public void setPosicionY(float posicionY) {
        this.posicionY = posicionY;
    }

    public float getPosicionX() {
        return this.posicionX;
    }

    public void setPosicionX(float posicionX) {
        this.posicionX = posicionX;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
