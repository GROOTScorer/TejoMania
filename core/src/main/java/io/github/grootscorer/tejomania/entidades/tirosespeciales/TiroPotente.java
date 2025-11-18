package io.github.grootscorer.tejomania.entidades.tirosespeciales;

import com.badlogic.gdx.Gdx;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;

public class TiroPotente extends TiroEspecial {
    private boolean ralentizando = false;

    public TiroPotente(Disco disco, Mazo mazoQueDispara, float xCancha, float yCancha, float anchoCancha, float altoCancha) {
        super(disco, mazoQueDispara, xCancha, yCancha, anchoCancha, altoCancha);
    }

    @Override
    public void activar() {
        activo = true;
        ralentizando = false;
        calcularTrayectoriaOptima();

        disco.setMaxVelocidad(900);

        if(disco.getPosicionX() < (Gdx.graphics.getWidth() / 2f)) {
            disco.setVelocidadX(900);
        } else {
            disco.setVelocidadX(-900);
        }
    }

    @Override
    public void actualizar(float delta) {
        if (!activo) return;

        if (ralentizando) {
            if(disco.getVelocidadX() > 200 || disco.getVelocidadX() < -200) {
                disco.setVelocidadX(disco.getVelocidadX() * 0.9f);
                disco.setVelocidadY(disco.getVelocidadY() * 0.9f);
            }
            desactivar();
        }
    }

    @Override
    public void desactivar() {
        this.activo = false;
    }

    public void iniciarRalentizacion() {
        if (activo && !ralentizando) {
            ralentizando = true;

        }
    }

    public boolean isRalentizando() {
        return this.ralentizando;
    }
}
