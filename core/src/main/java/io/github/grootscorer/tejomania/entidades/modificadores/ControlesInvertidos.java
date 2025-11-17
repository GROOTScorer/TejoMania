package io.github.grootscorer.tejomania.entidades.modificadores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.grootscorer.tejomania.pantallas.PantallaJuego;

public class ControlesInvertidos extends Modificador {
    private boolean efectoEjecutado = false;
    private final float DURACION_EFECTO = 8.0f;
    private float tiempoEfectoActivo = 0;
    private PantallaJuego pantallaJuego;

    public ControlesInvertidos(PantallaJuego pantallaJuego) {
        super();
        this.pantallaJuego = pantallaJuego;
        String rutaRelativaSprite = "imagenes/sprites/cambiar_controles.png";
        String rutaAbsolutaSprite = Gdx.files.internal(rutaRelativaSprite).file().getAbsolutePath();
        this.texturaPowerUp = new Texture(Gdx.files.internal(rutaAbsolutaSprite));
    }

    @Override
    public void actualizar(float delta) {
        tiempoVida += delta;

        if (disco != null && !activo) {
            verificarColision();
        }

        if (efectoEjecutado) {
            tiempoEfectoActivo += delta;
        }
    }

    @Override
    protected void ejecutarEfecto() {
        efectoEjecutado = true;
        tiempoEfectoActivo = 0;
    }

    @Override
    protected boolean debeDesaparecerEspecifico() {
        return efectoEjecutado && tiempoEfectoActivo >= DURACION_EFECTO;
    }

    public boolean isEfectoEjecutado() {
        return this.efectoEjecutado;
    }

    public void restaurarDesdeEstadoCompleto(float x, float y, float tiempo, boolean estaActivo, boolean efectoEjecutado) {
        restaurarDesdeEstado(x, y, tiempo, estaActivo);
        this.efectoEjecutado = efectoEjecutado;
        if (efectoEjecutado) {
            this.tiempoEfectoActivo = tiempo;
        }
    }

    public float getTiempoEfectoActivo() {
        return this.tiempoEfectoActivo;
    }
}
