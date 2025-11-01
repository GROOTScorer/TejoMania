package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.estado.GestorLiga;
import io.github.grootscorer.tejomania.enums.Pais;
import java.util.List;
import java.util.Map;

public class PantallaResultadoFinalLiga extends ScreenAdapter {
    private Principal juego;
    private Stage stage;
    private Skin skin;
    private EstadoPartida estadoPartida;
    private boolean ganoLiga;
    private Table tablaLiga;

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    public PantallaResultadoFinalLiga(Principal juego, EstadoPartida estadoPartida, boolean ganoLiga) {
        this.juego = juego;
        this.estadoPartida = estadoPartida;
        this.ganoLiga = ganoLiga;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Gdx.input.setCursorCatched(true);

        Label mensaje;
        if (ganoLiga) {
            mensaje = new Label("¡FELICITACIONES! ¡GANASTE LA LIGA!", skin);
            mensaje.setColor(Color.GOLD);
        } else {
            GestorLiga gestor = estadoPartida.getGestorLiga();
            int posicion = gestor.getPosicionEquipo(estadoPartida.getPaisSelecionad());
            mensaje = new Label("Liga Finalizada - Posición: " + posicion + "°", skin);
            mensaje.setColor(Color.WHITE);
        }

        mensaje.setFontScale(2f * escalaFuente);
        mensaje.setPosition(
            (Gdx.graphics.getWidth() - mensaje.getPrefWidth()) / 2f,
            Gdx.graphics.getHeight() - 80
        );
        stage.addActor(mensaje);

        crearTablaFinal();

        Label instruccion = new Label("Presiona ENTER para volver al menú", skin);
        instruccion.setFontScale(escalaFuente);
        instruccion.setColor(Color.GRAY);
        instruccion.setPosition(
            (Gdx.graphics.getWidth() - instruccion.getPrefWidth()) / 2f,
            50
        );
        stage.addActor(instruccion);

        Gdx.input.setInputProcessor(stage);
    }

    private void crearTablaFinal() {
        if (estadoPartida.getGestorLiga() == null) {
            return;
        }

        tablaLiga = new Table();
        tablaLiga.setFillParent(false);

        GestorLiga gestor = estadoPartida.getGestorLiga();
        List<Map.Entry<Pais, Integer>> tablaOrdenada = gestor.getTablaOrdenada();

        Label titulo = new Label("TABLA FINAL", skin);
        titulo.setFontScale(1.5f * escalaFuente);
        tablaLiga.add(titulo).colspan(3).padBottom(20);
        tablaLiga.row();

        Label headerPos = new Label("Pos", skin);
        headerPos.setFontScale(1.2f * escalaFuente);
        Label headerEquipo = new Label("Equipo", skin);
        headerEquipo.setFontScale(1.2f * escalaFuente);
        Label headerPuntos = new Label("Pts", skin);
        headerPuntos.setFontScale(1.2f * escalaFuente);

        tablaLiga.add(headerPos).padRight(20).padBottom(10);
        tablaLiga.add(headerEquipo).width(200 * escalaX).padRight(20).padBottom(10);
        tablaLiga.add(headerPuntos).padBottom(10);
        tablaLiga.row();

        int posicion = 1;
        for (Map.Entry<Pais, Integer> entrada : tablaOrdenada) {
            Pais equipo = entrada.getKey();
            int puntos = entrada.getValue();

            Label labelPos = new Label(String.valueOf(posicion), skin);
            Label labelEquipo = new Label(equipo.getnombre(), skin);
            Label labelPuntos = new Label(String.valueOf(puntos), skin);

            labelPos.setFontScale(escalaFuente);
            labelEquipo.setFontScale(escalaFuente);
            labelPuntos.setFontScale(escalaFuente);

            if (equipo == estadoPartida.getPaisSelecionad()) {
                labelPos.setColor(Color.YELLOW);
                labelEquipo.setColor(Color.YELLOW);
                labelPuntos.setColor(Color.YELLOW);
            }

            if (posicion == 1) {
                labelPos.setColor(Color.GOLD);
                labelEquipo.setColor(Color.GOLD);
                labelPuntos.setColor(Color.GOLD);
            }

            tablaLiga.add(labelPos).padRight(20).padBottom(5);
            tablaLiga.add(labelEquipo).left().padRight(20).padBottom(5);
            tablaLiga.add(labelPuntos).padBottom(5);
            tablaLiga.row();

            posicion++;
        }

        tablaLiga.pack();
        tablaLiga.setPosition(
            (Gdx.graphics.getWidth() - tablaLiga.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - tablaLiga.getHeight()) / 2f - 20
        );

        stage.addActor(tablaLiga);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            juego.setScreen(new MenuPrincipal(juego));
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
}
