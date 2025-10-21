package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;

public class EleccionCancha extends ScreenAdapter {
    private Principal juego;
    private Stage stage;
    private Skin skin;
    private Label nombreCancha;
    private Label escParaVolver;
    private Label flechaIzquierda, flechaDerecha;
    private Texture texturaCancha, texturaCanchaAnterior, texturaCanchaSiguiente;
    private Image imagenCancha, imagenCanchaAnterior, imagenCanchaSiguiente;
    private String CanchasDisponibles[] = {"Cancha estandar", "Cancha estanque", "Cancha marron"};
    private String imagenesCanchas[] = {"imagenes/canchas/cancha_estandar.png", "imagenes/canchas/cancha_estanque.png", "imagenes/canchas/cancha_marron.png"};
    private int opcionCancha = 0;
    private TipoJuegoLibre tipoJuegoLibre;
    private EstadoPartida estadoPartida;

    public EleccionCancha(Principal juego, TipoJuegoLibre tipoJuegoLibre, EstadoPartida estadoPartida) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.estadoPartida = estadoPartida;
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(true);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        nombreCancha = new Label("", skin);
        nombreCancha.setFontScale(2f);

        escParaVolver = new Label("Presiona Esc para volver", skin);
        escParaVolver.setColor(Color.GRAY);

        flechaIzquierda = new Label("<", skin);
        flechaIzquierda.setFontScale(3f);

        flechaDerecha = new Label(">", skin);
        flechaDerecha.setFontScale(3f);

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        float alturaPrincipal = Gdx.graphics.getHeight() * 0.15f;
        float alturaSecundaria = Gdx.graphics.getHeight() * 0.1f;

        imagenCanchaAnterior = new Image();
        imagenCanchaSiguiente = new Image();
        imagenCancha = new Image();

        imagenCancha.setSize(alturaPrincipal * (200f / 120f), alturaPrincipal);
        imagenCanchaAnterior.setSize(alturaSecundaria * (200f / 120f), alturaSecundaria);
        imagenCanchaSiguiente.setSize(alturaSecundaria * (200f / 120f), alturaSecundaria);

        if(Gdx.graphics.getHeight() < 700) {
            tabla.add(imagenCanchaAnterior).padLeft(10).padRight(20).height(alturaSecundaria * (200f / 120f)).maxWidth(900);
        } else {
            tabla.add(imagenCanchaAnterior).padLeft(10).padRight(20).height(alturaSecundaria * (200f / 120f)).maxWidth(250);
        }

        tabla.add(flechaIzquierda).padRight(20);

        if(Gdx.graphics.getHeight() < 700) {
            tabla.add(imagenCancha).padRight(20).height(alturaPrincipal * (200f / 120f));
        } else {
            tabla.add(imagenCancha).padRight(20).height(alturaPrincipal * (200f / 120f)).maxWidth(300);
        }

        tabla.add(flechaDerecha).padRight(20);

        if(Gdx.graphics.getHeight() < 700) {
            tabla.add(imagenCanchaSiguiente).height(alturaSecundaria * (200f / 120f)).padRight(10).maxWidth(900);
        } else {
            tabla.add(imagenCanchaSiguiente).height(alturaSecundaria * (200f / 120f)).padRight(10).maxWidth(250);
        }

        tabla.row();
        tabla.add(nombreCancha).colspan(5).center().padTop(20);
        tabla.row();
        tabla.add(escParaVolver).colspan(5).center().padTop(100);

        stage.addActor(tabla);

        actualizarCancha();
    }


    private void actualizarCancha() {

        String seleccionado = CanchasDisponibles[opcionCancha];
        String anterior = CanchasDisponibles[(opcionCancha - 1 + 3) % 3];
        String siguiente = CanchasDisponibles[(opcionCancha + 1) % 3];

        nombreCancha.setText(seleccionado);

        if (texturaCancha != null) texturaCancha.dispose();
        if (texturaCanchaAnterior != null) texturaCanchaAnterior.dispose();
        if (texturaCanchaSiguiente != null) texturaCanchaSiguiente.dispose();

        texturaCancha = new Texture(Gdx.files.internal(imagenesCanchas[opcionCancha]));
        texturaCanchaAnterior = new Texture(Gdx.files.internal(imagenesCanchas[(opcionCancha - 1 + 3) % 3]));
        texturaCanchaSiguiente = new Texture(Gdx.files.internal(imagenesCanchas[(opcionCancha + 1) % 3]));

        imagenCancha.setDrawable(new Image(texturaCancha).getDrawable());
        imagenCanchaAnterior.setDrawable(new Image(texturaCanchaAnterior).getDrawable());
        imagenCanchaSiguiente.setDrawable(new Image(texturaCanchaSiguiente).getDrawable());

        float OPACIDAD_CANCHA_SECUNDARIO = 0.5f;
        imagenCanchaAnterior.getColor().a = OPACIDAD_CANCHA_SECUNDARIO;
        imagenCanchaSiguiente.getColor().a = OPACIDAD_CANCHA_SECUNDARIO;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        manejarEntrada();

        stage.act(delta);
        stage.draw();
    }

    private void manejarEntrada() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            opcionCancha = (opcionCancha + 1) % 3;
            actualizarCancha();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            opcionCancha = (opcionCancha - 1 + 3) % 3;
            actualizarCancha();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            String CanchaSeleccionado = CanchasDisponibles[opcionCancha];
            estadoPartida.setCanchaSeleccionada(CanchaSeleccionado);
            juego.setScreen(new EleccionNombre(juego, tipoJuegoLibre, estadoPartida, CanchasDisponibles[opcionCancha]));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            juego.setScreen(new MenuModoJuego(juego));
        }
    }

    @Override
    public void resize(int width, int height) {
        float escalaX = (float) width / 640f;
        float escalaY = (float) height / 480f;
        float escalaFuente = Math.max(escalaX, escalaY);

        nombreCancha.setFontScale(2f * escalaFuente);
        flechaIzquierda.setFontScale(3f * escalaFuente);
        flechaDerecha.setFontScale(3f * escalaFuente);
        escParaVolver.setFontScale(escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (texturaCancha != null) texturaCancha.dispose();
        if (texturaCanchaAnterior != null) texturaCanchaAnterior.dispose();
        if (texturaCanchaSiguiente != null) texturaCanchaSiguiente.dispose();
        stage.dispose();
        skin.dispose();
    }
}



