package io.github.grootscorer.tejomania.estado;

import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;

public class EstadoFisico {
    public float mazo1PosX, mazo1PosY, mazo1VelX, mazo1VelY;
    public float mazo2PosX, mazo2PosY, mazo2VelX, mazo2VelY;
    public float discoPosX, discoPosY, discoVelX, discoVelY;

    public void guardarEstado(Mazo mazo1, Mazo mazo2, Disco disco) {
        this.mazo1PosX = mazo1.getPosicionX();
        this.mazo1PosY = mazo1.getPosicionY();
        this.mazo1VelX = mazo1.getVelocidadX();
        this.mazo1VelY = mazo1.getVelocidadY();

        this.mazo2PosX = mazo2.getPosicionX();
        this.mazo2PosY = mazo2.getPosicionY();
        this.mazo2VelX = mazo2.getVelocidadX();
        this.mazo2VelY = mazo2.getVelocidadY();

        this.discoPosX = disco.getPosicionX();
        this.discoPosY = disco.getPosicionY();
        this.discoVelX = disco.getVelocidadX();
        this.discoVelY = disco.getVelocidadY();
    }

    public void restaurarEstado(Mazo mazo1, Mazo mazo2, Disco disco) {
        mazo1.setPosicion((int) mazo1PosX, (int) mazo1PosY);
        mazo1.setVelocidadX(mazo1VelX);
        mazo1.setVelocidadY(mazo1VelY);

        mazo2.setPosicion((int) mazo2PosX, (int) mazo2PosY);
        mazo2.setVelocidadX(mazo2VelX);
        mazo2.setVelocidadY(mazo2VelY);

        disco.setPosicion(discoPosX, discoPosY);
        disco.setVelocidadX(discoVelX);
        disco.setVelocidadY(discoVelY);
    }
}
