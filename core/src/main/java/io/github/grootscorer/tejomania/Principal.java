package io.github.grootscorer.tejomania;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.grootscorer.tejomania.pantallas.MenuPrincipal;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Principal extends Game {
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MenuPrincipal(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
