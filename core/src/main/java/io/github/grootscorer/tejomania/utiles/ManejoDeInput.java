package io.github.grootscorer.tejomania.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;

public class ManejoDeInput extends InputAdapter {
    private final Mazo mazo1;
    private final Mazo mazo2;
    private final TipoJuegoLibre tipoJuegoLibre;

    private final float xCancha, yCancha;
    private final int canchaAncho, canchaAlto;
    private final int velocidad = 8;

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

    public void actualizarMovimiento() {
        if (mazo1 != null) {
            moverMazoWASD(mazo1, xCancha, xCancha + canchaAncho / 2f);
        }

        if (mazo2 != null) {
            if (tipoJuegoLibre == TipoJuegoLibre.DOS_JUGADORES) {
                moverMazoFlechas(mazo2, xCancha + canchaAncho / 2f, xCancha + canchaAncho);
            }
        }
    }

    private void moverMazoWASD(Mazo mazo, float limiteIzq, float limiteDer) {
        mazo.setVelocidadX(0);
        mazo.setVelocidadY(0);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            mazo.setVelocidadY(velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            mazo.setVelocidadY(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            mazo.setVelocidadX(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            mazo.setVelocidadX(velocidad);
        }

        mazo.actualizarPosicion((int) limiteIzq, (int) limiteDer, (int) yCancha, (int) (yCancha + canchaAlto)
        );
    }

    private void moverMazoFlechas(Mazo mazo, float limiteIzq, float limiteDer) {
        mazo.setVelocidadX(0);
        mazo.setVelocidadY(0);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            mazo.setVelocidadY(velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            mazo.setVelocidadY(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mazo.setVelocidadX(-velocidad);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mazo.setVelocidadX(velocidad);
        }

        mazo.actualizarPosicion((int) limiteIzq, (int) limiteDer, (int) yCancha, (int) (yCancha + canchaAlto)
        );
    }
}
