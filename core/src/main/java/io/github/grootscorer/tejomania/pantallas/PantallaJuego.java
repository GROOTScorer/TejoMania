package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.entidades.Jugador;
import io.github.grootscorer.tejomania.entidades.Obstaculo;
import io.github.grootscorer.tejomania.entidades.modificadores.CongelarRival;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.hud.BarraEspecial;
import io.github.grootscorer.tejomania.hud.EncabezadoPartida;

public class PantallaJuego extends ScreenAdapter {
    private Stage stage;
    private Principal juego;
    private Jugador jugador1, jugador2;
    private TipoJuegoLibre tipoJuegoLibre;
    private Skin skin;
    private Obstaculo obstaculo;
    private CongelarRival congelarRival;
    private BarraEspecial barraEspecial1, barraEspecial2;
    private EncabezadoPartida encabezadoPartida;
    private Texture texturaCancha;
    private Image imagenCancha;
    private ShapeRenderer shapeRenderer;
    private EstadoPartida estadoPartida;

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    private final int CANCHA_ANCHO = (int) (540 * escalaX);
    private final int CANCHA_ALTO = (int) (320 * escalaY);

    float xCancha = (Gdx.graphics.getWidth() - CANCHA_ANCHO) / 2f;
    float yCancha = (Gdx.graphics.getHeight() - CANCHA_ALTO) / 2f;

    private SpriteBatch batch;

    public PantallaJuego(Principal juego, TipoJuegoLibre tipoJuegoLibre, EstadoPartida estadoPartida) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.estadoPartida = estadoPartida;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        encabezadoPartida = new EncabezadoPartida(estadoPartida);
        encabezadoPartida.show();
        Stage encabezadoStage = encabezadoPartida.getStage();
        for (Actor actor : encabezadoStage.getActors()) {
            stage.addActor(actor);
        }
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.end();

        encabezadoPartida.render(delta);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO);
        shapeRenderer.end();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void anunciarResultado() {
        Label anuncioResultado;
        if(estadoPartida.getPuntaje1() > estadoPartida.getPuntaje2()) {
            anuncioResultado = new Label(jugador1.getNombre() + " gana", skin, "default");
        }   else if(estadoPartida.getPuntaje2() > estadoPartida.getPuntaje1()) {
            anuncioResultado = new Label(jugador2.getNombre() + " gana", skin, "default");
        }   else {
            anuncioResultado = new Label("Empate", skin, "default");
        }
    }
}
