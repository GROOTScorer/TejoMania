package io.github.grootscorer.tejomania.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.grootscorer.tejomania.entidades.Jugador;

public class BarraEspecial extends ProgressBar {
    private Label textoCantidadLlenada;
    private int cantidadLlenada = 0;
    private final int MAX_CANTIDAD = 100;
    private boolean llenado;
    private Jugador jugador;

    public BarraEspecial(float min, float max, float stepSize, boolean vertical, Skin skin) {
        super(min, max, stepSize, vertical, skin);
        this.setValue(0);
    }
    public int getCantidadLlenada() {
        return this.cantidadLlenada;
    }

    public boolean isLlenado() {
        return this.llenado;
    }

    public void aumentarPorcentaje(int porcentaje) {
        this.cantidadLlenada += porcentaje;

        if(this.cantidadLlenada >= this.MAX_CANTIDAD) {
            this.cantidadLlenada = this.MAX_CANTIDAD;
            this.llenado = true;
        }

        this.setValue(this.cantidadLlenada);
    }

    public void vaciarCantidadLlenada() {
        this.cantidadLlenada = 0;
        this.llenado = false;
        this.setValue(0);
    }

    public void setCantidadLlenada(int cantidad) {
        this.cantidadLlenada = Math.min(cantidad, MAX_CANTIDAD);
        this.llenado = (this.cantidadLlenada >= MAX_CANTIDAD);
        this.setValue(this.cantidadLlenada);
    }
}
