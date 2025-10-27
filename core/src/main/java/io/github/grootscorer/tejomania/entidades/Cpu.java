package io.github.grootscorer.tejomania.entidades;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.fsm.State;

import io.github.grootscorer.tejomania.enums.DificultadCPU;
import io.github.grootscorer.tejomania.hud.BarraEspecial;
import io.github.grootscorer.tejomania.utiles.ManejoDeInput;

public class Cpu {
    private final Mazo cpuMazo;
    private final Mazo rivalMazo;
    private final Disco discoOriginal;
    private Disco discoActual;
    private final ManejoDeInput manejoInput;
    private final int mazoId;

    private StateMachine<Cpu, CpuState> estadoMaquina;

    private final float tiempoReaccion;
    private final float factorPrediccion;
    private final float precision;
    private long ultimaDecisionEnMilis;

    private boolean ultimoMovimientoArriba = false;
    private boolean ultimoMovimientoAbajo = false;
    private boolean ultimoMovimientoIzquierda = false;
    private boolean ultimoMovimientoDerecha = false;

    private final BarraEspecial barraEspecial;
    private final boolean jugandoConTirosEspeciales;

    public Cpu(Mazo cpuMazo, Mazo rivalMazo, Disco disco, ManejoDeInput manejoInput, int mazoId, DificultadCPU dificultad, BarraEspecial barraEspecial, boolean tirosEspecialesActivos) {
        this.cpuMazo = cpuMazo;
        this.rivalMazo = rivalMazo;
        this.discoActual = disco;
        this.discoOriginal = disco;
        this.manejoInput = manejoInput;
        this.mazoId = mazoId;
        this.barraEspecial = barraEspecial;
        this.jugandoConTirosEspeciales = tirosEspecialesActivos;

        switch (dificultad) {
            case FACIL:
                tiempoReaccion = 500;
                factorPrediccion = 0.4f;
                precision = 0.55f;
                break;
            case INTERMEDIO:
                tiempoReaccion = 200;
                factorPrediccion = 0.6f;
                precision = 0.75f;
                break;
            default:
                tiempoReaccion = 50;
                factorPrediccion = 1.0f;
                precision = 0.95f;
                break;
        }

        estadoMaquina = new DefaultStateMachine<>(this, CpuState.DEFENDER);
        ultimaDecisionEnMilis = TimeUtils.millis();
    }

    public void update(float delta) {
        estadoMaquina.update();
    }

    private Vector2 predecirPosicionDisco(float tiempoSegundos) {
        float posicionX = discoActual.getPosicionX() + discoActual.getRadioDisco();
        float posicionY = discoActual.getPosicionY() + discoActual.getRadioDisco();
        float velocidadX = discoActual.getVelocidadX();
        float velocidadY = discoActual.getVelocidadY();

        return new Vector2(posicionX + velocidadX * tiempoSegundos, posicionY + velocidadY * tiempoSegundos);
    }

    private float distanciaEntre(float x1, float y1, float x2, float y2) {
        float distanciaX = x2 - x1;
        float distanciaY = y2 - y1;
        return (float)Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
    }

    private void moverHacia(float objetivoX, float objetivoY) {
        moverHacia(objetivoX, objetivoY, false);
    }

