package io.github.grootscorer.tejomania.estado;

import io.github.grootscorer.tejomania.enums.Pais;
import io.github.grootscorer.tejomania.enums.TipoCompetencia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GestorLiga {
    private Pais equipoJugador;
    private List<Pais> equiposLiga;
    private Map<Pais, Integer> tablaPosiciones;
    private List<Partido> fixture;
    private int fechaActual;
    private Random random;

    public static class Partido {
        private Pais local;
        private Pais visitante;
        private int golesLocal;
        private int golesVisitante;
        private boolean jugado;
        private boolean esPartidoJugador;

        public Partido(Pais local, Pais visitante, boolean esPartidoJugador) {
            this.local = local;
            this.visitante = visitante;
            this.esPartidoJugador = esPartidoJugador;
            this.jugado = false;
            this.golesLocal = 0;
            this.golesVisitante = 0;
        }

        public Pais getLocal() {
            return local;
        }

        public Pais getVisitante() {
            return visitante;
        }

        public int getGolesLocal() {
            return golesLocal;
        }

        public int getGolesVisitante() {
            return golesVisitante;
        }

        public boolean isJugado() {
            return jugado;
        }

        public boolean isEsPartidoJugador() {
            return esPartidoJugador;
        }

        public void setResultado(int golesLocal, int golesVisitante) {
            this.golesLocal = golesLocal;
            this.golesVisitante = golesVisitante;
            this.jugado = true;
        }
    }

    public GestorLiga(Pais equipoJugador) {
        this.equipoJugador = equipoJugador;
        this.random = new Random();
        this.fechaActual = 0;
        this.equiposLiga = new ArrayList<>();
        this.tablaPosiciones = new HashMap<>();
        this.fixture = new ArrayList<>();

        inicializarLiga();
        generarFixture();
    }

    private void inicializarLiga() {
        for (Pais pais : Pais.values()) {
            if (pais.getTipoTorneo() == TipoCompetencia.LIGA) {
                equiposLiga.add(pais);
                tablaPosiciones.put(pais, 0);
            }
        }
    }

    private void generarFixture() {
        int numEquipos = equiposLiga.size();

        List<Pais> equiposParaFixture = new ArrayList<>(equiposLiga);
        boolean hayBye = (numEquipos % 2 != 0);

        if (hayBye) {
            equiposParaFixture.add(null);
        }

        int totalFechas = equiposParaFixture.size() - 1;
        int partidosPorFecha = equiposParaFixture.size() / 2;

        for (int fecha = 0; fecha < totalFechas; fecha++) {
            for (int partido = 0; partido < partidosPorFecha; partido++) {
                int local = (fecha + partido) % (equiposParaFixture.size() - 1);
                int visitante = (equiposParaFixture.size() - 1 - partido + fecha) % (equiposParaFixture.size() - 1);

                if (partido == 0) {
                    visitante = equiposParaFixture.size() - 1;
                }

                Pais equipoLocal = equiposParaFixture.get(local);
                Pais equipoVisitante = equiposParaFixture.get(visitante);

                if (equipoLocal != null && equipoVisitante != null) {
                    boolean esPartidoJugador = (equipoLocal == equipoJugador || equipoVisitante == equipoJugador);
                    fixture.add(new Partido(equipoLocal, equipoVisitante, esPartidoJugador));
                }
            }
        }
    }

    public List<Partido> getPartidosFecha(int numeroFecha) {
        List<Partido> partidosFecha = new ArrayList<>();
        int partidosPorFecha = (equiposLiga.size() % 2 == 0) ? equiposLiga.size() / 2 : (equiposLiga.size() + 1) / 2;

        int inicio = numeroFecha * partidosPorFecha;
        int fin = Math.min(inicio + partidosPorFecha, fixture.size());

        for (int i = inicio; i < fin; i++) {
            partidosFecha.add(fixture.get(i));
        }

        return partidosFecha;
    }

    public Partido getPartidoJugadorFecha(int numeroFecha) {
        List<Partido> partidosFecha = getPartidosFecha(numeroFecha);

        for (Partido partido : partidosFecha) {
            if (partido.isEsPartidoJugador()) {
                return partido;
            }
        }

        return null;
    }

    public Pais getRivalJugadorFecha(int numeroFecha) {
        Partido partidoJugador = getPartidoJugadorFecha(numeroFecha);

        if (partidoJugador == null) {
            return null;
        }

        if (partidoJugador.getLocal() == equipoJugador) {
            return partidoJugador.getVisitante();
        } else {
            return partidoJugador.getLocal();
        }
    }

    public void simularPartidosIA(int numeroFecha) {
        List<Partido> partidosFecha = getPartidosFecha(numeroFecha);

        for (Partido partido : partidosFecha) {
            if (!partido.isEsPartidoJugador() && !partido.isJugado()) {
                int golesLocal = random.nextInt(4);
                int golesVisitante = random.nextInt(4);

                partido.setResultado(golesLocal, golesVisitante);
                actualizarTabla(partido);
            }
        }
    }

    public void registrarResultadoJugador(int numeroFecha, int golesFavor, int golesContra) {
        Partido partidoJugador = getPartidoJugadorFecha(numeroFecha);

        if (partidoJugador != null && !partidoJugador.isJugado()) {
            if (partidoJugador.getLocal() == equipoJugador) {
                partidoJugador.setResultado(golesFavor, golesContra);
            } else {
                partidoJugador.setResultado(golesContra, golesFavor);
            }

            actualizarTabla(partidoJugador);
        }
    }

    private void actualizarTabla(Partido partido) {
        int puntosLocal = 0;
        int puntosVisitante = 0;

        if (partido.getGolesLocal() > partido.getGolesVisitante()) {
            puntosLocal = 3;
        } else if (partido.getGolesLocal() < partido.getGolesVisitante()) {
            puntosVisitante = 3;
        } else {
            puntosLocal = 1;
            puntosVisitante = 1;
        }

        tablaPosiciones.put(partido.getLocal(), tablaPosiciones.get(partido.getLocal()) + puntosLocal);
        tablaPosiciones.put(partido.getVisitante(), tablaPosiciones.get(partido.getVisitante()) + puntosVisitante);
    }

    public List<Map.Entry<Pais, Integer>> getTablaOrdenada() {
        List<Map.Entry<Pais, Integer>> tablaOrdenada = new ArrayList<>(tablaPosiciones.entrySet());

        tablaOrdenada.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return tablaOrdenada;
    }

    public void avanzarFecha() {
        if (fechaActual < 9) {
            fechaActual++;
        }
    }

    public int getPosicionEquipo(Pais equipo) {
        List<Map.Entry<Pais, Integer>> tablaOrdenada = getTablaOrdenada();

        for (int i = 0; i < tablaOrdenada.size(); i++) {
            if (tablaOrdenada.get(i).getKey() == equipo) {
                return i + 1;
            }
        }

        return -1;
    }
}
