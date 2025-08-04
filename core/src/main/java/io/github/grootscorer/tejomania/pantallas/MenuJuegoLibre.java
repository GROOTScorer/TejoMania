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
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

public class MenuJuegoLibre extends ScreenAdapter {
    private Stage stage;
    private Skin skin;

    private final Principal juego;
    private EstadoPartida estadoPartida = new EstadoPartida();

    private int opcionActual = 0;
    private Label[] opciones;
    private Label textoDescripcion;

    public MenuJuegoLibre(Principal juego) {
        this.juego = juego;
    }

    public void show() {
        Gdx.input.setCursorCatched(true);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        opciones = new Label[4];

        opciones[0] = new Label("1 jugador", skin, "default");
        opciones[0].setColor(Color.RED);
        opciones[1] = new Label("2 jugadores", skin, "default");
        opciones[2] = new Label("Multijugador", skin, "default");
        opciones[2].setColor(Color.LIGHT_GRAY);
        opciones[3] = new Label("Volver", skin, "default");

        for(Label opcion: opciones) {
            opcion.setFontScale(1.5f);
            table.add(opcion).pad(20);
            table.row();
        }

        textoDescripcion = new Label("Juega contra la CPU", skin, "default");
        textoDescripcion.setFontScale(0.9f);
        textoDescripcion.setColor(Color.LIGHT_GRAY);
        table.add(textoDescripcion).padTop(20);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcionActual = (opcionActual - 1 + opciones.length) % opciones.length;
            actualizarSeleccion();
        }   else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcionActual = (opcionActual + 1) % opciones.length;
            actualizarSeleccion();
        }   else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarEnter();
        }

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        float escalaX = (float) width / 640f;
        float escalaY = (float) height / 480f;
        float escalaFuente = Math.max(escalaX, escalaY);

        for(Label opcion: opciones) {
            opcion.setFontScale(1.5f * escalaFuente);
        }

        textoDescripcion.setFontScale(0.9f * escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void actualizarSeleccion() {
        for(int i = 0; i < opciones.length; i++) {
            if(opcionActual == i) {
                if(opcionActual == 2) {
                    opciones[i].setColor(Color.GRAY);
                }   else {
                    opciones[i].setColor(Color.RED);
                }
            }   else {
                if(i == 2) {
                    opciones[i].setColor(Color.LIGHT_GRAY);
                }   else {
                    opciones[i].setColor(Color.WHITE);
                }
            }
        }
        ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("audio/sonidos/sonido_seleccion.mp3")));

        switch(opcionActual) {
            case 0:
                textoDescripcion.setText("Juega contra la CPU");
                break;
            case 1:
                textoDescripcion.setText("Juega contra otro jugador en este dispositivo");
                break;
            case 2:
                textoDescripcion.setText("Juega contra otro jugador en linea (proximamente...)");
                break;
            default:
                textoDescripcion.setText(" ");
                break;
        }
    }

    private void manejarEnter() {
        switch(opcionActual) {
            case 0:
                estadoPartida.setTipoJuegoLibre(TipoJuegoLibre.CPU);
                juego.setScreen(new MenuOpcionesJuego(juego, TipoJuegoLibre.CPU, estadoPartida));
                break;
            case 1:
                estadoPartida.setTipoJuegoLibre(TipoJuegoLibre.DOS_JUGADORES);
                juego.setScreen(new MenuOpcionesJuego(juego, TipoJuegoLibre.DOS_JUGADORES, estadoPartida));
                break;
            case 3:
                juego.setScreen(new MenuModoJuego(juego));
                break;
            default:
                break;
        }
    }
}