    private void moverHacia(float objetivoX, float objetivoY, boolean golpe) {
        boolean izquierda = false, derecha = false, arriba = false, abajo = false;

        float cpuCentroX = cpuMazo.getPosicionX() + cpuMazo.getRadioMazo();
        float cpuCentroY = cpuMazo.getPosicionY() + cpuMazo.getRadioMazo();

        float distanciaX = objetivoX - cpuCentroX;
        float distanciaY = objetivoY - cpuCentroY;

        float margenTolerancia = 3f;

        float distanciaTotal = (float) Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

        if (distanciaTotal > 50) {
            if (Math.abs(distanciaX) > Math.abs(distanciaY) * 1.3f) {
                if (distanciaX < -margenTolerancia) izquierda = true;
                else if (distanciaX > margenTolerancia) derecha = true;

                if (Math.abs(distanciaY) > margenTolerancia * 2) {
                    if (distanciaY < -margenTolerancia * 2) abajo = true;
                    else if (distanciaY > margenTolerancia * 2) arriba = true;
                }
            } else if (Math.abs(distanciaY) > Math.abs(distanciaX) * 1.3f) {
                if (distanciaY < -margenTolerancia) abajo = true;
                else if (distanciaY > margenTolerancia) arriba = true;

                if (Math.abs(distanciaX) > margenTolerancia * 2) {
                    if (distanciaX < -margenTolerancia * 2) izquierda = true;
                    else if (distanciaX > margenTolerancia * 2) derecha = true;
                }
            } else {
                if (distanciaX < -margenTolerancia) izquierda = true;
                else if (distanciaX > margenTolerancia) derecha = true;

                if (distanciaY < -margenTolerancia) abajo = true;
                else if (distanciaY > margenTolerancia) arriba = true;
            }
        } else {
            if (distanciaX < -margenTolerancia) izquierda = true;
            else if (distanciaX > margenTolerancia) derecha = true;

            if (distanciaY < -margenTolerancia) abajo = true;
            else if (distanciaY > margenTolerancia) arriba = true;
        }

        ultimoMovimientoArriba = arriba;
        ultimoMovimientoAbajo = abajo;
        ultimoMovimientoIzquierda = izquierda;
        ultimoMovimientoDerecha = derecha;

        manejoInput.setInputSimuladoParaMazo(mazoId, arriba, abajo, izquierda, derecha, golpe);
    }

    private void limpiarInputSimulado() {
        ultimoMovimientoArriba = false;
        ultimoMovimientoAbajo = false;
        ultimoMovimientoIzquierda = false;
        ultimoMovimientoDerecha = false;
        manejoInput.clearInputSimuladoParaMazo(mazoId);
    }

    private float getCentroMazoX(Mazo mazo) {
        return mazo.getPosicionX() + mazo.getRadioMazo();
    }

    private float getCentroMazoY(Mazo mazo) {
        return mazo.getPosicionY() + mazo.getRadioMazo();
    }

    private Disco decidirDiscoObjetivo(Disco discoSecundario) {
        if (discoSecundario == null) {
            return discoActual;
        }

        float cpuCentroX = getCentroMazoX(cpuMazo);

        float discoPrincipalX = discoActual.getPosicionX() + discoActual.getRadioDisco();
        float discoSecundarioX = discoSecundario.getPosicionX() + discoSecundario.getRadioDisco();

        float distanciaPrincipal, distanciaSecundario;

        if (mazoId == 2) {
            distanciaPrincipal = Math.abs(cpuCentroX - discoPrincipalX);
            distanciaSecundario = Math.abs(cpuCentroX - discoSecundarioX);

            if (discoPrincipalX > cpuCentroX && discoSecundarioX > cpuCentroX) {
                return (discoPrincipalX > discoSecundarioX) ? discoActual : discoSecundario;
            }
        } else {
            distanciaPrincipal = Math.abs(cpuCentroX - discoPrincipalX);
            distanciaSecundario = Math.abs(cpuCentroX - discoSecundarioX);

            if (discoPrincipalX < cpuCentroX && discoSecundarioX < cpuCentroX) {
                return (discoPrincipalX < discoSecundarioX) ? discoActual : discoSecundario;
            }
        }

        return (distanciaPrincipal < distanciaSecundario) ? discoActual : discoSecundario;
    }

