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
import io.github.grootscorer.tejomania.enums.Pais;
import io.github.grootscorer.tejomania.enums.TipoCompetencia;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;

import com.badlogic.gdx.graphics.Color;
import io.github.grootscorer.tejomania.estado.GestorLiga;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

import java.util.List;
import java.util.Map;

public class PostPartidoCompetencia extends ScreenAdapter {
    private Principal juego;
    private Stage stage;
    private Skin skin;
    private TipoCompetencia tipoCompetencia;
    private EstadoPartida estadoPartida;
    private int resultadoTorneo;
    private Label mensaje;
    private Table tablaLiga;

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    public PostPartidoCompetencia(Principal juego, TipoCompetencia tipoCompetencia, int resultado, EstadoPartida estadoPartida) {
        this.juego = juego;
        this.tipoCompetencia = tipoCompetencia;
        if(tipoCompetencia == TipoCompetencia.TORNEO) {
            this.resultadoTorneo = resultado;
        }

        this.estadoPartida = estadoPartida;
    }

    public void show() {
        stage = new Stage(new ScreenViewport());
        String rutaRelativaSkin = "ui/uiskin.json";
        String rutaAbsolutaSkin = Gdx.files.internal(rutaRelativaSkin).file().getAbsolutePath();
        skin = new Skin(Gdx.files.internal(rutaAbsolutaSkin));

        Gdx.input.setCursorCatched(true);

        mensaje = new Label("", skin);
        mensaje.setFontScale(2f * escalaFuente);

        if(tipoCompetencia == TipoCompetencia.LIGA) {
            mensaje.setText("Tabla de Posiciones - Fecha " + (estadoPartida.getFechaLiga() + 1));
            crearTablaLiga();
        }

        if(resultadoTorneo == 1 && tipoCompetencia == TipoCompetencia.TORNEO) {
            if(estadoPartida.getFaseActual() < 3) {
                mensaje.setText("Felicitaciones! Has pasado a la fase: " + estadoPartida.getNombreFase(estadoPartida.getFaseActual() + 1));
            } else {
                mensaje.setText("Felicitaciones por ganar el torneo!");
            }
        } else if(resultadoTorneo == 2 && tipoCompetencia == TipoCompetencia.TORNEO) {
            mensaje.setText("Has sido eliminado del torneo");
        }

        mensaje.setPosition(
            (Gdx.graphics.getWidth() - mensaje.getPrefWidth()) / 2f,
            Gdx.graphics.getHeight() - 100
        );

        Table contenedor = new Table();

        contenedor.setFillParent(true);
        if(tipoCompetencia == TipoCompetencia.TORNEO) {
            contenedor.center();
        } else {
            contenedor.top().padTop(Gdx.graphics.getHeight() / 10f);
        }
        contenedor.add(mensaje).expandX().center().padTop(20);

        stage.addActor(contenedor);

        if (tablaLiga != null) {
            stage.addActor(tablaLiga);
        }

        Label instruccion = new Label("Presiona ENTER para continuar", skin);
        instruccion.setFontScale(escalaFuente);
        instruccion.setColor(Color.GRAY);
        instruccion.setPosition(
            (Gdx.graphics.getWidth() - instruccion.getPrefWidth()) / 2f,
            50
        );
        stage.addActor(instruccion);

        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarEnter();
        }

