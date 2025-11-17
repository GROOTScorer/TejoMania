package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.enums.DificultadCPU;
import io.github.grootscorer.tejomania.enums.Pais;
import io.github.grootscorer.tejomania.enums.TipoCompetencia;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.estado.GestorTorneo;
import io.github.grootscorer.tejomania.estado.GestorLiga;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;
import io.github.grootscorer.tejomania.utiles.ManejoDeInput;

public class EleccionDificultadCompetencia extends ScreenAdapter {
    private final Principal juego;
    private EstadoPartida estadoPartida = new EstadoPartida();

    private Stage stage;
    private Skin skin;
    private boolean uiInicializada = false;
    private Label titulo;
    private Label valor;
    private DificultadCPU dificultad = DificultadCPU.FACIL;
    private Pais paisSeleccionado;
    private TipoCompetencia tipoCompetencia;

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    public EleccionDificultadCompetencia(Principal juego, Pais paisSeleccionado, TipoCompetencia tipoCompetencia) {
        this.juego = juego;
        this.paisSeleccionado = paisSeleccionado;
        this.tipoCompetencia = tipoCompetencia;
    }

    public void show() {
        Gdx.input.setCursorCatched(true);
        if (!uiInicializada) {
            crearInterfaz();
            uiInicializada = true;
        }
        actualizarValores();
        Gdx.input.setInputProcessor(stage);
    }

    private void crearInterfaz() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        titulo = new Label("Elegir dificultad", skin, "default");
        titulo.setFontScale(3f * escalaFuente);

        valor = new Label(dificultad.getNombre(), skin);
        valor.setFontScale(2f * escalaFuente);

        table.add(titulo).padBottom(50f).row();
        table.add(valor).padTop(20f).center();
    }


    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            manejarFlechaIzquierda();
            ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("audio/sonidos/sonido_seleccion.mp3")));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            manejarFlechaDerecha();
            ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("audio/sonidos/sonido_seleccion.mp3")));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarEnter();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            juego.setScreen(new MenuEleccionPais(juego, tipoCompetencia));
        }

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        valor.setFontScale(1.5f * escalaFuente);

        titulo.setFontScale(3f * escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void manejarEnter() {
        estadoPartida.setJugador1(paisSeleccionado.getnombre());
        estadoPartida.setDificultadCPU(dificultad);
        estadoPartida.setTiempoRestante(300);
        estadoPartida.setJugandoPorTiempo(true);
        estadoPartida.setJugandoPorPuntaje(false);
        estadoPartida.setJugarConModificadores(false);
        estadoPartida.setJugarConObstaculos(false);
        estadoPartida.setJugarConTirosEspeciales(false);
        estadoPartida.setTipoJuegoLibre(TipoJuegoLibre.CPU);
        estadoPartida.setCanchaSeleccionada("Cancha estandar");
        estadoPartida.setTipoCompetencia(tipoCompetencia);
        estadoPartida.setPaisSeleccionado(paisSeleccionado);

        if (tipoCompetencia == TipoCompetencia.TORNEO) {
            GestorTorneo gestorTorneo = new GestorTorneo(paisSeleccionado);
            estadoPartida.setGestorTorneo(gestorTorneo);
            estadoPartida.setFaseActual(0);

            Pais primerRival = gestorTorneo.seleccionarRivalAleatorio();
            estadoPartida.setRivalActual(primerRival);
            estadoPartida.setJugador2(primerRival.getnombre());
        }

        if (tipoCompetencia == TipoCompetencia.LIGA) {
            GestorLiga gestorLiga = new GestorLiga(paisSeleccionado);
            estadoPartida.setGestorLiga(gestorLiga);
            estadoPartida.setFechaLiga(0);

            Pais primerRival = gestorLiga.getRivalJugadorFecha(0);
            estadoPartida.setRivalActual(primerRival);
            estadoPartida.setJugador2(primerRival.getnombre());
        }

        juego.setScreen(new PantallaJuego(juego, TipoJuegoLibre.CPU, estadoPartida));
    }

    private void manejarFlechaIzquierda() {
        dificultad = dificultadAnterior(dificultad);
        actualizarValores();
    }

    private void manejarFlechaDerecha() {
        dificultad = siguienteDificultad(dificultad);
        actualizarValores();
    }

    private void actualizarValores() {
        valor.setText(dificultad.getNombre());
    }

    private DificultadCPU siguienteDificultad(DificultadCPU actual) {
        switch (actual) {
            case FACIL:
                return DificultadCPU.INTERMEDIO;
            case INTERMEDIO:
                return DificultadCPU.DIFICIL;
            case DIFICIL:
                return DificultadCPU.FACIL;
        }
        return actual;
    }

    private DificultadCPU dificultadAnterior(DificultadCPU actual) {
        switch (actual) {
            case FACIL:
                return DificultadCPU.DIFICIL;
            case INTERMEDIO:
                return DificultadCPU.FACIL;
            case DIFICIL:
                return DificultadCPU.INTERMEDIO;
        }
        return actual;
    }
}