    public enum CpuState implements State<Cpu> {
        DEFENDER {
            @Override
            public void enter(Cpu entity) {
                entity.limpiarInputSimulado();
            }

            @Override
            public void update(Cpu entity) {
                float cpuCentroX = entity.getCentroMazoX(entity.cpuMazo);
                float cpuCentroY = entity.getCentroMazoY(entity.cpuMazo);
                float discoCentroX = entity.discoActual.getPosicionX() + entity.discoActual.getRadioDisco();
                float discoCentroY = entity.discoActual.getPosicionY() + entity.discoActual.getRadioDisco();
                float distanciaDiscoACpu = entity.distanciaEntre(cpuCentroX, cpuCentroY, discoCentroX, discoCentroY);

                boolean discoDetras;
                if (entity.mazoId == 2) {
                    discoDetras = discoCentroX > cpuCentroX + 10;
                } else {
                    discoDetras = discoCentroX < cpuCentroX - 10;
                }

                if (discoDetras && distanciaDiscoACpu < 180) {

                    if (distanciaDiscoACpu < 45) {
                        Vector2 posicionSegura = entity.calcularPosicionSeguraParaGolpear();
                        entity.moverHacia(posicionSegura.x, posicionSegura.y);

                        boolean golpeSeguro = !entity.golpeariaHaciaPropioArco(cpuCentroX, cpuCentroY, discoCentroX, discoCentroY);
                        if (distanciaDiscoACpu < 35 && golpeSeguro) {
                            entity.estadoMaquina.changeState(ATACAR);
                        }
                    } else {
                        entity.moverHacia(discoCentroX, discoCentroY);
                        if (distanciaDiscoACpu < 50) {
                            entity.estadoMaquina.changeState(ATACAR);
                        }
                    }

                    entity.ultimaDecisionEnMilis = TimeUtils.millis();
                    return;
                }

                if (entity.discoApuntaHaciaArcoPropio()) {
                    if (distanciaDiscoACpu < 50) {
                        Vector2 posSegura = entity.calcularPosicionSeguraParaGolpear();
                        entity.moverHacia(posSegura.x, posSegura.y);

                        boolean golpeSeguro = !entity.golpeariaHaciaPropioArco(cpuCentroX, cpuCentroY, discoCentroX, discoCentroY);
                        if (distanciaDiscoACpu < 40 && golpeSeguro) {
                            entity.estadoMaquina.changeState(ATACAR);
                        }
                    } else {
                        Vector2 puntoIntercepcion = entity.calcularPuntoIntercepcion();
                        entity.moverHacia(puntoIntercepcion.x, puntoIntercepcion.y);

                        if (distanciaDiscoACpu < 60) {
                            entity.estadoMaquina.changeState(ATACAR);
                        }
                    }

                    entity.ultimaDecisionEnMilis = TimeUtils.millis();
                    return;
                }

                long ahora = TimeUtils.millis();
                if (ahora - entity.ultimaDecisionEnMilis < entity.tiempoReaccion) {
                    entity.manejoInput.setInputSimuladoParaMazo(entity.mazoId,
                        entity.ultimoMovimientoArriba, entity.ultimoMovimientoAbajo, entity.ultimoMovimientoIzquierda, entity.ultimoMovimientoDerecha, false);
                    return;
                }
                entity.ultimaDecisionEnMilis = ahora;

                float campoCentroX = (entity.getCentroMazoX(entity.cpuMazo) + entity.getCentroMazoX(entity.rivalMazo)) / 2f;
                boolean discoEnZonaPropia;
                if (entity.mazoId == 2) {
                    discoEnZonaPropia = discoCentroX > campoCentroX - 50;
                } else {
                    discoEnZonaPropia = discoCentroX < campoCentroX + 50;
                }

                if (discoEnZonaPropia && distanciaDiscoACpu < 180) {
                    Vector2 puntoIntercepcion = entity.calcularPuntoIntercepcion();
                    entity.moverHacia(puntoIntercepcion.x, puntoIntercepcion.y);

                    if (distanciaDiscoACpu < 70) {
                        entity.estadoMaquina.changeState(ATACAR);
                    }
                } else if (distanciaDiscoACpu < 80) {
                    entity.moverHacia(discoCentroX, discoCentroY);
                    entity.estadoMaquina.changeState(ATACAR);
                } else {
                    entity.moverHacia(cpuCentroX, discoCentroY);
                }
            }

            @Override
            public boolean onMessage(Cpu entity, Telegram telegram) {
                return false;
            }
        },

