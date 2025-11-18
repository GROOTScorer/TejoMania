package io.github.grootscorer.tejomania.entidades.tirosespeciales;

import com.badlogic.gdx.graphics.Texture;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;

public class TiroInvisible extends TiroEspecial {
    private float tiempoDesvanecimiento = 0;
    private final float DURACION_DESVANECIMIENTO = 0.1f;
    private boolean desvaneciendo = false;
    private boolean invisible = false;
    private float alphaActual = 1.0f;

    public TiroInvisible(Disco disco, Mazo mazoQueDispara, float xCancha, float yCancha, float anchoCancha, float altoCancha) {
        super(disco, mazoQueDispara, xCancha, yCancha, anchoCancha, altoCancha);
    }

    @Override
    public void activar() {
        activo = true;
        desvaneciendo = true;
        invisible = false;
        tiempoDesvanecimiento = 0;
        alphaActual = 1.0f;
        calcularTrayectoriaOptima();
    }

    @Override
    public void actualizar(float delta) {
        if (!activo) return;

        if (desvaneciendo) {
            tiempoDesvanecimiento += delta;
            alphaActual = 1.0f - (tiempoDesvanecimiento / DURACION_DESVANECIMIENTO);

            if (tiempoDesvanecimiento >= DURACION_DESVANECIMIENTO) {
                desvaneciendo = false;
                invisible = true;
                alphaActual = 0.0f;
            }
        }
    }

    @Override
    public void desactivar() {
        activo = false;
        desvaneciendo = false;
        invisible = false;
        alphaActual = 1.0f;
    }

    public boolean isInvisible() {
        return this.invisible;
    }

    public boolean isDesvaneciendo() {
        return this.desvaneciendo;
    }

    public float getAlphaActual() {
        return this.alphaActual;
    }

    public void hacerVisible() {
        desactivar();
    }
}
