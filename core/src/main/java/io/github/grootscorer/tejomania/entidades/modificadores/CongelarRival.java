package io.github.grootscorer.tejomania.entidades.modificadores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.grootscorer.tejomania.entidades.Jugador;
import io.github.grootscorer.tejomania.estado.EstadoFisico;
import io.github.grootscorer.tejomania.pantallas.PantallaJuego;

public class CongelarRival extends Modificador {
    private PantallaJuego pantallaJuego;
    private EstadoFisico estadoFisico;
    private boolean efectoEjecutado;
    private boolean debeDesaparecer;
    private float tiempoEfecto = 0;
    private final float DURACION_EFECTO = 5.0f;

    public CongelarRival(PantallaJuego pantallaJuego, EstadoFisico estadoFisico) {
        this.pantallaJuego = pantallaJuego;
        this.estadoFisico = estadoFisico;
        String rutaRelativaSprite = "imagenes/sprites/congelar.png";
        String rutaAbsolutaSprite = Gdx.files.internal(rutaRelativaSprite).file().getAbsolutePath();
        this.texturaPowerUp = new Texture(Gdx.files.internal(rutaAbsolutaSprite));
    }

    @Override
    protected void ejecutarEfecto() {
        if (!efectoEjecutado && pantallaJuego != null) {
            congelarRival();
            this.efectoEjecutado = true;
            tiempoEfecto = 0;
        }
    }

    private void congelarRival() {
        if(isActivo()) {

        }
    }

    public void restaurarDesdeEstadoCompleto(float x, float y, float tiempo, boolean estaActivo, boolean efectoEjecutado) {
        restaurarDesdeEstado(x, y, tiempo, estaActivo);
        this.efectoEjecutado = efectoEjecutado;
    }

    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);

        if (efectoEjecutado) {
            tiempoEfecto += delta;
            if (tiempoEfecto >= DURACION_EFECTO) {
                this.debeDesaparecer = true;
                this.efectoEjecutado = false;
            }
        }
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
