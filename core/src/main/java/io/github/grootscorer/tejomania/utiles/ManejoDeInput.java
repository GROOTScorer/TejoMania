package io.github.grootscorer.tejomania.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.entidades.Cpu;
import io.github.grootscorer.tejomania.entidades.modificadores.CongelarRival;
import io.github.grootscorer.tejomania.entidades.modificadores.ControlesInvertidos;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.DatosMazo;

public class ManejoDeInput extends InputAdapter {
    private final Mazo mazo1;
    private final Mazo mazo2;
    private final TipoJuegoLibre tipoJuegoLibre;
    private CongelarRival congelarRival;
    private ControlesInvertidos controlesInvertidos;
    private DatosMazo datosMazo;

    private final float xCancha, yCancha;
    private final int canchaAncho, canchaAlto;
    private final int velocidad = 8;

    private int mazoCongeladoId = -1;

    private boolean solicitudCpuTiroEspecial = false;
    private int mazoIdTiroEspecial = -1;

    private Cpu cpu;

    private static class InputSimulado {
        boolean arriba = false, abajo = false, izquierda = false, derecha = false, golpe = false;
        void clear(){
            arriba = abajo = izquierda = derecha = golpe = false;
        }

        @Override
        public String toString() {
            return "ARRIBA:" + arriba + " ABAJO:" + abajo + " IZQUIERDA:" + izquierda + " DERECHA:" + derecha + " GOLPE:" + golpe;
        }
    }
    private final InputSimulado inputSimuladoMazo1 = new InputSimulado();
    private final InputSimulado inputSimuladoMazo2 = new InputSimulado();

    public void setInputSimuladoParaMazo(int mazoId, boolean arriba, boolean abajo, boolean izquierda, boolean derecha, boolean golpe){
        InputSimulado s = (mazoId == 1) ? inputSimuladoMazo1 : inputSimuladoMazo2;
        s.arriba = arriba;
        s.abajo = abajo;
        s.izquierda = izquierda;
        s.derecha = derecha;
        s.golpe = golpe;
    }

    public void clearInputSimuladoParaMazo(int mazoId){
        InputSimulado s = (mazoId == 1) ? inputSimuladoMazo1 : inputSimuladoMazo2;
        s.clear();
    }

