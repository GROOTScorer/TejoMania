package io.github.grootscorer.tejomania.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.estado.EstadoPartida;

public class EncabezadoPartida extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private Label labelNombre1, labelNombre2, marcador1, marcador2, tiempo;
    private String nombre1, nombre2;
    private int puntaje1, puntaje2;
    private float cantTiempo;
    private final EstadoPartida estado;

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    public EncabezadoPartida(EstadoPartida estado) {
        this.estado = estado;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        nombre1 = estado.getJugador1();
        labelNombre1 = new Label(nombre1, skin, "default");
        labelNombre1.setPosition(100 * escalaX, 420 * escalaY);
        labelNombre1.setFontScale(escalaFuente);

        puntaje1 = estado.getPuntaje1();
        marcador1 = new Label(String.valueOf(puntaje1), skin, "default");
        marcador1.setPosition(260 * escalaX, 420 * escalaY);
        marcador1.setFontScale(escalaFuente);

        nombre2 = estado.getJugador2();
        labelNombre2 = new Label(nombre2, skin, "default");
        labelNombre2.setPosition(540 * escalaX, 420 * escalaY);
        labelNombre2.setFontScale(escalaFuente);

        puntaje2 = estado.getPuntaje2();
        marcador2 = new Label(String.valueOf(puntaje2), skin, "default");
        marcador2.setPosition(380 * escalaX, 420 * escalaY);
        marcador2.setFontScale(escalaFuente);

        cantTiempo = estado.getTiempoRestante();
        tiempo = new Label(cantTiempo / 60 + ":" + cantTiempo % 60, skin, "default");
        tiempo.setPosition(320 * escalaX, 420 * escalaY);
        tiempo.setFontScale(escalaFuente);

        stage.addActor(labelNombre1);
        stage.addActor(marcador1);
        stage.addActor(labelNombre2);
        stage.addActor(marcador2);
        if(estado.isJugandoPorTiempo()) {
            stage.addActor(tiempo);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        nombre1 = estado.getJugador1();
        labelNombre1.setText(nombre1);
        puntaje1 = estado.getPuntaje1();
        marcador1.setText(String.valueOf(puntaje1));

        nombre2 = estado.getJugador2();
        labelNombre2.setText(nombre2);
        puntaje2 = estado.getPuntaje2();
        marcador2.setText(String.valueOf(puntaje2));

        cantTiempo = estado.getTiempoRestante();
        tiempo.setText(String.format("%02d:%02d", (int) (cantTiempo / 60), (int) (cantTiempo % 60)));

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public Stage getStage() {
        return this.stage;
    }
}
