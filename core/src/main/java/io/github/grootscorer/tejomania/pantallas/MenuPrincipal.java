package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.Texture;
import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

public class MenuPrincipal extends ScreenAdapter {

    private Stage stage;
    private Skin skin;
    private final Principal juego;
    private int opcionActual = 0;
    private Label[] opciones;

    public MenuPrincipal(Principal juego) {
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
        stage.addActor(table);

        Label titulo = new Label("TEJOMANIA", skin, "default");
        titulo.setFontScale(3f);

        opciones = new Label[3];

        opciones[0] = new Label("Jugar", skin, "default");
        opciones[0].setColor(Color.RED);
        opciones[1] = new Label("Configuracion", skin, "default");
        opciones[2] = new Label("Salir", skin, "default");

        Texture texturaGithub = new Texture(Gdx.files.internal("imagenes/github_logo.png"));
        Image logoGithub = new Image(texturaGithub);
        Label indicadorTeclaGithub1 = new Label("Presione 'G' para", skin, "default");
        Label indicadorTeclaGithub2 = new Label("acceder al repositorio", skin, "default");
        indicadorTeclaGithub1.setColor(Color.LIGHT_GRAY);
        indicadorTeclaGithub2.setColor(Color.LIGHT_GRAY);

        table.add(titulo).padBottom(40).row();

        for(Label opcion: opciones) {
            opcion.setFontScale(1.5f);
            table.add(opcion).pad(20);
            table.row();
        }

        table.add(logoGithub).padLeft(400).padTop(50).width(50).height(50);
        table.row();
        table.add(indicadorTeclaGithub1).padLeft(400);
        table.row();
        table.add(indicadorTeclaGithub2).padLeft(400);
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
        }   else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarEnter();
        }   else if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            Gdx.net.openURI("https://github.com/GROOTScorer/TejoMania");
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
        ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("sonidos/sonido_seleccion.wav")));
    }

    private void manejarEnter() {
        switch(opcionActual) {
            case 1:
                juego.setScreen(new MenuConfiguracion(juego));
                break;
            case 2:
                Gdx.app.exit();
                break;
            default:
                break;
        }
    }
}
