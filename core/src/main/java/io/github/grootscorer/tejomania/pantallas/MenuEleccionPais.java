package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.Principal;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.grootscorer.tejomania.enums.Pais;
import io.github.grootscorer.tejomania.enums.TipoCompetencia;

import java.util.ArrayList;
import java.util.List;

public class MenuEleccionPais extends ScreenAdapter {
    private Principal juego;
    private Stage stage;
    private Skin skin;
    private TipoCompetencia tipoCompetencia;
    private Label nombrePais;
    private Label escParaVolver;
    private Label flechaIzquierda, flechaDerecha;
    private Texture texturaPais, texturaPaisAnterior, texturaPaisSiguiente;
    private Image imagenPais, imagenPaisAnterior, imagenPaisSiguiente;
    private List<Pais> paisesDisponibles;
    private int opcionPais = 0;

    public MenuEleccionPais(Principal juego, TipoCompetencia tipoCompetencia) {
        this.juego = juego;
        this.tipoCompetencia = tipoCompetencia;
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(true);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        nombrePais = new Label("", skin);
        nombrePais.setFontScale(2f);

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

        imagenPaisAnterior = new Image();
        imagenPaisSiguiente = new Image();
        imagenPais = new Image();

        imagenPais.setSize(alturaPrincipal * (200f / 120f), alturaPrincipal);
        imagenPaisAnterior.setSize(alturaSecundaria * (200f / 120f), alturaSecundaria);
        imagenPaisSiguiente.setSize(alturaSecundaria * (200f / 120f), alturaSecundaria);

        if(Gdx.graphics.getHeight() < 700) {
            tabla.add(imagenPaisAnterior).padLeft(10).padRight(20).height(alturaSecundaria * (200f / 120f)).maxWidth(900);
        } else {
            tabla.add(imagenPaisAnterior).padLeft(10).padRight(20).height(alturaSecundaria * (200f / 120f)).maxWidth(250);
        }

        tabla.add(flechaIzquierda).padRight(20);

        if(Gdx.graphics.getHeight() < 700) {
            tabla.add(imagenPais).padRight(20).height(alturaPrincipal * (200f / 120f));
        } else {
            tabla.add(imagenPais).padRight(20).height(alturaPrincipal * (200f / 120f)).maxWidth(300);
        }

        tabla.add(flechaDerecha).padRight(20);

        if(Gdx.graphics.getHeight() < 700) {
            tabla.add(imagenPaisSiguiente).height(alturaSecundaria * (200f / 120f)).padRight(10).maxWidth(900);
        } else {
            tabla.add(imagenPaisSiguiente).height(alturaSecundaria * (200f / 120f)).padRight(10).maxWidth(250);
        }

        tabla.row();
        tabla.add(nombrePais).colspan(5).center().padTop(20);
        tabla.row();
        tabla.add(escParaVolver).colspan(5).center().padTop(100);

        stage.addActor(tabla);
        paisesDisponibles = new ArrayList<>();
        filtrarPaises();
        actualizarPais();
    }

    private void filtrarPaises() {
        for (Pais pais : Pais.values()) {
            if (tipoCompetencia == TipoCompetencia.LIGA && pais.getTipoTorneo() == TipoCompetencia.LIGA) {
                paisesDisponibles.add(pais);
            } else if (tipoCompetencia == TipoCompetencia.TORNEO) {
                paisesDisponibles.add(pais);
            }
        }
        paisesDisponibles.sort((p1, p2) -> p1.getnombre().compareTo(p2.getnombre()));
    }

    private void actualizarPais() {
        if (paisesDisponibles.isEmpty()) return;

        Pais seleccionado = paisesDisponibles.get(opcionPais);
        Pais anterior = paisesDisponibles.get((opcionPais - 1 + paisesDisponibles.size()) % paisesDisponibles.size());
        Pais siguiente = paisesDisponibles.get((opcionPais + 1) % paisesDisponibles.size());

        nombrePais.setText(seleccionado.getnombre());

        if (texturaPais != null) texturaPais.dispose();
        if (texturaPaisAnterior != null) texturaPaisAnterior.dispose();
        if (texturaPaisSiguiente != null) texturaPaisSiguiente.dispose();

        texturaPais = new Texture(Gdx.files.internal(seleccionado.getTextura()));
        texturaPaisAnterior = new Texture(Gdx.files.internal(anterior.getTextura()));
        texturaPaisSiguiente = new Texture(Gdx.files.internal(siguiente.getTextura()));

        imagenPais.setDrawable(new Image(texturaPais).getDrawable());
        imagenPaisAnterior.setDrawable(new Image(texturaPaisAnterior).getDrawable());
        imagenPaisSiguiente.setDrawable(new Image(texturaPaisSiguiente).getDrawable());

        float OPACIDAD_PAIS_SECUNDARIO = 0.5f;
        imagenPaisAnterior.getColor().a = OPACIDAD_PAIS_SECUNDARIO;
        imagenPaisSiguiente.getColor().a = OPACIDAD_PAIS_SECUNDARIO;
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
            opcionPais = (opcionPais + 1) % paisesDisponibles.size();
            actualizarPais();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            opcionPais = (opcionPais - 1 + paisesDisponibles.size()) % paisesDisponibles.size();
            actualizarPais();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Pais paisSeleccionado = paisesDisponibles.get(opcionPais);
            juego.setScreen(new EleccionDificultadCompetencia(juego, paisSeleccionado, tipoCompetencia));
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

        nombrePais.setFontScale(2f * escalaFuente);
        flechaIzquierda.setFontScale(3f * escalaFuente);
        flechaDerecha.setFontScale(3f * escalaFuente);
        escParaVolver.setFontScale(escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (texturaPais != null) texturaPais.dispose();
        if (texturaPaisAnterior != null) texturaPaisAnterior.dispose();
        if (texturaPaisSiguiente != null) texturaPaisSiguiente.dispose();
        stage.dispose();
        skin.dispose();
    }
}
