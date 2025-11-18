package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.Input;
import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

public class EleccionNombre extends ScreenAdapter {
    private Stage stage;
    private Skin skin;

    private final Principal juego;
    private final TipoJuegoLibre tipoJuegoLibre;
    private EstadoPartida estadoPartida;

    private String nombre1 = "", nombre2 = "";
    private Label labelNombre;
    private StringBuilder textoIngresado;
    private Label titulo;
    private Label empezarJuego;
    private boolean esPrimerJugador = true;

    private final float tiempoEsperaBackspace = 0.5f;
    private final float tiempoAceleracionBackspace = 0.1f;
    private boolean backspacePresionadoAnteriormente = false;
    private float tiempoDesdeUltimoBorrado = 0f;
    private boolean primerBorradoRealizado = false;

    public EleccionNombre(Principal juego, TipoJuegoLibre tipoJuegoLibre, EstadoPartida estadoPartida, String CanchaSeleccionado) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.estadoPartida = estadoPartida;
    }

    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        String rutaRelativaSkin = "ui/uiskin.json";
        String rutaAbsolutaSkin = Gdx.files.internal(rutaRelativaSkin).file().getAbsolutePath();
        skin = new Skin(Gdx.files.internal(rutaAbsolutaSkin));
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        titulo = new Label("Elija su nombre", skin, "default");
        titulo.setFontScale(3f);

        Table tituloContenedor = new Table();
        tituloContenedor.setFillParent(true);
        tituloContenedor.top();
        tituloContenedor.add(titulo).expandX().center().padTop(20);
        stage.addActor(tituloContenedor);

        labelNombre = new Label("", skin, "default");
        labelNombre.setFontScale(2f);
        table.add(labelNombre).padTop(30).width(300).height(40).row();

        empezarJuego = new Label("Presiona ENTER para continuar", skin, "default");
        table.add(empezarJuego).padTop(40).row();

        textoIngresado = new StringBuilder();

        labelNombre.setText("Jugador 1: " + nombre1);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            manejarEscape();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarEnter();
        }

        for (int keyCode = Input.Keys.A; keyCode <= Input.Keys.Z; keyCode++) {
            if (Gdx.input.isKeyJustPressed(keyCode)) {
                char teclaPresionada = (char) (keyCode - Input.Keys.A + 'A');
                textoIngresado.append(teclaPresionada);
                actualizarNombre();
            }
        }

        for (int keyCode = Input.Keys.NUM_0; keyCode <= Input.Keys.NUM_9; keyCode++) {
            if (Gdx.input.isKeyJustPressed(keyCode)) {
                char teclaPresionada = (char) (keyCode - Input.Keys.NUM_0 + '0');
                textoIngresado.append(teclaPresionada);
                actualizarNombre();
            }
        }

        boolean backspacePresionadoAhora = Gdx.input.isKeyPressed(Input.Keys.BACKSPACE);

        if (backspacePresionadoAhora) {
            if (!backspacePresionadoAnteriormente) {
                if (textoIngresado.length() > 0) {
                    textoIngresado.deleteCharAt(textoIngresado.length() - 1);
                    actualizarNombre();
                }
                primerBorradoRealizado = true;
                tiempoDesdeUltimoBorrado = 0f;
            } else {
                tiempoDesdeUltimoBorrado += delta;

                if (primerBorradoRealizado && tiempoDesdeUltimoBorrado > tiempoEsperaBackspace) {
                    if (textoIngresado.length() > 0) {
                        textoIngresado.deleteCharAt(textoIngresado.length() - 1);
                        actualizarNombre();
                    }
                    tiempoDesdeUltimoBorrado = tiempoEsperaBackspace - tiempoAceleracionBackspace;
                }
            }
        } else {
            primerBorradoRealizado = false;
            tiempoDesdeUltimoBorrado = 0f;
        }

        backspacePresionadoAnteriormente = backspacePresionadoAhora;

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void resize(int width, int height) {
        float escalaX = (float) width / 640f;
        float escalaY = (float) height / 480f;
        float escalaFuente = Math.max(escalaX, escalaY);

        titulo.setFontScale(3f * escalaFuente);
        labelNombre.setFontScale(2f * escalaFuente);
        empezarJuego.setFontScale(escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void actualizarNombre() {
        if (esPrimerJugador) {
            labelNombre.setText("Jugador 1: " + textoIngresado.toString());
        } else {
            labelNombre.setText("Jugador 2: " + textoIngresado.toString());
        }
    }

    private void manejarEscape() {
        if (tipoJuegoLibre == TipoJuegoLibre.CPU) {
            juego.setScreen(new MenuOpcionesJuego(juego, tipoJuegoLibre, estadoPartida));
        } else if (tipoJuegoLibre == TipoJuegoLibre.DOS_JUGADORES) {
            if (esPrimerJugador) {
                juego.setScreen(new MenuOpcionesJuego(juego, tipoJuegoLibre, estadoPartida));
            } else {
                esPrimerJugador = true;
                textoIngresado.setLength(0);
                labelNombre.setText("Jugador 1: ");
            }
        }
    }

    private void manejarEnter() {
        if (tipoJuegoLibre == TipoJuegoLibre.CPU) {
            nombre1 = textoIngresado.toString();
            if (!nombre1.isEmpty()) {
                ManejoDeAudio.pararMusica();
                estadoPartida.setJugador1(nombre1);
                estadoPartida.setJugador2("CPU");
                juego.setScreen(new PantallaJuego(juego, tipoJuegoLibre, estadoPartida));
            }
        } else if (tipoJuegoLibre == TipoJuegoLibre.DOS_JUGADORES) {
            if (esPrimerJugador) {
                nombre1 = textoIngresado.toString();
                if (!nombre1.isEmpty()) {
                    estadoPartida.setJugador1(nombre1);
                    esPrimerJugador = false;
                    textoIngresado.setLength(0);
                    labelNombre.setText("Jugador 2: ");
                }
            } else {
                nombre2 = textoIngresado.toString();
                if (!nombre2.isEmpty()) {
                    ManejoDeAudio.pararMusica();
                    estadoPartida.setJugador2(nombre2);
                    juego.setScreen(new PantallaJuego(juego, tipoJuegoLibre, estadoPartida));
                }
            }
        }
    }
}
