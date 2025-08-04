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
    private boolean pausaGol = false;
    private float tiempoPausaGol = 0;
    private final float DURACION_PAUSA_GOL = 1.0f;

    private boolean juegoTerminado = false;
    private float tiempoMostrandoResultado = 0;
    private final float DURACION_MOSTRAR_RESULTADO = 3.0f;
    private Label labelGanador;

    private SpriteBatch batch;

    private final Texture mazoRojo = new Texture(Gdx.files.internal("imagenes/sprites/mazo_rojo.png"));
    private final Texture mazoAzul = new Texture(Gdx.files.internal("imagenes/sprites/mazo_azul.png"));

    private final Texture spritesheetMazoRojo = new Texture(Gdx.files.internal("imagenes/sprites/spritesheet_mazo_rojo.png"));
    private final Texture spritesheetMazoAzul = new Texture(Gdx.files.internal("imagenes/sprites/spritesheet_mazo_azul.png"));

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
        mazo1.setSpritesheet(spritesheetMazoAzul);

        mazo2.setTextura(mazoRojo);
        mazo2.setSpritesheet(spritesheetMazoRojo);

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
        if (estaPausado || juegoTerminado) {
            tiempoPausa += delta;
            Gdx.gl.glClearColor(BRILLO_PAUSA, BRILLO_PAUSA, BRILLO_PAUSA, 1);
        } else {
            tiempoPausa = 0;
            Gdx.gl.glClearColor(0, 0, 0, 1);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (pausaGol) {
            tiempoPausaGol += delta;
            if (tiempoPausaGol >= DURACION_PAUSA_GOL) {
                pausaGol = false;
                tiempoPausaGol = 0;
            }
        }

        if (!estaPausado && !pausaGol && !juegoTerminado) {
            estadoPartida.actualizarTiempo(delta);
            manejoDeInput.actualizarMovimiento();
            barraEspecial1.aumentarCantidadLlenada();
            barraEspecial2.aumentarCantidadLlenada();

            mazo1.actualizarAnimacion(delta);
            mazo2.actualizarAnimacion(delta);

            if (disco.colisionaConMazo(mazo1)) {
                disco.manejarColision(mazo1);
                if (disco.isCambioDePosesion()) {
                    mazo1.activarEncendido();
                    disco.resetearCambioDePosesion();
                }
            }

            if (disco.colisionaConMazo(mazo2)) {
                disco.manejarColision(mazo2);
                if (disco.isCambioDePosesion()) {
                    mazo2.activarEncendido();
                    disco.resetearCambioDePosesion();
                }
            }

            disco.actualizarPosicion(delta, xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO);

            verificarGoles();
            verificarFinDeJuego();

            encabezadoPartida.render(delta);
        }

        if (juegoTerminado) {
            tiempoMostrandoResultado += delta;
            if (tiempoMostrandoResultado >= DURACION_MOSTRAR_RESULTADO) {
                juego.setScreen(new MenuPrincipal(juego));
                return;
            }
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO);
        shapeRenderer.end();

        dibujarLineasCancha();

        batch.begin();
        mazo1.dibujarConTextura(batch);
        mazo2.dibujarConTextura(batch);
        disco.dibujarConTextura(batch);
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (!juegoTerminado && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            estaPausado = !estaPausado;
            if (estaPausado) {
                juego.setScreen(new MenuPausa(juego, tipoJuegoLibre, estadoPartida));
            }
        }
    }

    private void dibujarLineasCancha() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        float grosorLinea = Math.max(1f, 2f * Math.min(escalaX, escalaY));
        Gdx.gl.glLineWidth(grosorLinea);

        float mitadCanchaX = xCancha + CANCHA_ANCHO / 2f;
        shapeRenderer.line(mitadCanchaX, yCancha, mitadCanchaX, yCancha + CANCHA_ALTO);

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        float radioSemicirculo = CANCHA_ALTO / 6f;

        float centroIzquierdoX = xCancha;
        float centroIzquierdoY = yCancha + CANCHA_ALTO / 2f;

        int segmentos = Math.max(16, (int)(32 * Math.min(escalaX, escalaY)));
        for (int i = 0; i < segmentos; i++) {
            float angulo1 = (float) (-Math.PI / 2 + (Math.PI * i) / segmentos);
            float angulo2 = (float) (-Math.PI / 2 + (Math.PI * (i + 1)) / segmentos);

            float x1 = centroIzquierdoX + radioSemicirculo * (float) Math.cos(angulo1);
            float y1 = centroIzquierdoY + radioSemicirculo * (float) Math.sin(angulo1);
            float x2 = centroIzquierdoX + radioSemicirculo * (float) Math.cos(angulo2);
            float y2 = centroIzquierdoY + radioSemicirculo * (float) Math.sin(angulo2);

            shapeRenderer.line(x1, y1, x2, y2);
        }

        float centroDerechoX = xCancha + CANCHA_ANCHO;
        float centroDerechoY = yCancha + CANCHA_ALTO / 2f;

        for (int i = 0; i < segmentos; i++) {
            float angulo1 = (float) (Math.PI / 2 + (Math.PI * i) / segmentos);
            float angulo2 = (float) (Math.PI / 2 + (Math.PI * (i + 1)) / segmentos);

            float x1 = centroDerechoX + radioSemicirculo * (float) Math.cos(angulo1);
            float y1 = centroDerechoY + radioSemicirculo * (float) Math.sin(angulo1);
            float x2 = centroDerechoX + radioSemicirculo * (float) Math.cos(angulo2);
            float y2 = centroDerechoY + radioSemicirculo * (float) Math.sin(angulo2);

            shapeRenderer.line(x1, y1, x2, y2);
        }

        shapeRenderer.end();

        Gdx.gl.glLineWidth(1f);
    }

    private void verificarGoles() {
        float radioSemicirculo = CANCHA_ALTO / 6f;
        float centroSemicirculoY = yCancha + CANCHA_ALTO / 2f;

        float limiteInferiorGol = centroSemicirculoY - radioSemicirculo;
        float limiteSuperiorGol = centroSemicirculoY + radioSemicirculo;

        boolean discoEnAreaVerticalGol = (disco.getPosicionY() + disco.getRadioDisco() >= limiteInferiorGol) &&
            (disco.getPosicionY() + disco.getRadioDisco() <= limiteSuperiorGol);

        if (disco.getPosicionX() + disco.getRadioDisco() * 2 < xCancha) {
            if (discoEnAreaVerticalGol) {
                anotarGol(2);
            } else {
                anotarGol(2);
            }
        }
        else if (disco.getPosicionX() > xCancha + CANCHA_ANCHO) {
            if (discoEnAreaVerticalGol) {
                anotarGol(1);
            } else {
                anotarGol(1);
            }
        }
    }

    private void verificarFinDeJuego() {
        boolean finPorTiempo = estadoPartida.isJugandoPorTiempo() && estadoPartida.getTiempoRestante() <= 0;
        boolean finPorPuntaje = estadoPartida.isJugandoPorPuntaje() &&
            (estadoPartida.getPuntaje1() >= estadoPartida.getPuntajeGanador() ||
                estadoPartida.getPuntaje2() >= estadoPartida.getPuntajeGanador());

        if (finPorTiempo || finPorPuntaje) {
            mostrarResultado();
        }
    }

    private void mostrarResultado() {
        if (!juegoTerminado) {
            juegoTerminado = true;
            tiempoMostrandoResultado = 0;

            String textoGanador;
            if (estadoPartida.getPuntaje1() > estadoPartida.getPuntaje2()) {
                textoGanador = estadoPartida.getJugador1() + " GANA!";
            } else if (estadoPartida.getPuntaje2() > estadoPartida.getPuntaje1()) {
                textoGanador = estadoPartida.getJugador2() + " GANA!";
            } else {
                textoGanador = "EMPATE!";
            }

            labelGanador = new Label(textoGanador, skin, "default");
            labelGanador.setColor(Color.RED);
            labelGanador.setFontScale(escalaFuente * 4.0f);

            labelGanador.pack();

            labelGanador.setPosition(Gdx.graphics.getWidth() / 2f, 100
            );

            stage.addActor(labelGanador);
        }
    }

    private void anotarGol(int jugadorQueAnota) {
        if (jugadorQueAnota == 1) {
            estadoPartida.agregarGolJugador1();
        } else {
            estadoPartida.agregarGolJugador2();
        }

        reiniciarPosicionesTrasGol();

        pausaGol = true;
        tiempoPausaGol = 0;
    }

    private void reiniciarPosicionesTrasGol() {
        disco.setPosicion(xCancha + CANCHA_ANCHO / 2f - disco.getRadioDisco(),
            yCancha + CANCHA_ALTO / 2f - disco.getRadioDisco());
        disco.setVelocidadX(0);
        disco.setVelocidadY(0);

        disco.reiniciarPosesion();

        float offsetMazos = 50 * Math.min(escalaX, escalaY);

        mazo1.setPosicion((int)(xCancha + offsetMazos - mazo1.getRadioMazo()),
            (int)(yCancha + CANCHA_ALTO / 2f - mazo1.getRadioMazo()));
        mazo1.setVelocidadX(0);
        mazo1.setVelocidadY(0);

        mazo2.setPosicion((int)(xCancha + CANCHA_ANCHO - offsetMazos - mazo2.getRadioMazo()),
            (int)(yCancha + CANCHA_ALTO / 2f - mazo2.getRadioMazo()));
        mazo2.setVelocidadX(0);
        mazo2.setVelocidadY(0);
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
        mazoAzul.dispose();
        spritesheetMazoRojo.dispose();
        spritesheetMazoAzul.dispose();
    }
}
