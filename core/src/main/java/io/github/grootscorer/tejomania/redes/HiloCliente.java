package io.github.grootscorer.tejomania.redes;

import com.badlogic.gdx.Gdx;
import io.github.grootscorer.tejomania.interfaces.ControladorJuegoRed;
import java.io.IOException;
import java.net.*;

/*
 Hilo encargado de recibir y enviar mensajes por UDP hacia el servidor del juego.
 Procesa los mensajes recibidos y notifica al controlador del juego mediante callbacks.
 */
public class HiloCliente extends Thread {
    private DatagramSocket socket;            // Socket UDP del cliente
    private int puertoServidor = 5555;        // Puerto donde escucha el servidor
    private String ipServidorStr = "255.255.255.255"; // Dirección broadcast inicial. Envía datos a todos los dispositivos de la LAN
    private InetAddress ipServidor;           // IP real del servidor (definida al recibir "Conectado")
    private boolean finalizado = false;       // Bandera para detener el hilo
    private ControladorJuegoRed controladorJuego; // Interfaz para comunicar eventos al juego

    public HiloCliente(ControladorJuegoRed controladorJuego) {
        try {
            this.controladorJuego = controladorJuego;

            // Dirección inicial: broadcast; el servidor luego responde con su IP real
            ipServidor = InetAddress.getByName(ipServidorStr);

            // Crea socket UDP sin puerto específico (el SO asigna uno)
            socket = new DatagramSocket();

        } catch (SocketException | UnknownHostException e) {
            System.err.println("Error al crear socket del cliente: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // Bucle principal del hilo: escucha mensajes hasta que finalizado = true
        do {
            // Crea un paquete vacío donde se guardarán los datos recibidos
            DatagramPacket paquete = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(paquete); // Espera la llegada de un paquete UDP
                procesarMensaje(paquete); // Maneja el contenido del paquete

            } catch (IOException e) {
                if (!finalizado) {
                    System.err.println("Error al recibir paquete: " + e.getMessage());
                }
            }
        } while (!finalizado);
    }

    // Procesa el mensaje recibido y ejecuta la acción correspondiente
    private void procesarMensaje(DatagramPacket paquete) {
        // Convierte los bytes a String y elimina espacios vacíos
        String mensaje = (new String(paquete.getData())).trim();
        String[] partes = mensaje.split(":"); // Formato del protocolo: "Comando:param1:param2"

        System.out.println("Mensaje recibido: " + mensaje);

        switch (partes[0]) {

            case "Conectado":
                // El servidor responde con: numJugador:tiempo:jugandoPorTiempo:jugandoPorPuntaje:puntajeGanador:obstaculos:tirosEspeciales:modificadores:cancha
                this.ipServidor = paquete.getAddress();

                // Parsear configuración
                int numeroJugador = Integer.parseInt(partes[1]);
                float tiempoRestante = Float.parseFloat(partes[2]);
                boolean jugandoPorTiempo = Boolean.parseBoolean(partes[3]);
                boolean jugandoPorPuntaje = Boolean.parseBoolean(partes[4]);
                int puntajeGanador = Integer.parseInt(partes[5]);
                boolean conObstaculos = Boolean.parseBoolean(partes[6]);
                boolean conTirosEspeciales = Boolean.parseBoolean(partes[7]);
                boolean conModificadores = Boolean.parseBoolean(partes[8]);
                String cancha = partes[9];

                // Encolar ejecución en el hilo principal
                Gdx.app.postRunnable(() ->
                    controladorJuego.onConectar(numeroJugador, tiempoRestante,
                        jugandoPorTiempo, jugandoPorPuntaje, puntajeGanador,
                        conObstaculos, conTirosEspeciales, conModificadores, cancha)
                );
                break;

            case "Iniciar":
                // El servidor indica que la partida va a comenzar
                Gdx.app.postRunnable(() -> controladorJuego.onIniciarJuego());
                break;

            case "ActualizarPosicion":
                // Actualización del estado del juego: mazos o disco
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
                // Actualiza puntaje de ambos jugadores
                int puntaje1 = Integer.parseInt(partes[1]);
                int puntaje2 = Integer.parseInt(partes[2]);

                Gdx.app.postRunnable(() -> controladorJuego.onActualizarPuntaje(puntaje1, puntaje2));
                break;

            case "ActualizarTiempo":
                // Actualiza el tiempo restante del juego
                float tiempo = Float.parseFloat(partes[1]);
                Gdx.app.postRunnable(() -> controladorJuego.onActualizarTiempo(tiempo));
                break;

            case "FinalizarJuego":
                // El servidor indica que el juego terminó y envía el ganador
                int ganador = Integer.parseInt(partes[1]);
                Gdx.app.postRunnable(() -> controladorJuego.onFinalizarJuego(ganador));
                break;

            case "Desconectar":
                // Regresa al menú principal
                Gdx.app.postRunnable(() -> controladorJuego.onVolverAlMenu());
                break;
        }
    }

    // Envía un mensaje al servidor usando UDP
    public void enviarMensaje(String mensaje) {
        byte[] mensajeBytes = mensaje.getBytes();

        // Crea el paquete hacia la IP del servidor en el puerto 5555
        DatagramPacket paquete = new DatagramPacket(
            mensajeBytes, mensajeBytes.length, ipServidor, puertoServidor
        );

        try {
            socket.send(paquete); // Envía el paquete UDP
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    // Cierra el hilo y el socket
    public void terminar() {
        this.finalizado = true;

        // Cierra el socket si aún está abierto
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        this.interrupt(); // Interrumpe el hilo (en caso de que esté bloqueado)
    }
}