    public ManejoDeInput(Mazo mazo1, Mazo mazo2, TipoJuegoLibre tipoJuegoLibre,
                         float xCancha, float yCancha, int canchaAncho, int canchaAlto) {
        this.mazo1 = mazo1;
        this.mazo2 = mazo2;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.xCancha = xCancha;
        this.yCancha = yCancha;
        this.canchaAncho = canchaAncho;
        this.canchaAlto = canchaAlto;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public void setCongelarRival(CongelarRival congelarRival) {
        this.congelarRival = congelarRival;
    }

    public void setControlesInvertidos(ControlesInvertidos controlesInvertidos) {
        this.controlesInvertidos = controlesInvertidos;
    }

    public void setDatosMazo(DatosMazo datosMazo) {
        this.datosMazo = datosMazo;

        if (datosMazo != null && datosMazo.getMazoEnPosesion() != null) {
            if (datosMazo.getMazoEnPosesion() == mazo1) {
                mazoCongeladoId = 2;
            } else if (datosMazo.getMazoEnPosesion() == mazo2) {
                mazoCongeladoId = 1;
            }
        } else {
            mazoCongeladoId = -1;
        }
    }

    public void configurarCongelacionDesdeEstado(CongelarRival congelarRivalActivo, int mazoEnPosesionId) {
        if (congelarRivalActivo != null && mazoEnPosesionId != -1) {
            this.congelarRival = congelarRivalActivo;

            if (mazoEnPosesionId == 1) {
                mazoCongeladoId = 2;
            } else if (mazoEnPosesionId == 2) {
                mazoCongeladoId = 1;
            }

            Mazo mazoEnPosesion = (mazoEnPosesionId == 1) ? mazo1 : mazo2;
            this.datosMazo = new DatosMazo(
                mazo1.getPosicionX(), mazo1.getPosicionY(), mazo1.getVelocidadX(), mazo1.getVelocidadY(),
                mazo2.getPosicionX(), mazo2.getPosicionY(), mazo2.getVelocidadX(), mazo2.getVelocidadY(),
                mazoEnPosesion
            );
        }
    }

    public void configurarControlesInvertidosDesdeEstado(ControlesInvertidos controlesInvertidosActivo) {
        if (controlesInvertidosActivo != null) {
            this.controlesInvertidos = controlesInvertidosActivo;
        }
    }

    public void actualizarMovimiento() {
        boolean mazo1Congelado = false;
        boolean mazo2Congelado = false;
        boolean controlesInvertidosActivo = false;

        if (congelarRival != null && congelarRival.isEfectoEjecutado()) {
            if (mazoCongeladoId == 1) {
                mazo1Congelado = true;
            } else if (mazoCongeladoId == 2) {
                mazo2Congelado = true;
            }
        } else {
            if (mazoCongeladoId != -1) {
                mazoCongeladoId = -1;
            }
        }

        if (controlesInvertidos != null && controlesInvertidos.isEfectoEjecutado()) {
            controlesInvertidosActivo = true;
        }

        if (mazo1 != null && !mazo1Congelado) {
            if (controlesInvertidosActivo) {
                moverMazoFlechas(mazo1, xCancha, xCancha + canchaAncho / 2f);
            } else {
                moverMazoWASD(mazo1, xCancha, xCancha + canchaAncho / 2f);
            }
        }

        if (mazo2 != null && !mazo2Congelado) {
            if (tipoJuegoLibre == TipoJuegoLibre.DOS_JUGADORES) {
                if (controlesInvertidosActivo) {
                    moverMazoWASD(mazo2, xCancha + canchaAncho / 2f, xCancha + canchaAncho);
                } else {
                    moverMazoFlechas(mazo2, xCancha + canchaAncho / 2f, xCancha + canchaAncho);
                }
            } else {
                moverMazoSimulado(mazo2, xCancha + canchaAncho / 2f, xCancha + canchaAncho);
            }
        }
    }

    private void moverMazoWASD(Mazo mazo, float limiteIzq, float limiteDer) {
        mazo.setVelocidadX(0);
        mazo.setVelocidadY(0);

        int mazoId = (mazo == mazo1) ? 1 : 2;
        InputSimulado sim = (mazoId == 1) ? inputSimuladoMazo1 : inputSimuladoMazo2;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || sim.arriba) {
            mazo.setVelocidadY(velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || sim.abajo) {
            mazo.setVelocidadY(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || sim.izquierda) {
            mazo.setVelocidadX(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || sim.derecha) {
            mazo.setVelocidadX(velocidad);
        }

        mazo.actualizarPosicion((int) limiteIzq, (int) limiteDer, (int) yCancha, (int) (yCancha + canchaAlto));
    }

    private void moverMazoFlechas(Mazo mazo, float limiteIzq, float limiteDer) {
        mazo.setVelocidadX(0);
        mazo.setVelocidadY(0);

        int mazoId = (mazo == mazo1) ? 1 : 2;
        InputSimulado sim = (mazoId == 1) ? inputSimuladoMazo1 : inputSimuladoMazo2;

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || sim.arriba) {
            mazo.setVelocidadY(velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || sim.abajo) {
            mazo.setVelocidadY(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || sim.izquierda) {
            mazo.setVelocidadX(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || sim.derecha) {
            mazo.setVelocidadX(velocidad);
        }

        mazo.actualizarPosicion((int) limiteIzq, (int) limiteDer, (int) yCancha, (int) (yCancha + canchaAlto));
    }

    private void moverMazoSimulado(Mazo mazo, float limiteIzq, float limiteDer) {
        mazo.setVelocidadX(0);
        mazo.setVelocidadY(0);

        int mazoId = (mazo == mazo1) ? 1 : 2;
        InputSimulado sim = (mazoId == 1) ? inputSimuladoMazo1 : inputSimuladoMazo2;

        if (sim.arriba) {
            mazo.setVelocidadY(velocidad);
        }
        if (sim.abajo) {
            mazo.setVelocidadY(-velocidad);
        }
        if (sim.izquierda) {
            mazo.setVelocidadX(-velocidad);
        }
        if (sim.derecha) {
            mazo.setVelocidadX(velocidad);
        }

        mazo.actualizarPosicion((int) limiteIzq, (int) limiteDer, (int) yCancha, (int) (yCancha + canchaAlto));
    }

    public CongelarRival getCongelarRival() {
        return this.congelarRival;
    }

    public ControlesInvertidos getControlesInvertidos() {
        return this.controlesInvertidos;
    }

    public void limpiarCongelarRival() {
        this.congelarRival = null;
        this.datosMazo = null;
        this.mazoCongeladoId = -1;
    }

    public void limpiarControlesInvertidos() {
        this.controlesInvertidos = null;
    }

    public void simularTiroEspecial(int mazoId) {
        this.solicitudCpuTiroEspecial = true;
        this.mazoIdTiroEspecial = mazoId;
    }

    public boolean getSolicitudCpuTiroEspecial() {
        return this.solicitudCpuTiroEspecial;
    }

    public int getMazoIdTiroEspecial() {
        return this.mazoIdTiroEspecial;
    }

    public void limpiarSolicitudTiroEspecial() {
        solicitudCpuTiroEspecial = false;
        mazoIdTiroEspecial = -1;
    }
}
