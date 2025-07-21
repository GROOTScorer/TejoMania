package io.github.grootscorer.tejomania.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class ManejoDeAudio {
    private static Music musica;
    private static boolean musicaActivada = true;
    private static boolean sonidoActivado = true;
    private static float volumenSonido = 1;
    private static float volumenMusica = 1;
    private static String pathMusicaActual;
    private static boolean bucleMusicaActual;

    public static void activarMusica(String path, boolean bucle) {
        if (!musicaActivada) return;

        if (musica != null) {
            musica.stop();
            musica.dispose();
        }

        pathMusicaActual = path;
        bucleMusicaActual = bucle;

        musica = Gdx.audio.newMusic(Gdx.files.internal(path));
        musica.setLooping(bucle);
        musica.setVolume(volumenMusica);
        musica.play();
    }

    public static void reactivarMusica() {
        if (musicaActivada && (musica == null || !musica.isPlaying()) && pathMusicaActual != null) {
            activarMusica(pathMusicaActual, bucleMusicaActual);
        }
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
        sonido.play(volumenSonido);
    }

    public static void setMusicaActivada(boolean activada) {
        musicaActivada = activada;

        if (!activada && musica != null) {
            musica.stop();
        } else if (activada) {
            if (musica != null && !musica.isPlaying()) {
                musica.play();
            }
        }
    }


    public static void setSonidoActivado(boolean activado) {
        sonidoActivado = activado;
    }

    public static boolean isMusicaActivada() {
        return musicaActivada;
    }

    public static boolean isReproduciendoMusica() {
        return musica != null && musica.isPlaying();
    }

    public static boolean isSonidoActivado() {
        return sonidoActivado;
    }

    public static float getVolumenSonido() {
        return volumenSonido;
    }

    public static void setVolumenSonido(int nuevoVolumenSonido) {
        volumenSonido = (float) nuevoVolumenSonido / 100;
    }

    public static float getVolumenMusica() {
        return volumenMusica;
    }

    public static void setVolumenMusica(int nuevoVolumenMusica) {
        volumenMusica = (float) nuevoVolumenMusica / 100;

        if (musica != null) {
            musica.setVolume(volumenMusica);
        }
    }
}
