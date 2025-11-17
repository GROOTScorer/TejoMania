package io.github.grootscorer.tejomania.redes;

import com.badlogic.gdx.Gdx;
import io.github.grootscorer.tejomania.interfaces.ControladorJuegoRed;
import java.io.IOException;
import java.net.*;

public class HiloCliente extends Thread {
    private DatagramSocket socket;
    private int puertoServidor = 5555;
    private String ipServidorStr = "255.255.255.255";
    private InetAddress ipServidor;
    private boolean finalizado = false;
    private ControladorJuegoRed controladorJuego;

    public HiloCliente(ControladorJuegoRed controladorJuego) {
        try {
            this.controladorJuego = controladorJuego;
            ipServidor = InetAddress.getByName(ipServidorStr);
            socket = new DatagramSocket();
        } catch (SocketException | UnknownHostException e) {
            System.err.println("Error al crear socket del cliente: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        do {
            DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(paquete);
                procesarMensaje(paquete);
            } catch (IOException e) {
                if (!finalizado) {
                    System.err.println("Error al recibir paquete: " + e.getMessage());
                }
            }
        } while (!finalizado);
    }

    private void procesarMensaje(DatagramPacket paquete) {
        String mensaje = (new String(paquete.getData())).trim();
        String[] partes = mensaje.split(":");

        System.out.println("Mensaje recibido: " + mensaje);

        switch (partes[0]) {
            case "Conectado":
                this.ipServidor = paquete.getAddress();
                Gdx.app.postRunnable(() ->
                    controladorJuego.onConectar(Integer.parseInt(partes[1]))
                );
                break;

            case "Iniciar":
                Gdx.app.postRunnable(() -> controladorJuego.onIniciarJuego());
                break;

            case "ActualizarPosicion":
                if (partes[1].equals("Mazo")) {
                    int numJugador = Integer.parseInt(partes[2]);
                    float x = Float.parseFloat(partes[3]);
                    float y = Float.parseFloat(partes[4]);
                    Gdx.app.postRunnable(() ->
                        controladorJuego.onActualizarPosicionMazo(numJugador, x, y)
                    );
                } else if (partes[1].equals("Disco")) {
                    float x = Float.parseFloat(partes[2]);
                    float y = Float.parseFloat(partes[3]);
                    float velX = Float.parseFloat(partes[4]);
                    float velY = Float.parseFloat(partes[5]);
                    Gdx.app.postRunnable(() ->
                        controladorJuego.onActualizarPosicionDisco(x, y, velX, velY)
                    );
                }
                break;

            case "ActualizarPuntaje":
                int p1 = Integer.parseInt(partes[1]);
                int p2 = Integer.parseInt(partes[2]);
                Gdx.app.postRunnable(() -> controladorJuego.onActualizarPuntaje(p1, p2));
                break;

            case "ActualizarTiempo":
                float t = Float.parseFloat(partes[1]);
                Gdx.app.postRunnable(() -> controladorJuego.onActualizarTiempo(t));
                break;

            case "FinalizarJuego":
                int ganador = Integer.parseInt(partes[1]);
                Gdx.app.postRunnable(() -> controladorJuego.onFinalizarJuego(ganador));
                break;

            case "Desconectar":
                Gdx.app.postRunnable(() -> controladorJuego.onVolverAlMenu());
                break;
        }
    }

    public void enviarMensaje(String mensaje) {
        byte[] mensajeBytes = mensaje.getBytes();
        DatagramPacket paquete = new DatagramPacket(mensajeBytes,
            mensajeBytes.length, ipServidor, puertoServidor);
        try {
            socket.send(paquete);
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    public void terminar() {
        this.finalizado = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        this.interrupt();
    }
}
