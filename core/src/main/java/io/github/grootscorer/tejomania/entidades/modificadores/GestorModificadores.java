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
    private final float PROBABILIDAD_POR_SEGUNDO = 0.3f;
    private PantallaJuego pantallaJuego;
    private boolean discoDobleActivo = false;
    private boolean congelarRivalActivo = false;
    private boolean controlesInvertidosActivo = false;
    private boolean hayModificadorEnPantalla = false;

    private Mazo mazo1, mazo2;
    private Disco disco;
    private float xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO;
    private EstadoFisico estadoFisico;

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
        this.estadoFisico = new EstadoFisico();
    }

    public void actualizar(float delta, float velocidadDisco) {
        tiempoSinGenerar += delta;

        if (velocidadDisco > 200 && tiempoSinGenerar >= 1.0f && !hayModificadorEnPantalla) {
            tiempoSinGenerar = 0;

            if (random.nextFloat() < PROBABILIDAD_POR_SEGUNDO) {
                generarModificadorAleatorio();
            }
        }

        Iterator<Modificador> iterator = modificadores.iterator();
        while (iterator.hasNext()) {
            Modificador modificador = iterator.next();
            modificador.actualizar(delta);

            if (modificador instanceof DiscoDoble && modificador.isActivo() && !discoDobleActivo) {
                discoDobleActivo = true;
            }

            if (modificador instanceof CongelarRival && modificador.isActivo() && !congelarRivalActivo) {
                congelarRivalActivo = true;
            }

            if (modificador instanceof ControlesInvertidos && modificador.isActivo() && !controlesInvertidosActivo) {
                controlesInvertidosActivo = true;
            }

            if (modificador.debeDesaparecer()) {
                if (modificador instanceof DiscoDoble && discoDobleActivo) {
                    discoDobleActivo = false;
                }
                if (modificador instanceof CongelarRival && congelarRivalActivo) {
                    congelarRivalActivo = false;
                }
                if (modificador instanceof ControlesInvertidos && controlesInvertidosActivo) {
                    controlesInvertidosActivo = false;
                }
                hayModificadorEnPantalla = false;

                modificador.dispose();
                iterator.remove();
            }
        }

        hayModificadorEnPantalla = !modificadores.isEmpty();
    }

    private void generarModificadorAleatorio() {
        boolean tieneDiscoDoble = false;
        boolean tieneCongelarRival = false;
        boolean tieneControlesInvertidos = false;

        for (Modificador mod : modificadores) {
            if (mod instanceof DiscoDoble) tieneDiscoDoble = true;
            if (mod instanceof CongelarRival) tieneCongelarRival = true;
            if (mod instanceof ControlesInvertidos) tieneControlesInvertidos = true;
        }

        List<Integer> tiposDisponibles = new ArrayList<>();
        if (!tieneDiscoDoble) tiposDisponibles.add(0);
        if (!tieneCongelarRival) tiposDisponibles.add(1);
        if (!tieneControlesInvertidos) tiposDisponibles.add(2);

        if (tiposDisponibles.isEmpty()) return;

        int tipoSeleccionado = tiposDisponibles.get(random.nextInt(tiposDisponibles.size()));

        if (tipoSeleccionado == 0) {
            generarDiscoDoble();
        } else if (tipoSeleccionado == 1) {
            generarCongelarRival();
        } else {
            generarControlesInvertidos();
        }

        hayModificadorEnPantalla = true;
    }

    private void generarDiscoDoble() {
        DiscoDoble discoDoble = new DiscoDoble(pantallaJuego);

        discoDoble.inicializar(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO,
            mazo1.getPosicionX(), mazo1.getPosicionY(),
            mazo2.getPosicionX(), mazo2.getPosicionY(),
            disco.getPosicionX(), disco.getPosicionY(),
            mazo1.getRadioMazo(), disco.getRadioDisco());

        discoDoble.setDisco(disco);
        modificadores.add(discoDoble);
    }

    private void generarCongelarRival() {
        CongelarRival congelarRival = new CongelarRival(pantallaJuego, estadoFisico);

        congelarRival.inicializar(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO,
            mazo1.getPosicionX(), mazo1.getPosicionY(),
            mazo2.getPosicionX(), mazo2.getPosicionY(),
            disco.getPosicionX(), disco.getPosicionY(),
            mazo1.getRadioMazo(), disco.getRadioDisco());

        congelarRival.setDisco(disco);
        modificadores.add(congelarRival);
    }

    private void generarControlesInvertidos() {
        ControlesInvertidos controlesInvertidos = new ControlesInvertidos(pantallaJuego);

        controlesInvertidos.inicializar(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO,
            mazo1.getPosicionX(), mazo1.getPosicionY(),
            mazo2.getPosicionX(), mazo2.getPosicionY(),
            disco.getPosicionX(), disco.getPosicionY(),
            mazo1.getRadioMazo(), disco.getRadioDisco());

        controlesInvertidos.setDisco(disco);
        modificadores.add(controlesInvertidos);
    }

    public CongelarRival getCongelarRivalActivo() {
        for (Modificador mod : modificadores) {
            if (mod instanceof CongelarRival && ((CongelarRival) mod).isEfectoEjecutado()) {
                return (CongelarRival) mod;
            }
        }
        return null;
    }

    public ControlesInvertidos getControlesInvertidosActivo() {
        for (Modificador mod : modificadores) {
            if (mod instanceof ControlesInvertidos && ((ControlesInvertidos) mod).isEfectoEjecutado()) {
                return (ControlesInvertidos) mod;
            }
        }
        return null;
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
            } else if ("CongelarRival".equals(datos.getTipo())) {
                CongelarRival congelarRival = new CongelarRival(pantallaJuego, estadoFisico);
                congelarRival.restaurarDesdeEstadoCompleto(datos.getPosicionX(), datos.getPosicionY(),
                    datos.getTiempoVida(), datos.isActivo(), datos.isEfectoEjecutado());
                congelarRival.setDisco(disco);
                modificadores.add(congelarRival);
            } else if ("ControlesInvertidos".equals(datos.getTipo())) {
                ControlesInvertidos controlesInvertidos = new ControlesInvertidos(pantallaJuego);
                controlesInvertidos.restaurarDesdeEstadoCompleto(datos.getPosicionX(), datos.getPosicionY(),
                    datos.getTiempoVida(), datos.isActivo(), datos.isEfectoEjecutado());
                controlesInvertidos.setDisco(disco);
                modificadores.add(controlesInvertidos);
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
            if (modificador instanceof CongelarRival && congelarRivalActivo) {
                congelarRivalActivo = false;
            }
            if (modificador instanceof ControlesInvertidos && controlesInvertidosActivo) {
                controlesInvertidosActivo = false;
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

    public boolean isCongelarRivalActivo() {
        return this.congelarRivalActivo;
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

    public void desactivarCongelarRival() {
        congelarRivalActivo = false;

        Iterator<Modificador> iterator = modificadores.iterator();
        while (iterator.hasNext()) {
            Modificador modificador = iterator.next();
            if (modificador instanceof CongelarRival) {
                modificador.dispose();
                iterator.remove();
                break;
            }
        }

        hayModificadorEnPantalla = !modificadores.isEmpty();
    }

    public void desactivarControlesInvertidos() {
        controlesInvertidosActivo = false;

        Iterator<Modificador> iterator = modificadores.iterator();
        while (iterator.hasNext()) {
            Modificador modificador = iterator.next();
            if (modificador instanceof ControlesInvertidos) {
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
