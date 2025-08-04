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
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;

public class MenuPausa extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private final Principal juego;
    private TipoJuegoLibre tipoJuegoLibre;
    private EstadoPartida estadoPartida;

    private int opcionSeleccionada = 0;
    private Label seguir, salir;

    public MenuPausa(Principal juego, TipoJuegoLibre tipoJuegoLibre, EstadoPartida estadoPartida) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.estadoPartida = estadoPartida;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        seguir = new Label("Reanudar", skin);
        salir = new Label("Salir", skin);

        table.add(seguir).width(200).pad(20).row();
        table.add(salir).width(200).pad(20);

        actualizarColores();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcionSeleccionada = (opcionSeleccionada == 0) ? 1 : 0;
            actualizarColores();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcionSeleccionada = (opcionSeleccionada == 0) ? 1 : 0;
            actualizarColores();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (opcionSeleccionada == 0) {
                juego.setScreen(new PantallaJuego(juego, tipoJuegoLibre, estadoPartida));
            } else if (opcionSeleccionada == 1) {
                juego.setScreen(new MenuPrincipal(juego));
            }
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void actualizarColores() {
        seguir.setColor(opcionSeleccionada == 0 ? Color.RED : Color.WHITE);
        salir.setColor(opcionSeleccionada == 1 ? Color.RED : Color.WHITE);
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
}
