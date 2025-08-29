package io.github.grootscorer.tejomania.estado;

public class DatosMazo {
    private float mazo1PosX, mazo1PosY, mazo1VelX, mazo1VelY;
    private float mazo2PosX, mazo2PosY, mazo2VelX, mazo2VelY;

    public DatosMazo() {}

    public DatosMazo(float mazo1PosX, float mazo1PosY, float mazo1VelX, float mazo1VelY,
                     float mazo2PosX, float mazo2PosY, float mazo2VelX, float mazo2VelY) {
        this.mazo1PosX = mazo1PosX;
        this.mazo1PosY = mazo1PosY;
        this.mazo1VelX = mazo1VelX;
        this.mazo1VelY = mazo1VelY;
        this.mazo2PosX = mazo2PosX;
        this.mazo2PosY = mazo2PosY;
        this.mazo2VelX = mazo2VelX;
        this.mazo2VelY = mazo2VelY;
    }

    public float getMazo1PosX() {
        return this.mazo1PosX;
    }

    public float getMazo1PosY() {
        return this.mazo1PosY;
    }

    public float getMazo1VelX() {
        return this.mazo1VelX;
    }

    public float getMazo1VelY() {
        return this.mazo1VelY;
    }

    public float getMazo2PosX() {
        return this.mazo2PosX;
    }

    public float getMazo2PosY() {
        return this.mazo2PosY;
    }

    public float getMazo2VelX() {
        return this.mazo2VelX;
    }

    public float getMazo2VelY() {
        return this.mazo2VelY;
    }
}