        ATACAR {
            @Override
            public void update(Cpu entity) {
                long ahora = TimeUtils.millis();
                if (ahora - entity.ultimaDecisionEnMilis < entity.tiempoReaccion) {
                    entity.manejoInput.setInputSimuladoParaMazo(entity.mazoId,
                        entity.ultimoMovimientoArriba, entity.ultimoMovimientoAbajo, entity.ultimoMovimientoIzquierda, entity.ultimoMovimientoDerecha, false);
                    return;
                }
                entity.ultimaDecisionEnMilis = ahora;

                float cpuCentroX = entity.getCentroMazoX(entity.cpuMazo);
                float cpuCentroY = entity.getCentroMazoY(entity.cpuMazo);
                float discoCentroX = entity.discoActual.getPosicionX() + entity.discoActual.getRadioDisco();
                float discoCentroY = entity.discoActual.getPosicionY() + entity.discoActual.getRadioDisco();
                float rivalCentroX = entity.getCentroMazoX(entity.rivalMazo);
                float rivalCentroY = entity.getCentroMazoY(entity.rivalMazo);
                float distanciaAlDisco = entity.distanciaEntre(cpuCentroX, cpuCentroY, discoCentroX, discoCentroY);

                if (entity.jugandoConTirosEspeciales && entity.puedeUsarTiroEspecial(entity.barraEspecial)) {
                    float distanciaAlRival = entity.distanciaEntre(cpuCentroX, cpuCentroY, rivalCentroX, rivalCentroY);

                    if (distanciaAlRival > 100 && distanciaAlDisco < 60) {
                        entity.manejoInput.simularTiroEspecial(entity.mazoId);
                    }
                }

                if (entity.discoApuntaHaciaArcoPropio() && distanciaAlDisco > 40) {
                    entity.estadoMaquina.changeState(DEFENDER);
                    return;
                }

                boolean discoEstaAtras;
                if (entity.mazoId == 2) {
                    discoEstaAtras = discoCentroX > cpuCentroX + 30;
                } else {
                    discoEstaAtras = discoCentroX < cpuCentroX - 30;
                }

                if (discoEstaAtras && distanciaAlDisco > 50) {
                    entity.estadoMaquina.changeState(DEFENDER);
                    return;
                }

                float campoCentroX = (cpuCentroX + rivalCentroX) / 2f;
                boolean discoEnZonaRival;
                if (entity.mazoId == 2) {
                    discoEnZonaRival = discoCentroX < campoCentroX - 80;
                } else {
                    discoEnZonaRival = discoCentroX > campoCentroX + 80;
                }

                if (distanciaAlDisco > 200 && discoEnZonaRival) {
                    entity.estadoMaquina.changeState(DEFENDER);
                    return;
                }

                if (distanciaAlDisco < 100) {
                    boolean riesgoAutogol = entity.golpeariaHaciaPropioArco(cpuCentroX, cpuCentroY, discoCentroX, discoCentroY);

                    if (riesgoAutogol && distanciaAlDisco < 60) {
                        Vector2 posicionSegura = entity.calcularPosicionSeguraParaGolpear();
                        entity.moverHacia(posicionSegura.x, posicionSegura.y);

                        boolean situacionSegura = !entity.golpeariaHaciaPropioArco(
                            entity.getCentroMazoX(entity.cpuMazo),
                            entity.getCentroMazoY(entity.cpuMazo),
                            discoCentroX,
                            discoCentroY
                        );
                        entity.moverHacia(posicionSegura.x, posicionSegura.y, situacionSegura && distanciaAlDisco < 40);
                        return;
                    }

                    float arcoRivalX;
                    if (entity.mazoId == 2) {
                        arcoRivalX = rivalCentroX - 180;
                    } else {
                        arcoRivalX = rivalCentroX + 180;
                    }

                    float targetY;
                    if (entity.precision > 0.9f) {
                        targetY = rivalCentroY;
                    } else if (entity.precision > 0.6f) {
                        targetY = rivalCentroY + (float)((Math.random() - 0.5) * 40);
                    } else {
                        targetY = discoCentroY + (float)((Math.random() - 0.5) * 80);
                    }

                    float anguloDisparo = (float) Math.atan2(targetY - discoCentroY, arcoRivalX - discoCentroX);
                    float offsetX = (float) Math.cos(anguloDisparo + Math.PI) * 35;
                    float offsetY = (float) Math.sin(anguloDisparo + Math.PI) * 35;

                    float objetivoX = discoCentroX + offsetX;
                    float objetivoY = discoCentroY + offsetY;

                    boolean debeGolpear = distanciaAlDisco < 50;
                    entity.moverHacia(objetivoX, objetivoY, debeGolpear);
                } else {
                    float tiempoPrediccion = entity.factorPrediccion * 0.4f;
                    Vector2 posicionPredecida = entity.predecirPosicionDisco(tiempoPrediccion);
                    entity.moverHacia(posicionPredecida.x, posicionPredecida.y);
                }
            }

            @Override
            public boolean onMessage(Cpu entity, Telegram telegram) {
                return false;
            }
        },

