package io.github.grootscorer.tejomania.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class EncabezadoPartida {
    private Stage stage;
    private Skin skin;
    private Label labelNombre1, labelNombre2, marcador1, marcador2, tiempo;
    private String nombre1, nombre2;
    private int puntaje1 = 0, puntaje2 = 0;
    private int cantTiempo = 1;

    public EncabezadoPartida(String nombre1, String nombre2, int cantTiempo) {
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.cantTiempo = cantTiempo;
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public int getPuntaje1() {
        return puntaje1;
    }

    public int getPuntaje2() {
        return puntaje2;
    }

    public int getCantTiempo() {
        return cantTiempo;
    }

    public void aumentarPuntajeJugadorUno() {
        this.puntaje1++;
    }

    public void aumentarPuntajeJugadorDos() {
        this.puntaje2++;
    }
}
