package io.github.grootscorer.tejomania.estado;

import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.entidades.modificadores.ControlesInvertidos;
import io.github.grootscorer.tejomania.entidades.modificadores.Modificador;
import io.github.grootscorer.tejomania.entidades.modificadores.DiscoDoble;
import io.github.grootscorer.tejomania.entidades.modificadores.CongelarRival;
import io.github.grootscorer.tejomania.entidades.obstaculos.GestorObstaculos;
import io.github.grootscorer.tejomania.hud.BarraEspecial;

import java.util.ArrayList;
import java.util.List;

public class EstadoFisico {
    private DatosMazo datosMazo;
    private DatosDisco datosDisco;
    private List<DatosModificador> datosModificadores;

    private boolean discoDobleActivo = false;
    private boolean hayModificadorEnPantalla = false;
    private float tiempoSinGenerar = 0;

    private int mazoEnPosesionId = -1;
    private int mazoEnPosesionIdRespaldo = -1;

    private EstadoObstaculo estadoObstaculo = new EstadoObstaculo();

    private int cantidadLlenadaBarra1 = 0;
    private int cantidadLlenadaBarra2 = 0;

    public EstadoFisico() {
        this.datosMazo = new DatosMazo();
        this.datosDisco = new DatosDisco();
        this.datosModificadores = new ArrayList<>();
    }

    public void guardarEstadoCompleto(Mazo mazo1, Mazo mazo2, Disco disco, Disco discoSecundario, Mazo mazoEnPosesion,
                                      List<Modificador> modificadores, boolean discoDobleActivo,
                                      boolean hayModificadorEnPantalla, float tiempoSinGenerar,
                                      GestorObstaculos gestorObstaculos,
                                      int cantidadLlenadaBarra1, int cantidadLlenadaBarra2) {

        this.datosMazo = new DatosMazo(
            mazo1.getPosicionX(), mazo1.getPosicionY(), mazo1.getVelocidadX(), mazo1.getVelocidadY(),
            mazo2.getPosicionX(), mazo2.getPosicionY(), mazo2.getVelocidadX(), mazo2.getVelocidadY(), mazoEnPosesion
        );

        this.mazoEnPosesionId = -1;
        if (mazoEnPosesion != null) {
            if (mazoEnPosesion == mazo1) {
                this.mazoEnPosesionId = 1;
            } else if (mazoEnPosesion == mazo2) {
                this.mazoEnPosesionId = 2;
            }
        }

        if (this.mazoEnPosesionId == -1 && this.mazoEnPosesionIdRespaldo != -1) {
            this.mazoEnPosesionId = this.mazoEnPosesionIdRespaldo;
        } else if (this.mazoEnPosesionId != -1) {
            this.mazoEnPosesionIdRespaldo = this.mazoEnPosesionId;
        }

        if (discoSecundario != null) {
            this.datosDisco = new DatosDisco(
                disco.getPosicionX(), disco.getPosicionY(), disco.getVelocidadX(), disco.getVelocidadY(), disco.haAnotadoGol(),
                true, discoSecundario.getPosicionX(), discoSecundario.getPosicionY(),
                discoSecundario.getVelocidadX(), discoSecundario.getVelocidadY(), discoSecundario.haAnotadoGol()
            );
        } else {
            this.datosDisco = new DatosDisco(
                disco.getPosicionX(), disco.getPosicionY(), disco.getVelocidadX(), disco.getVelocidadY(), disco.haAnotadoGol(),
                false, 0, 0, 0, 0, false
            );
        }

        this.discoDobleActivo = discoDobleActivo;
        this.hayModificadorEnPantalla = hayModificadorEnPantalla;
        this.tiempoSinGenerar = tiempoSinGenerar;

        this.cantidadLlenadaBarra1 = cantidadLlenadaBarra1;
        this.cantidadLlenadaBarra2 = cantidadLlenadaBarra2;

        if (gestorObstaculos != null) {
            gestorObstaculos.guardarEstado(this.estadoObstaculo);
        }

        datosModificadores.clear();
        for (Modificador modificador : modificadores) {
            DatosModificador datosModificador = new DatosModificador();
            datosModificador.setPosicionX(modificador.getPosicionX());
            datosModificador.setPosicionY(modificador.getPosicionY());

            datosModificador.setActivo(modificador.isActivo());

            if (modificador instanceof DiscoDoble) {
                DiscoDoble discoDoble = (DiscoDoble) modificador;
                datosModificador.setEfectoEjecutado(discoDoble.isEfectoEjecutado());
                datosModificador.setTiempoVida(modificador.getTiempoVida());
            } else if (modificador instanceof CongelarRival) {
                CongelarRival congelarRival = (CongelarRival) modificador;
                datosModificador.setEfectoEjecutado(congelarRival.isEfectoEjecutado());
                datosModificador.setTiempoVida(modificador.getTiempoVida());
            } else if (modificador instanceof ControlesInvertidos) {
                ControlesInvertidos controlesInvertidos = (ControlesInvertidos) modificador;
                datosModificador.setEfectoEjecutado(controlesInvertidos.isEfectoEjecutado());
                if (controlesInvertidos.isEfectoEjecutado()) {
                    datosModificador.setTiempoVida(controlesInvertidos.getTiempoEfectoActivo());
                } else {
                    datosModificador.setTiempoVida(modificador.getTiempoVida());
                }
            }

            datosModificador.setTipo(modificador.getClass().getSimpleName());
            datosModificadores.add(datosModificador);
        }
    }

