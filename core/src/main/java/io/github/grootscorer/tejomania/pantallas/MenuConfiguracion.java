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
    Label titulo;

    private final int[][] resoluciones = {
        {640, 480},
        {800, 600},
        {1280, 720},
        {1920, 1080}
    };
    private int indiceResolucion = 0;

    private String rutaRelativaSonido = "audio/sonidos/sonido_seleccion.mp3";
    private String rutaAbsolutaSonido = Gdx.files.internal(rutaRelativaSonido).file().getAbsolutePath();

    public MenuConfiguracion(Principal juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(true);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        String rutaRelativaSkin = "ui/uiskin.json";
        String rutaAbsolutaSkin = Gdx.files.internal(rutaRelativaSkin).file().getAbsolutePath();
        skin = new Skin(Gdx.files.internal(rutaAbsolutaSkin));

        indiceResolucion = buscarIndice();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        titulo = new Label("Configuracion", skin, "default");
        titulo.setFontScale(3f);
        table.add(titulo).padBottom(40).row();

        opciones = new Label[4];
        opciones[0] = new Label("Volumen de sonido: " + volumenSonido, skin);
        opciones[0].setColor(Color.RED);
        opciones[1] = new Label("Volumen de musica: " + volumenMusica, skin);
        opciones[2] = new Label("Resolucion: " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight(), skin);
        opciones[3] = new Label("Salir", skin);

        for (Label opcion : opciones) {
            table.add(opcion).pad(10);
            table.row();
        }

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcionActual = (opcionActual - 1 + opciones.length) % opciones.length;
            actualizarSeleccion();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcionActual = (opcionActual + 1) % opciones.length;
            actualizarSeleccion();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && opcionActual != 2) {
            manejarInputIzquierda();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && opcionActual == 2) {
            manejarInputIzquierdaResolucion();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && opcionActual != 2) {
            manejarInputDerecha();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && opcionActual == 2) {
            manejarInputDerechaResolucion();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarInputEnter();
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        float escalaX = (float) width / 640f;
        float escalaY = (float) height / 480f;
        float escalaFuente = Math.max(escalaX, escalaY);

        for (Label opcion : opciones) {
            opcion.setFontScale(1.5f * escalaFuente);
        }

        titulo.setFontScale(3f * escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void actualizarSeleccion() {
        for (int i = 0; i < opciones.length; i++) {
            opciones[i].setColor(i == opcionActual ? Color.RED : Color.WHITE);
        }
        ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal(rutaAbsolutaSonido)));
    }

    private void manejarInputEnter() {
        if (opcionActual == 3) {
            juego.setScreen(new MenuPrincipal(juego));
        }
    }

    private void manejarInputIzquierda() {
        if (opcionActual == 0 && volumenSonido > 0) {
            volumenSonido--;
            if (volumenSonido == 0) ManejoDeAudio.setSonidoActivado(false);
            opciones[0].setText("Volumen de sonido: " + volumenSonido);
            ManejoDeAudio.setVolumenSonido(volumenSonido);
        } else if (opcionActual == 1 && volumenMusica > 0) {
            volumenMusica--;
            if (volumenMusica == 0) ManejoDeAudio.setMusicaActivada(false);
            opciones[1].setText("Volumen de musica: " + volumenMusica);
            ManejoDeAudio.setVolumenMusica(volumenMusica);
        }
    }

    private void manejarInputIzquierdaResolucion() {
        indiceResolucion = (indiceResolucion - 1 + resoluciones.length) % resoluciones.length;
        cambiarResolucion();
    }

    private void manejarInputDerecha() {
        if (opcionActual == 0 && volumenSonido < 100) {
            if (volumenSonido == 0) ManejoDeAudio.setSonidoActivado(true);
            volumenSonido++;
            opciones[0].setText("Volumen de sonido: " + volumenSonido);
            ManejoDeAudio.setVolumenSonido(volumenSonido);
        } else if (opcionActual == 1 && volumenMusica < 100) {
            volumenMusica++;
            if (volumenMusica == 1) {
                ManejoDeAudio.setMusicaActivada(true);
                ManejoDeAudio.reactivarMusica();
            }
            opciones[1].setText("Volumen de musica: " + volumenMusica);
            ManejoDeAudio.setVolumenMusica(volumenMusica);
        }
    }

    private void manejarInputDerechaResolucion() {
        indiceResolucion = (indiceResolucion + 1) % resoluciones.length;
        cambiarResolucion();
    }

    private void cambiarResolucion() {
        int ancho = resoluciones[indiceResolucion][0];
        int altura = resoluciones[indiceResolucion][1];
        Gdx.graphics.setWindowedMode(ancho, altura);
        stage.getViewport().update(ancho, altura, true);
        opciones[2].setText("Resolucion: " + ancho + "x" + altura);
        ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal(rutaAbsolutaSonido)));
    }

    private int buscarIndice() {
        int i = 0;
        int ancho = Gdx.graphics.getWidth();
        int altura = Gdx.graphics.getHeight();

        do {
            if(ancho == resoluciones[i][0] && altura == resoluciones[i][1]) {
                return i;
            }

            i++;
        }   while(i < resoluciones.length);

        return 0;
    }
}
