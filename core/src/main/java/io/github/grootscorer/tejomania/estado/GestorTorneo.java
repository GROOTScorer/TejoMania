package io.github.grootscorer.tejomania.estado;

import io.github.grootscorer.tejomania.enums.Pais;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GestorTorneo {
    private List<Pais> equiposDisponibles;
    private List<Pais> equiposEliminados;
    private Pais equipoJugador;
    private Random random;

    public GestorTorneo(Pais equipoJugador) {
        this.equipoJugador = equipoJugador;
        this.equiposEliminados = new ArrayList<>();
        this.random = new Random();
        inicializarEquiposDisponibles();
    }

    private void inicializarEquiposDisponibles() {
        equiposDisponibles = new ArrayList<>();

        for (Pais pais : Pais.values()) {
            if (!pais.equals(equipoJugador)) {
                equiposDisponibles.add(pais);
            }
        }
    }

    public Pais seleccionarRivalAleatorio() {
        if (equiposDisponibles.isEmpty()) {
            return null;
        }

        int indiceAleatorio = random.nextInt(equiposDisponibles.size());
        return equiposDisponibles.get(indiceAleatorio);
    }

    public void eliminarEquipo(Pais equipo) {
        equiposDisponibles.remove(equipo);
        equiposEliminados.add(equipo);
    }

    public List<Pais> getEquiposDisponibles() {
        return new ArrayList<>(equiposDisponibles);
    }

    public List<Pais> getEquiposEliminados() {
        return new ArrayList<>(equiposEliminados);
    }

    public int getCantidadEquiposDisponibles() {
        return equiposDisponibles.size();
    }

    public boolean hayEquiposDisponibles() {
        return !equiposDisponibles.isEmpty();
    }

    public void reiniciar() {
        equiposEliminados.clear();
        inicializarEquiposDisponibles();
    }
}
