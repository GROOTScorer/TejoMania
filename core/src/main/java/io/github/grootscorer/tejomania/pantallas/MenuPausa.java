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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoFisico;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

public class MenuPausa extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private final Principal juego;
    private TipoJuegoLibre tipoJuegoLibre;
    private EstadoPartida estadoPartida;

    private int opcionSeleccionada = 0;
    private Label seguir, volumenSonido, salir;
    private int volumenSonidoValor = (int) (ManejoDeAudio.getVolumenSonido() * 100);

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    private EstadoFisico estadoFisico;

    public MenuPausa(Principal juego, TipoJuegoLibre tipoJuegoLibre, EstadoPartida estadoPartida, EstadoFisico estadoFisico) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.estadoPartida = estadoPartida;
        this.estadoFisico = estadoFisico;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        seguir = new Label("Reanudar", skin);
        seguir.setFontScale(1.5f * escalaFuente);
        volumenSonido = new Label("Volumen de sonido: " + volumenSonidoValor, skin);
        volumenSonido.setFontScale(1.5f * escalaFuente);
        salir = new Label("Salir", skin);
        salir.setFontScale(1.5f * escalaFuente);

        seguir.setAlignment(Align.center);
        volumenSonido.setAlignment(Align.center);
        salir.setAlignment(Align.center);

        table.add(seguir).width(200).pad(20).center().row();
        table.add(volumenSonido).width(200).pad(20).center().row();
        table.add(salir).width(200).pad(20).center();

        actualizarColores();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcionSeleccionada = (opcionSeleccionada - 1 + 3) % 3;
            actualizarColores();
            ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("audio/sonidos/sonido_seleccion.mp3")));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcionSeleccionada = (opcionSeleccionada + 1) % 3;
            actualizarColores();
            ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("audio/sonidos/sonido_seleccion.mp3")));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && opcionSeleccionada == 1) {
            manejarInputIzquierda();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && opcionSeleccionada == 1) {
            manejarInputDerecha();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (opcionSeleccionada == 0) {
                juego.setScreen(new PantallaJuego(juego, tipoJuegoLibre, estadoPartida, estadoFisico));
            } else if (opcionSeleccionada == 2) {
                ManejoDeAudio.activarMusica("audio/musica/musica_menu.mp3", true);
                juego.setScreen(new MenuPrincipal(juego));
            }
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void actualizarColores() {
        seguir.setColor(opcionSeleccionada == 0 ? Color.RED : Color.WHITE);
        volumenSonido.setColor(opcionSeleccionada == 1 ? Color.RED : Color.WHITE);
        salir.setColor(opcionSeleccionada == 2 ? Color.RED : Color.WHITE);
    }

    private void manejarInputIzquierda() {
        if (volumenSonidoValor > 0) {
            volumenSonidoValor--;
            if (volumenSonidoValor == 0) ManejoDeAudio.setSonidoActivado(false);
            volumenSonido.setText("Volumen de sonido: " + volumenSonidoValor);
            ManejoDeAudio.setVolumenSonido(volumenSonidoValor);
        }
    }

    private void manejarInputDerecha() {
        if (volumenSonidoValor < 100) {
            if (volumenSonidoValor == 0) ManejoDeAudio.setSonidoActivado(true);
            volumenSonidoValor++;
            volumenSonido.setText("Volumen de sonido: " + volumenSonidoValor);
            ManejoDeAudio.setVolumenSonido(volumenSonidoValor);
        }
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
