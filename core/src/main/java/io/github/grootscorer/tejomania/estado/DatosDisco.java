package io.github.grootscorer.tejomania.estado;

public class DatosDisco {
    private float discoPosX, discoPosY, discoVelX, discoVelY;
    private boolean discoHaAnotadoGol = false;

    private boolean hayDiscoSecundario = false;
    private float discoSecundarioPosX, discoSecundarioPosY, discoSecundarioVelX, discoSecundarioVelY;
    private boolean discoSecundarioHaAnotadoGol = false;

    public DatosDisco() {}

    public DatosDisco(float discoPosX, float discoPosY, float discoVelX, float discoVelY, boolean discoHaAnotadoGol,
                      boolean hayDiscoSecundario, float discoSecundarioPosX, float discoSecundarioPosY,
                      float discoSecundarioVelX, float discoSecundarioVelY, boolean discoSecundarioHaAnotadoGol) {
        this.discoPosX = discoPosX;
        this.discoPosY = discoPosY;
        this.discoVelX = discoVelX;
        this.discoVelY = discoVelY;
        this.discoHaAnotadoGol = discoHaAnotadoGol;
        this.hayDiscoSecundario = hayDiscoSecundario;
        this.discoSecundarioPosX = discoSecundarioPosX;
        this.discoSecundarioPosY = discoSecundarioPosY;
        this.discoSecundarioVelX = discoSecundarioVelX;
        this.discoSecundarioVelY = discoSecundarioVelY;
        this.discoSecundarioHaAnotadoGol = discoSecundarioHaAnotadoGol;
    }

    public float getDiscoPosX() {
        return this.discoPosX;
    }

    public float getDiscoPosY() {
        return this.discoPosY;
    }

    public float getDiscoVelX() {
        return this.discoVelX;
    }

    public float getDiscoVelY() {
        return this.discoVelY;
    }

    public boolean isDiscoHaAnotadoGol() {
        return this.discoHaAnotadoGol;
    }

    public boolean isHayDiscoSecundario() {
        return this.hayDiscoSecundario;
    }

    public float getDiscoSecundarioPosX() {
        return this.discoSecundarioPosX;
    }

    public float getDiscoSecundarioPosY() {
        return this.discoSecundarioPosY;
    }

    public float getDiscoSecundarioVelX() {
        return this.discoSecundarioVelX;
    }

    public float getDiscoSecundarioVelY() {
        return this.discoSecundarioVelY;
    }

    public boolean isDiscoSecundarioHaAnotadoGol() {
        return this.discoSecundarioHaAnotadoGol;
    }
}
