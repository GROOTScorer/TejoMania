package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.Principal;

public class MenuPausa {
    private Stage stage;
    private Skin skin;
    private final Principal juego;

    public MenuPausa(Principal juego) {
        this.juego = juego;
    }

    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton seguirBtn = new TextButton("Reanudar", skin);
        TextButton salirBtn = new TextButton("Salir", skin);

        salirBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new MenuPrincipal(juego));
            }
        });

        seguirBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //juego.setScreen(new PantallaJuego(juego));
            }
        });

        table.add(salirBtn).width(200).pad(20);
        table.add(seguirBtn).width(200).pad(20);
    }
}
