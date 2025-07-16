package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.grootscorer.tejomania.entidades.Jugador;
import io.github.grootscorer.tejomania.entidades.Obstaculo;
import io.github.grootscorer.tejomania.entidades.modificadores.CongelarRival;
import io.github.grootscorer.tejomania.hud.BarraEspecial;
import io.github.grootscorer.tejomania.hud.EncabezadoPartida;

public class PantallaJuego {
    private Jugador jugador1, jugador2;
    private Skin skin;
    private Obstaculo obstaculo;
    private CongelarRival congelarRival;
    private BarraEspecial barraEspecial1, barraEspecial2;
    private EncabezadoPartida encabezadoPartida;
    private Texture texturaCancha;
    private Image imagenCancha;

    public PantallaJuego(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    private void anunciarResultado() {
        Label anuncioResultado;
        if(encabezadoPartida.getPuntaje1() > encabezadoPartida.getPuntaje2()) {
            anuncioResultado = new Label(jugador1.getNombre() + " gana", skin, "default");
        }   else if(encabezadoPartida.getPuntaje2() > encabezadoPartida.getPuntaje1()) {
            anuncioResultado = new Label(jugador2.getNombre() + " gana", skin, "default");
        }   else {
            anuncioResultado = new Label("Empate", skin, "default");
        }
    }
}
