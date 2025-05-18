package io.github.grootscorer.tejomania.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class ManejoDeAudio {
    private static Music musica;
    private static boolean musicaActivada = true;
    private static boolean sonidoActivado = true;

    public static void activarMusica(String path, boolean loop) {
        if (!musicaActivada) return;
        if (musica != null) musica.stop();

        musica = Gdx.audio.newMusic(Gdx.files.internal(path));
        musica.setLooping(loop);
        musica.play();
    }

    public static void pararMusica() {
        if (musica != null) {
            musica.stop();
            musica.dispose();
            musica = null;
        }
    }

    public static void activarSonido(String ruta) {
        if (!sonidoActivado) return;
        Sound sonido = Gdx.audio.newSound(Gdx.files.internal(ruta));
        sonido.play();
    }

    public static void setMusicaActivada(boolean activada) {
        musicaActivada = activada;
        if (!activada && musica != null) musica.stop();
    }

    public static void setSonidoActivado(boolean activado) {
        sonidoActivado = activado;
    }

    public static boolean isMusicaActivada() {
        return musicaActivada;
    }

    public static boolean isSonidoActivado() {
        return sonidoActivado;
    }
}