    public void restaurarEstado(Mazo mazo1, Mazo mazo2, Disco disco) {
        mazo1.setPosicion((int) datosMazo.getMazo1PosX(), (int) datosMazo.getMazo1PosY());
        mazo1.setVelocidadX(datosMazo.getMazo1VelX());
        mazo1.setVelocidadY(datosMazo.getMazo1VelY());

        mazo2.setPosicion((int) datosMazo.getMazo2PosX(), (int) datosMazo.getMazo2PosY());
        mazo2.setVelocidadX(datosMazo.getMazo2VelX());
        mazo2.setVelocidadY(datosMazo.getMazo2VelY());

        disco.setPosicion(datosDisco.getDiscoPosX(), datosDisco.getDiscoPosY());
        disco.setVelocidadX(datosDisco.getDiscoVelX());
        disco.setVelocidadY(datosDisco.getDiscoVelY());

        if (datosDisco.isDiscoHaAnotadoGol()) {
            disco.marcarGolAnotado();
        }
    }

    public Disco restaurarDiscoSecundario() {
        if (datosDisco.isHayDiscoSecundario()) {
            Disco discoSecundario = new Disco();
            discoSecundario.setPosicion(datosDisco.getDiscoSecundarioPosX(), datosDisco.getDiscoSecundarioPosY());
            discoSecundario.setVelocidadX(datosDisco.getDiscoSecundarioVelX());
            discoSecundario.setVelocidadY(datosDisco.getDiscoSecundarioVelY());

            if (datosDisco.isDiscoSecundarioHaAnotadoGol()) {
                discoSecundario.marcarGolAnotado();
            }

            return discoSecundario;
        }
        return null;
    }

    public boolean tieneDiscoSecundario() {
        return datosDisco.isHayDiscoSecundario();
    }

    public boolean isDiscoDobleActivo() {
        return this.discoDobleActivo;
    }

    public boolean isModificadorEnPantalla() {
        return this.hayModificadorEnPantalla;
    }

    public float getTiempoSinGenerar() {
        return this.tiempoSinGenerar;
    }

    public List<DatosModificador> getModificadoresGuardados() {
        return this.datosModificadores;
    }

    public int getMazoEnPosesionId() {
        return this.mazoEnPosesionId;
    }

    public EstadoObstaculo getEstadoObstaculo() {
        return this.estadoObstaculo;
    }

    public int getCantidadLlenadaBarra1() {
        return this.cantidadLlenadaBarra1;
    }

    public int getCantidadLlenadaBarra2() {
        return this.cantidadLlenadaBarra2;
    }

    public DatosMazo getDatosMazo() {
        return this.datosMazo;
    }
}
