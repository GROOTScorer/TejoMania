package io.github.grootscorer.tejomania.entidades.tirosespeciales;

import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;

public abstract class TiroEspecial {
    protected Disco disco;
    protected Mazo mazoQueDispara;
    protected boolean activo = false;
    protected float xCancha;
    protected float yCancha;
    protected float anchoCancha;
    protected float altoCancha;

    public TiroEspecial(Disco disco, Mazo mazoQueDispara, float xCancha, float yCancha, float anchoCancha, float altoCancha) {
        this.disco = disco;
        this.mazoQueDispara = mazoQueDispara;
        this.xCancha = xCancha;
        this.yCancha = yCancha;
        this.anchoCancha = anchoCancha;
        this.altoCancha = altoCancha;
    }

    public abstract void activar();

    public abstract void actualizar(float delta);

    public abstract void desactivar();

    public boolean isActivo() {
        return activo;
    }

    public Disco getDisco() {
        return disco;
    }

    protected void calcularTrayectoriaOptima() {
        float centroDiscoX = disco.getPosicionX() + disco.getRadioDisco();
        float centroDiscoY = disco.getPosicionY() + disco.getRadioDisco();
        float centroMazoX = mazoQueDispara.getPosicionX() + mazoQueDispara.getRadioMazo();

        boolean mazoEstaEnLadoIzquierdo = centroMazoX < xCancha + anchoCancha / 2f;

        float arcoRivalCentroX = mazoEstaEnLadoIzquierdo ? xCancha + anchoCancha : xCancha;
        float arcoRivalCentroY = yCancha + altoCancha / 2f;

        float velocidadTiro = 450f;

        float mejorAngulo = 0;
        float mejorPuntajeEvaluacion = -1;

        int numeroSimulaciones = 36;

        for (int i = 0; i < numeroSimulaciones; i++) {
            float anguloTest = (float) (Math.PI * 2 * i / numeroSimulaciones);

            float velX = velocidadTiro * (float) Math.cos(anguloTest);
            float velY = velocidadTiro * (float) Math.sin(anguloTest);

            float puntajeEvaluacion = evaluarTrayectoria(centroDiscoX, centroDiscoY, velX, velY,
                arcoRivalCentroX, arcoRivalCentroY, mazoEstaEnLadoIzquierdo);

            if (puntajeEvaluacion > mejorPuntajeEvaluacion) {
                mejorPuntajeEvaluacion = puntajeEvaluacion;
                mejorAngulo = anguloTest;
            }
        }

        if (mejorPuntajeEvaluacion > 0) {
            disco.setVelocidadX(velocidadTiro * (float) Math.cos(mejorAngulo));
            disco.setVelocidadY(velocidadTiro * (float) Math.sin(mejorAngulo));
        } else {
            float vectorDirectoX = arcoRivalCentroX - centroDiscoX;
            float vectorDirectoY = arcoRivalCentroY - centroDiscoY;
            float distanciaDirecta = (float) Math.sqrt(vectorDirectoX * vectorDirectoX + vectorDirectoY * vectorDirectoY);

            vectorDirectoX /= distanciaDirecta;
            vectorDirectoY /= distanciaDirecta;

            disco.setVelocidadX(velocidadTiro * vectorDirectoX);
            disco.setVelocidadY(velocidadTiro * vectorDirectoY);
        }
    }

    private float evaluarTrayectoria(float origenX, float origenY, float velX, float velY,
                                     float objetivoX, float objetivoY, boolean disparaDesdeIzquierda) {
        float posX = origenX;
        float posY = origenY;
        float velocidadX = velX;
        float velocidadY = velY;

        float radioArco = altoCancha / 4.5f;
        float limiteInferiorArco = objetivoY - radioArco;
        float limiteSuperiorArco = objetivoY + radioArco;

        int maxPasos = 200;
        float dt = 0.016f;
        int rebotes = 0;
        int maxRebotes = 3;

        for (int paso = 0; paso < maxPasos; paso++) {
            posX += velocidadX * dt;
            posY += velocidadY * dt;

            if (disparaDesdeIzquierda) {
                if (posX >= xCancha + anchoCancha) {
                    if (posY >= limiteInferiorArco && posY <= limiteSuperiorArco) {
                        float distanciaAlCentro = Math.abs(posY - objetivoY);
                        float factorCentrado = 1.0f - (distanciaAlCentro / radioArco) * 0.5f;
                        float factorRebotes = 1.0f - (rebotes * 0.2f);
                        float factorTiempo = 1.0f - (paso / (float)maxPasos) * 0.3f;

                        return 100f * factorCentrado * factorRebotes * factorTiempo;
                    } else {
                        return -1;
                    }
                }
            } else {
                if (posX <= xCancha) {
                    if (posY >= limiteInferiorArco && posY <= limiteSuperiorArco) {
                        float distanciaAlCentro = Math.abs(posY - objetivoY);
                        float factorCentrado = 1.0f - (distanciaAlCentro / radioArco) * 0.5f;
                        float factorRebotes = 1.0f - (rebotes * 0.2f);
                        float factorTiempo = 1.0f - (paso / (float) maxPasos) * 0.3f;

                        return 100f * factorCentrado * factorRebotes * factorTiempo;
                    } else {
                        return -1;
                    }
                }
            }

            if (rebotes >= maxRebotes) {
                return -1;
            }

            boolean enAreaArco = (posY >= limiteInferiorArco && posY <= limiteSuperiorArco);

            if (!enAreaArco) {
                if (disparaDesdeIzquierda) {
                    if (posX <= xCancha) {
                        velocidadX = -velocidadX * 0.9f;
                        posX = xCancha;
                        rebotes++;
                    }
                } else {
                    if (posX >= xCancha + anchoCancha) {
                        velocidadX = -velocidadX * 0.9f;
                        posX = xCancha + anchoCancha;
                        rebotes++;
                    }
                }
            }

            if (posY <= yCancha) {
                velocidadY = -velocidadY * 0.9f;
                posY = yCancha;
                rebotes++;
            }
            if (posY >= yCancha + altoCancha) {
                velocidadY = -velocidadY * 0.9f;
                posY = yCancha + altoCancha;
                rebotes++;
            }
        }

        float distanciaFinalX = Math.abs(posX - objetivoX);
        float distanciaFinalY = Math.abs(posY - objetivoY);
        float distanciaTotal = (float) Math.sqrt(distanciaFinalX * distanciaFinalX + distanciaFinalY * distanciaFinalY);

        float proximidad = Math.max(0, 500 - distanciaTotal) / 500f;
        float factorRebotes = Math.max(0, 1.0f - (rebotes * 0.3f));

        return proximidad * 50f * factorRebotes;
    }
}
