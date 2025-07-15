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
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

public class MenuConfiguracion extends ScreenAdapter {

    private Stage stage;
    private Skin skin;
    private final Principal juego;
    private int opcionActual = 0;
    private int volumenSonido = (int) (ManejoDeAudio.getVolumenSonido() * 100);
    private int volumenMusica = (int) (ManejoDeAudio.getVolumenMusica() * 100);
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

        Label titulo = new Label("Configuracion", skin, "default");
        titulo.setFontScale(3f);

        table.add(titulo).padBottom(40).row();

        opciones = new Label[3];
        opciones[0] = new Label("Volumen de sonido: " + volumenSonido, skin, "default");
        opciones[0].setColor(Color.RED);
        opciones[1] = new Label("Volumen de musica: " + volumenMusica, skin, "default");
        opciones[2] = new Label("Salir", skin, "default");

        for(Label opcion : opciones) {
            opcion.setFontScale(1.5f);
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
        ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("audio/sonidos/sonido_seleccion.mp3")));
    }

    private void manejarInputEnter() {
        if(opcionActual == 2) {
            juego.setScreen(new MenuPrincipal(juego));
        }
    }

    private void manejarInputIzquierda() {
        if (opcionActual == 0) {
            if (volumenSonido > 0) {
                volumenSonido--;
                if (volumenSonido == 0) {
                    ManejoDeAudio.setSonidoActivado(false);
                }
            }
            opciones[0].setText("Volumen de sonido: " + volumenSonido);
            ManejoDeAudio.setVolumenSonido(volumenSonido);
        }   else if(opcionActual == 1) {
            if(volumenMusica > 0) {
                volumenMusica--;
                if(volumenMusica == 0) {
                    ManejoDeAudio.setMusicaActivada(false);
                }
            }
            opciones[1].setText("Volumen de musica: " + volumenMusica);
            ManejoDeAudio.setVolumenMusica(volumenMusica);
        }
    }

    private void manejarInputDerecha() {
        if (opcionActual == 0) {
            if (volumenSonido >= 100) {
                volumenSonido = 100;
            } else {
                if (volumenSonido == 0) {
                    ManejoDeAudio.setSonidoActivado(true);
                }
                volumenSonido++;
            }
            opciones[0].setText("Volumen de sonido: " + volumenSonido);
            ManejoDeAudio.setVolumenSonido(volumenSonido);
        } else if (opcionActual == 1) {
            if (volumenMusica < 100) {
                volumenMusica++;

                if (volumenMusica == 1) {
                    ManejoDeAudio.setMusicaActivada(true);
                    ManejoDeAudio.reactivarMusica();
                }

                opciones[1].setText("Volumen de musica: " + volumenMusica);
                ManejoDeAudio.setVolumenMusica(volumenMusica);
                if (ManejoDeAudio.isMusicaActivada() && ManejoDeAudio.isReproduciendoMusica()) {
                    ManejoDeAudio.setVolumenMusica(volumenMusica);
                }
            }
        }

    }
}