        stage.act(delta);
        stage.draw();
    }

    public void manejarEnter() {
        if(tipoCompetencia == TipoCompetencia.LIGA) {
            estadoPartida.setFechaLiga(estadoPartida.getFechaLiga() + 1);
            estadoPartida.getGestorLiga().avanzarFecha();

            if(estadoPartida.getFechaLiga() >= 9) {
                GestorLiga gestor = estadoPartida.getGestorLiga();
                int posicionFinal = gestor.getPosicionEquipo(estadoPartida.getPaisSelecionad());

                if (posicionFinal == 1) {
                    juego.setScreen(new PantallaResultadoFinalLiga(juego, estadoPartida, true));
                } else {
                    juego.setScreen(new PantallaResultadoFinalLiga(juego, estadoPartida, false));
                }
            } else {
                Pais siguienteRival = estadoPartida.getGestorLiga().getRivalJugadorFecha(estadoPartida.getFechaLiga());
                estadoPartida.setRivalActual(siguienteRival);
                estadoPartida.setJugador2(siguienteRival.getnombre());

                estadoPartida.setPuntaje1(0);
                estadoPartida.setPuntaje2(0);
                estadoPartida.setTiempoRestante(300);

                juego.setScreen(new PantallaJuego(juego, TipoJuegoLibre.CPU, estadoPartida));
            }
        }

        if(tipoCompetencia == TipoCompetencia.TORNEO) {
            if(resultadoTorneo == 1) {
                if(estadoPartida.getRivalActual() != null) {
                    estadoPartida.getGestorTorneo().eliminarEquipo(estadoPartida.getRivalActual());
                }

                if(estadoPartida.getFaseActual() >= 3) {
                    ManejoDeAudio.activarMusica("audio/musica/musica_menu.mp3", true);
                    juego.setScreen(new MenuPrincipal(juego));
                } else {
                    estadoPartida.setFaseActual(estadoPartida.getFaseActual() + 1);

                    Pais nuevoRival = estadoPartida.getGestorTorneo().seleccionarRivalAleatorio();
                    estadoPartida.setRivalActual(nuevoRival);
                    estadoPartida.setJugador2(nuevoRival.getnombre());

                    estadoPartida.setPuntaje1(0);
                    estadoPartida.setPuntaje2(0);
                    estadoPartida.setTiempoRestante(300);

                    juego.setScreen(new PantallaJuego(juego, TipoJuegoLibre.CPU, estadoPartida));
                }
            } else {
                ManejoDeAudio.activarMusica("audio/musica/musica_menu.mp3", true);
                juego.setScreen(new MenuPrincipal(juego));
            }
        }
    }

    private void crearTablaLiga() {
        if (estadoPartida.getGestorLiga() == null) {
            return;
        }

        tablaLiga = new Table();
        tablaLiga.setFillParent(false);

        GestorLiga gestor = estadoPartida.getGestorLiga();
        List<Map.Entry<Pais, Integer>> tablaOrdenada = gestor.getTablaOrdenada();

        Label headerPosicion = new Label("Pos", skin);
        headerPosicion.setFontScale(1.2f * escalaFuente);
        Label headerEquipo = new Label("Equipo", skin);
        headerEquipo.setFontScale(1.2f * escalaFuente);
        Label headerPuntos = new Label("Pts", skin);
        headerPuntos.setFontScale(1.2f * escalaFuente);

        tablaLiga.add(headerPosicion).padRight(20).padBottom(10);
        tablaLiga.add(headerEquipo).width(200 * escalaX).padRight(20).padBottom(10);
        tablaLiga.add(headerPuntos).padBottom(10);
        tablaLiga.row();

        tablaLiga.row();

        int posicion = 1;
        for (Map.Entry<Pais, Integer> entrada : tablaOrdenada) {
            Pais equipo = entrada.getKey();
            int puntos = entrada.getValue();

            Label labelPosicion = new Label(String.valueOf(posicion), skin);
            Label labelEquipo = new Label(equipo.getnombre(), skin);
            Label labelPuntos = new Label(String.valueOf(puntos), skin);

            labelPosicion.setFontScale(escalaFuente);
            labelEquipo.setFontScale(escalaFuente);
            labelPuntos.setFontScale(escalaFuente);

            if (equipo == estadoPartida.getPaisSelecionad()) {
                labelPosicion.setColor(Color.YELLOW);
                labelEquipo.setColor(Color.YELLOW);
                labelPuntos.setColor(Color.YELLOW);
            }

            tablaLiga.add(labelPosicion).padRight(20).padBottom(5);
            tablaLiga.add(labelEquipo).left().padRight(20).padBottom(5);
            tablaLiga.add(labelPuntos).padBottom(5);
            tablaLiga.row();

            posicion++;
        }

        tablaLiga.pack();
        tablaLiga.setPosition(
            (Gdx.graphics.getWidth() - tablaLiga.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - tablaLiga.getHeight()) / 2f
        );
    }

    public void resize(int width, int height) {
        mensaje.setFontScale(1.5f * escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
