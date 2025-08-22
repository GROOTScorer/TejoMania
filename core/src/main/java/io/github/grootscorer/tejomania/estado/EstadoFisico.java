package io.github.grootscorer.tejomania.estado;

import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.entidades.modificadores.Modificador;
import io.github.grootscorer.tejomania.entidades.modificadores.DiscoDoble;

import java.util.ArrayList;
import java.util.List;

public class EstadoFisico {
    public float mazo1PosX, mazo1PosY, mazo1VelX, mazo1VelY;
    public float mazo2PosX, mazo2PosY, mazo2VelX, mazo2VelY;
    public float discoPosX, discoPosY, discoVelX, discoVelY;
    public boolean discoHaAnotadoGol = false;

    public boolean hayDiscoSecundario = false;
    public float discoSecundarioPosX, discoSecundarioPosY, discoSecundarioVelX, discoSecundarioVelY;
    public boolean discoSecundarioHaAnotadoGol = false;

    public List<DatosModificador> modificadoresGuardados;
    public boolean discoDobleActivo = false;
    public boolean hayModificadorEnPantalla = false;
    public float tiempoSinGenerar = 0;

    public EstadoFisico() {
        this.modificadoresGuardados = new ArrayList<>();
    }

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

    public void guardarEstadoCompleto(Mazo mazo1, Mazo mazo2, Disco disco, Disco discoSecundario,
                                      List<Modificador> modificadores, boolean discoDobleActivo,
                                      boolean hayModificadorEnPantalla, float tiempoSinGenerar) {
        guardarEstado(mazo1, mazo2, disco);

        this.discoHaAnotadoGol = disco.haAnotadoGol();

        if (discoSecundario != null) {
            this.hayDiscoSecundario = true;
            this.discoSecundarioPosX = discoSecundario.getPosicionX();
            this.discoSecundarioPosY = discoSecundario.getPosicionY();
            this.discoSecundarioVelX = discoSecundario.getVelocidadX();
            this.discoSecundarioVelY = discoSecundario.getVelocidadY();
            this.discoSecundarioHaAnotadoGol = discoSecundario.haAnotadoGol();
        } else {
            this.hayDiscoSecundario = false;
            this.discoSecundarioHaAnotadoGol = false;
        }

        this.discoDobleActivo = discoDobleActivo;
        this.hayModificadorEnPantalla = hayModificadorEnPantalla;
        this.tiempoSinGenerar = tiempoSinGenerar;

        modificadoresGuardados.clear();
        for (Modificador modificador : modificadores) {
            DatosModificador datos = new DatosModificador();
            datos.posicionX = modificador.getPosicionX();
            datos.posicionY = modificador.getPosicionY();
            datos.tiempoVida = modificador.getTiempoVida();
            datos.activo = modificador.isActivo();
            datos.efectoEjecutado = (modificador instanceof DiscoDoble) ?
                ((DiscoDoble) modificador).isEfectoEjecutado() : false;
            datos.tipo = modificador.getClass().getSimpleName();
            modificadoresGuardados.add(datos);
        }
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

        if (discoHaAnotadoGol) {
            disco.marcarGolAnotado();
        }
    }

    public Disco restaurarDiscoSecundario() {
        if (hayDiscoSecundario) {
            Disco discoSecundario = new Disco();
            discoSecundario.setPosicion(discoSecundarioPosX, discoSecundarioPosY);
            discoSecundario.setVelocidadX(discoSecundarioVelX);
            discoSecundario.setVelocidadY(discoSecundarioVelY);

            if (discoSecundarioHaAnotadoGol) {
                discoSecundario.marcarGolAnotado();
            }

            return discoSecundario;
        }
        return null;
    }

    public boolean tieneDiscoSecundario() {
        return this.hayDiscoSecundario;
    }

    public static class DatosModificador {
        public String tipo;
        public float posicionX;
        public float posicionY;
        public float tiempoVida;
        public boolean activo;
        public boolean efectoEjecutado;
    }
}
