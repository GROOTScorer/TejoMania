package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.Principal;

public class MenuConfiguracion extends ScreenAdapter {

    private Stage stage;
    private Skin skin;
    private final Principal juego;
    private int opcionActual = 0;
    private int volumenSonido = 100;
    private int volumenMusica = 100;
    private Label[] opciones;

    public MenuConfiguracion(Principal juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(true);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        opciones = new Label[3];
        opciones[0] = new Label("Volumen de sonido: " + volumenSonido, skin, "default");
        opciones[0].setColor(Color.RED);
        opciones[1] = new Label("Volumen de musica: " + volumenMusica, skin, "default");
        opciones[2] = new Label("Salir", skin, "default");

        for(Label opcion : opciones) {
            table.add(opcion).pad(10);
            table.row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcionActual = (opcionActual - 1 + opciones.length) % opciones.length;
            actualizarSeleccion();
        }   else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcionActual = (opcionActual + 1) % opciones.length;
            actualizarSeleccion();
        }   else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            manejarInputIzquierda();
        }   else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            manejarInputDerecha();
        }   else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarInputEnter();
        }

        stage.act(delta);
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

    private void actualizarSeleccion() {
        for(int i = 0; i < opciones.length; i++) {
            if(opcionActual == i) {
                opciones[i].setColor(Color.RED);
            }   else {
                opciones[i].setColor(Color.WHITE);
            }
        }
    }

    private void manejarInputEnter() {
        if(opcionActual == 2) {
            juego.setScreen(new MenuPrincipal(juego));
        }
    }

    private void manejarInputIzquierda() {
        if(opcionActual == 0) {
            if(volumenSonido <= 0) {
                volumenSonido = 0;
            }   else {
                volumenSonido--;
            }
            opciones[0].setText("Volumen de sonido: " + volumenSonido);
        }   else if(opcionActual == 1) {
            if(volumenMusica <= 0) {
                volumenMusica = 0;
            }   else {
                volumenMusica--;
            }
            opciones[1].setText("Volumen de musica: " + volumenMusica);
        }
    }

    private void manejarInputDerecha() {
        if(opcionActual == 0) {
            if(volumenSonido >= 100) {
                volumenSonido = 100;
            }   else {
                volumenSonido++;
            }
            opciones[0].setText("Volumen de sonido: " + volumenSonido);
        }   else if(opcionActual == 1) {
            if(volumenMusica >= 100) {
                volumenMusica = 100;
            }   else {
                volumenMusica++;
            }
            opciones[1].setText("Volumen de musica: " + volumenMusica);
        }
    }
}
