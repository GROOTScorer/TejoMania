package io.github.grootscorer.tejomania.entidades.modificadores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.pantallas.PantallaJuego;

public class DiscoDoble extends Modificador {
    private PantallaJuego pantallaJuego;
    private boolean efectoEjecutado = false;
    private boolean debeDesaparecer = false;

    public DiscoDoble(PantallaJuego pantallaJuego) {
        this.pantallaJuego = pantallaJuego;
        this.texturaPowerUp = new Texture(Gdx.files.internal("imagenes/sprites/disco_doble.png"));
    }

    @Override
    protected void ejecutarEfecto() {
        if (!efectoEjecutado && pantallaJuego != null) {
            duplicarDisco();
            efectoEjecutado = true;
        }
    }

    private void duplicarDisco() {
        if (isActivo() && disco != null) {
            Disco nuevoDisco = new Disco();
            nuevoDisco.setPosicion(disco.getPosicionX(), disco.getPosicionY());

            nuevoDisco.setVelocidadX(disco.getVelocidadX() + 50);
            nuevoDisco.setVelocidadY(disco.getVelocidadY() - 50);

            pantallaJuego.agregarDiscoSecundario(nuevoDisco);
        }
    }

    public void restaurarDesdeEstadoCompleto(float x, float y, float tiempo, boolean estaActivo, boolean efectoEjecutado) {
        restaurarDesdeEstado(x, y, tiempo, estaActivo);
        this.efectoEjecutado = efectoEjecutado;
    }

    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);
    }

    @Override
    protected boolean debeDesaparecerEspecifico() {
        return this.debeDesaparecer;
    }

    public boolean isEfectoEjecutado() {
        return this.efectoEjecutado;
    }

    public void setEfectoEjecutado(boolean efectoEjecutado) {
        this.efectoEjecutado = efectoEjecutado;
    }
}
