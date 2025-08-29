package io.github.grootscorer.tejomania.entidades.modificadores;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.estado.DatosModificador;
import io.github.grootscorer.tejomania.estado.EstadoFisico;
import io.github.grootscorer.tejomania.pantallas.PantallaJuego;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GestorModificadores {
    private List<Modificador> modificadores;
    private Random random;
    private float tiempoSinGenerar = 0;
    private final float PROBABILIDAD_POR_SEGUNDO = 0.1f;
    private PantallaJuego pantallaJuego;
    private boolean discoDobleActivo = false;
    private boolean hayModificadorEnPantalla = false;

    private Mazo mazo1, mazo2;
    private Disco disco;
    private float xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO;

    public GestorModificadores(PantallaJuego pantallaJuego, Mazo mazo1, Mazo mazo2, Disco disco,
                               float xCancha, float yCancha, float CANCHA_ANCHO, float CANCHA_ALTO) {
        this.modificadores = new ArrayList<>();
        this.random = new Random();
        this.pantallaJuego = pantallaJuego;
        this.mazo1 = mazo1;
        this.mazo2 = mazo2;
        this.disco = disco;
        this.xCancha = xCancha;
        this.yCancha = yCancha;
        this.CANCHA_ANCHO = CANCHA_ANCHO;
        this.CANCHA_ALTO = CANCHA_ALTO;
    }

    public void actualizar(float delta, float velocidadDisco) {
        tiempoSinGenerar += delta;

        if (velocidadDisco > 200 && tiempoSinGenerar >= 1.0f &&
            !discoDobleActivo && !hayModificadorEnPantalla) {
            tiempoSinGenerar = 0;

            if (random.nextFloat() < PROBABILIDAD_POR_SEGUNDO) {
                generarModificador();
            }
        }

        Iterator<Modificador> iterator = modificadores.iterator();
        while (iterator.hasNext()) {
            Modificador modificador = iterator.next();
            modificador.actualizar(delta);

            if (modificador instanceof DiscoDoble && modificador.isActivo() && !discoDobleActivo) {
                discoDobleActivo = true;
            }

            if (modificador.debeDesaparecer()) {
                if (modificador instanceof DiscoDoble && discoDobleActivo) {
                    discoDobleActivo = false;
                }
                hayModificadorEnPantalla = false;

                modificador.dispose();
                iterator.remove();
            }
        }

        hayModificadorEnPantalla = !modificadores.isEmpty();
    }

    private void generarModificador() {
        DiscoDoble discoDoble = new DiscoDoble(pantallaJuego);

        discoDoble.inicializar(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO,
            mazo1.getPosicionX(), mazo1.getPosicionY(),
            mazo2.getPosicionX(), mazo2.getPosicionY(),
            disco.getPosicionX(), disco.getPosicionY(),
            mazo1.getRadioMazo(), disco.getRadioDisco());

        discoDoble.setDisco(disco);
        modificadores.add(discoDoble);

        hayModificadorEnPantalla = true;
    }

    public void restaurarDesdeEstado(EstadoFisico estadoFisico) {
        this.discoDobleActivo = estadoFisico.isDiscoDobleActivo();
        this.hayModificadorEnPantalla = estadoFisico.isModificadorEnPantalla();
        this.tiempoSinGenerar = estadoFisico.getTiempoSinGenerar();

        modificadores.clear();

        for (DatosModificador datos : estadoFisico.getModificadoresGuardados()) {
            if ("DiscoDoble".equals(datos.getTipo())) {
                DiscoDoble discoDoble = new DiscoDoble(pantallaJuego);
                discoDoble.restaurarDesdeEstadoCompleto(datos.getPosicionX(), datos.getPosicionY(),
                    datos.getTiempoVida(), datos.isActivo(), datos.isEfectoEjecutado());
                discoDoble.setDisco(disco);
                modificadores.add(discoDoble);
            }
        }
    }

    public void dibujar(SpriteBatch batch) {
        for (Modificador modificador : modificadores) {
            modificador.dibujar(batch);
        }
    }

    public void limpiarTodos() {
        for (Modificador modificador : modificadores) {
            if (modificador instanceof DiscoDoble && discoDobleActivo) {
                discoDobleActivo = false;
            }
            modificador.dispose();
        }
        modificadores.clear();
        hayModificadorEnPantalla = false;
    }

    public List<Modificador> getModificadores() {
        return this.modificadores;
    }

    public void reiniciarModificadores() {
        limpiarTodos();
        tiempoSinGenerar = 0;
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

    public void desactivarDiscoDoble() {
        discoDobleActivo = false;

        Iterator<Modificador> iterator = modificadores.iterator();
        while (iterator.hasNext()) {
            Modificador modificador = iterator.next();
            if (modificador instanceof DiscoDoble) {
                modificador.dispose();
                iterator.remove();
                break;
            }
        }

        hayModificadorEnPantalla = !modificadores.isEmpty();
    }

    public void dispose() {
        limpiarTodos();
    }
}
