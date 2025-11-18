package io.github.grootscorer.tejomania.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.grootscorer.tejomania.redes.HiloCliente;

public class ManejoDeInputCliente extends InputAdapter {
    private final int velocidad = 8;
    private HiloCliente hiloCliente;
    private int numeroJugador; // 1 o 2

    public ManejoDeInputCliente(HiloCliente hiloCliente, int numeroJugador) {
        this.hiloCliente = hiloCliente;
        this.numeroJugador = numeroJugador;
    }

    public void actualizarMovimiento() {
        float velX = 0;
        float velY = 0;

        // Determinar qué controles usar según el número de jugador
        if (numeroJugador == 1) {
            // Jugador 1 usa WASD
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                velY = velocidad;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                velY = -velocidad;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                velX = -velocidad;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                velX = velocidad;
            }
        } else if (numeroJugador == 2) {
            // Jugador 2 usa flechas
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                velY = velocidad;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                velY = -velocidad;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velX = -velocidad;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velX = velocidad;
            }
        }

        enviarMovimiento(velX, velY);
    }

    private void enviarMovimiento(float velX, float velY) {
        // Formato: "MoverMazo:velX:velY"
        String mensaje = "MoverMazo:" + velX + ":" + velY;
        hiloCliente.enviarMensaje(mensaje);
    }}
