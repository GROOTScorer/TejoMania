package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;

public class EleccionNombre extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private final Principal juego;
    private final TipoJuegoLibre tipoJuegoLibre;
    private String nombre1, nombre2;

    public EleccionNombre(Principal juego, TipoJuegoLibre tipoJuegoLibre) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
    }

    public void show() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label titulo = new Label("Elija su nombre", skin, "default");
        titulo.setFontScale(3f);

        Table tituloContenedor = new Table();
        tituloContenedor.setFillParent(true);
        tituloContenedor.top();
        tituloContenedor.add(titulo).expandX().center().padTop(20);
        stage.addActor(tituloContenedor);

        Label textoJugador = new Label("Jugador 1", skin, "default");

        Label nombre = new Label(nombre1, skin, "default");

        Label empezarJuego = new Label("Empezar", skin, "default");
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void actualizarNombre() {

    }

    private void manejarEnter() {

    }

    public String getNombre1() {
        return nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }
}