        REPOSICIONARSE {
            @Override
            public void update(Cpu entity) {
                long ahora = TimeUtils.millis();
                if (ahora - entity.ultimaDecisionEnMilis < entity.tiempoReaccion) {
                    entity.manejoInput.setInputSimuladoParaMazo(entity.mazoId,
                        entity.ultimoMovimientoArriba, entity.ultimoMovimientoAbajo, entity.ultimoMovimientoIzquierda, entity.ultimoMovimientoDerecha, false);
                    return;
                }
                entity.ultimaDecisionEnMilis = ahora;

                float objetivoX = entity.getCentroMazoX(entity.cpuMazo);
                float objetivoY = entity.getCentroMazoY(entity.rivalMazo);

                entity.moverHacia(objetivoX, objetivoY);

                entity.estadoMaquina.changeState(DEFENDER);
            }

            @Override
            public boolean onMessage(Cpu entity, Telegram telegram) {
                return false;
            }
        };

        @Override public void enter(Cpu entity) { }
        @Override public void exit(Cpu entity) { }
    }

    private boolean discoApuntaHaciaArcoPropio() {
        float discoCentroX = discoActual.getPosicionX() + discoActual.getRadioDisco();
        float cpuCentroX = getCentroMazoX(cpuMazo);

        boolean discoVaHaciaArco;
        if (mazoId == 2) {
            discoVaHaciaArco = discoActual.getVelocidadX() > 20 && discoCentroX < cpuCentroX + 120;
        } else {
            discoVaHaciaArco = discoActual.getVelocidadX() < -20 && discoCentroX > cpuCentroX - 120;
        }

        if (mazoId == 2) {
            if (discoCentroX > cpuCentroX + 80) {
                discoVaHaciaArco = true;
            }
        } else {
            if (discoCentroX < cpuCentroX - 80) {
                discoVaHaciaArco = true;
            }
        }

        return discoVaHaciaArco;
    }

    private Vector2 calcularPuntoIntercepcion() {
        float cpuCentroX = getCentroMazoX(cpuMazo);

        float tiempoPrediccion = factorPrediccion * 0.8f;
        Vector2 posicionFutura = predecirPosicionDisco(tiempoPrediccion);

        float intercepcionX;
        if (mazoId == 2) {
            intercepcionX = Math.min(posicionFutura.x + 30, cpuCentroX);
        } else {
            intercepcionX = Math.max(posicionFutura.x - 30, cpuCentroX);
        }

        return new Vector2(intercepcionX, posicionFutura.y);
    }

    private Vector2 calcularPosicionSeguraParaGolpear() {
        float discoCentroX = discoActual.getPosicionX() + discoActual.getRadioDisco();
        float discoCentroY = discoActual.getPosicionY() + discoActual.getRadioDisco();
        float cpuCentroY = getCentroMazoY(cpuMazo);

        float direccionSeguraX, direccionSeguraY;

        if (mazoId == 2) {
            direccionSeguraX = -1f;
        } else {
            direccionSeguraX = 1f;
        }

        if (discoCentroY < cpuCentroY - 30) {
            direccionSeguraY = -0.5f;
        } else if (discoCentroY > cpuCentroY + 30) {
            direccionSeguraY = 0.5f;
        } else {
            direccionSeguraY = 0f;
        }

        float magnitud = (float) Math.sqrt(direccionSeguraX * direccionSeguraX + direccionSeguraY * direccionSeguraY);
        direccionSeguraX /= magnitud;
        direccionSeguraY /= magnitud;

        float distanciaOptima = 40f;
        float posicionX = discoCentroX - (direccionSeguraX * distanciaOptima);
        float posicionY = discoCentroY - (direccionSeguraY * distanciaOptima);

        return new Vector2(posicionX, posicionY);
    }

    private boolean golpeariaHaciaPropioArco(float cpuX, float cpuY, float discoX, float discoY) {
        float vectorGolpeX = discoX - cpuX;

        if (mazoId == 2) {
            return vectorGolpeX > 0;
        } else {
            return vectorGolpeX < 0;
        }
    }

    private boolean puedeUsarTiroEspecial(BarraEspecial barra) {
        return barra != null && barra.isLlenado();
    }

    public void setDiscoSecundario(Disco discoSecundario) {
        this.discoActual = decidirDiscoObjetivo(discoSecundario);
    }

    public void restaurarDiscoOriginal(Disco discoOriginal) {
        this.discoActual = this.discoOriginal;
    }
}
