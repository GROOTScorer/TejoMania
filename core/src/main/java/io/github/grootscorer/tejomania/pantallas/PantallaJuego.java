package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
import io.github.grootscorer.tejomania.entidades.Disco;
import io.github.grootscorer.tejomania.entidades.Jugador;
import io.github.grootscorer.tejomania.entidades.Mazo;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.hud.BarraEspecial;
import io.github.grootscorer.tejomania.hud.EncabezadoPartida;
import io.github.grootscorer.tejomania.utiles.ManejoDeInput;

public class PantallaJuego extends ScreenAdapter {
    private Stage stage;
    private Principal juego;
    private Disco disco;
    private Mazo mazo1, mazo2;
    private Jugador jugador1, jugador2;
    private TipoJuegoLibre tipoJuegoLibre;
    private Skin skin;
    private BarraEspecial barraEspecial1, barraEspecial2;
    private EncabezadoPartida encabezadoPartida;
    private Texture texturaCancha;
    private Image imagenCancha;
    private ShapeRenderer shapeRenderer;
    private EstadoPartida estadoPartida;
    private ManejoDeInput manejoDeInput;
    private boolean estaPausado = false;
    private float tiempoPausa = 0;

    private SpriteBatch batch;

    private final Texture mazoRojo = new Texture(Gdx.files.internal("imagenes/sprites/mazo_rojo.png"));
    private final Texture mazoRojoEncendido = new Texture(Gdx.files.internal("imagenes/sprites/mazo_rojo_encendido.png"));
    private final Texture mazoAzul = new Texture(Gdx.files.internal("imagenes/sprites/mazo_azul.png"));
    private final Texture mazoAzulEncendido = new Texture(Gdx.files.internal("imagenes/sprites/mazo_azul_encendido.png"));

    private final float BRILLO_NORMAL = 1.0f;
    private final float BRILLO_PAUSA = 0.5f;

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    private final int CANCHA_ANCHO = (int) (540 * escalaX);
    private final int CANCHA_ALTO = (int) (320 * escalaY);

    float xCancha = (Gdx.graphics.getWidth() - CANCHA_ANCHO) / 2f;
    float yCancha = (Gdx.graphics.getHeight() - CANCHA_ALTO) / 2f;

    public PantallaJuego(Principal juego, TipoJuegoLibre tipoJuegoLibre, EstadoPartida estadoPartida) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.estadoPartida = estadoPartida;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        disco = new Disco();
        mazo1 = new Mazo();
        mazo2 = new Mazo();

        mazo1.setTextura(mazoAzul);
        mazo2.setTextura(mazoRojo);

        manejoDeInput = new ManejoDeInput(mazo1, mazo2, tipoJuegoLibre, xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, manejoDeInput));

        encabezadoPartida = new EncabezadoPartida(estadoPartida);
        encabezadoPartida.show();
        Stage encabezadoStage = encabezadoPartida.getStage();
        for (Actor actor : encabezadoStage.getActors()) {
            stage.addActor(actor);
        }

        barraEspecial1 = new BarraEspecial(0, 100, 1, false, skin);
        barraEspecial2 = new BarraEspecial(0, 100, 1, false, skin);

        barraEspecial1.setPosition(xCancha, yCancha - 30);
        barraEspecial2.setPosition(xCancha + CANCHA_ANCHO - barraEspecial2.getWidth(), yCancha - 30);

        stage.addActor(barraEspecial1);
        stage.addActor(barraEspecial2);
    }

    @Override
    public void render(float delta) {
        if (estaPausado) {
            tiempoPausa += delta;
            Gdx.gl.glClearColor(BRILLO_PAUSA, BRILLO_PAUSA, BRILLO_PAUSA, 1);
        } else {
            tiempoPausa = 0;
            Gdx.gl.glClearColor(0, 0, 0, 1);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!estaPausado) {
            estadoPartida.actualizarTiempo(delta);
            manejoDeInput.actualizarMovimiento();
            barraEspecial1.aumentarCantidadLlenada();
            barraEspecial2.aumentarCantidadLlenada();

            if (disco.colisionaConMazo(mazo1)) {
                disco.manejarColision(mazo1);
            }

            if (disco.colisionaConMazo(mazo2)) {
                disco.manejarColision(mazo2);
            }

            disco.actualizarPosicion(delta, xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO);

            encabezadoPartida.render(delta);
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO);
        shapeRenderer.end();

        batch.begin();
        mazo1.dibujarConTextura(batch);
        mazo2.dibujarConTextura(batch);
        disco.dibujarConTextura(batch);
        batch.end();

        if (estadoPartida.isJugandoPorTiempo() && estadoPartida.getTiempoRestante() <= 0) {
            anunciarResultado();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            estaPausado = !estaPausado;
            if (estaPausado) {
                juego.setScreen(new MenuPausa(juego, tipoJuegoLibre, estadoPartida));
            }
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        disco.dispose();
        mazoRojo.dispose();
        mazoRojoEncendido.dispose();
        mazoAzul.dispose();
        mazoAzulEncendido.dispose();
    }

    private void anunciarResultado() {
        Label anuncioResultado;
        if(estadoPartida.getPuntaje1() > estadoPartida.getPuntaje2()) {
            anuncioResultado = new Label(jugador1.getNombre() + " gana", skin, "default");
        } else if(estadoPartida.getPuntaje2() > estadoPartida.getPuntaje1()) {
            anuncioResultado = new Label(jugador2.getNombre() + " gana", skin, "default");
        } else {
            anuncioResultado = new Label("Empate", skin, "default");
        }
    }
}
