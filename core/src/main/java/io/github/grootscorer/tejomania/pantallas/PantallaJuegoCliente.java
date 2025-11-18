package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.entidades.DiscoCliente;
import io.github.grootscorer.tejomania.entidades.MazoCliente;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.interfaces.ControladorJuegoRed;
import io.github.grootscorer.tejomania.redes.HiloCliente;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;
import io.github.grootscorer.tejomania.utiles.ManejoDeInputCliente;

public class PantallaJuegoCliente extends ScreenAdapter implements ControladorJuegoRed {
    private Stage stage;
    private Principal juego;
    private DiscoCliente disco;
    private MazoCliente mazo1, mazo2;
    private Skin skin;
    private EstadoPartida estadoPartida;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private HiloCliente hiloCliente;
    private ManejoDeInputCliente manejoDeInput;

    private boolean esperandoConexion = true;
    private boolean juegoIniciado = false;
    private boolean juegoTerminado = false;
    private int miNumeroJugador = -1;

    private Label labelEspera;
    private Label labelPuntaje;
    private Label labelGanador;
    private Label labelTiempo;
    private Label labelJugador1, labelJugador2;

    private String rutaRelativaMazoRojo = "imagenes/sprites/mazo_rojo.png";
    private String rutaAbsolutaMazoRojo = Gdx.files.internal(rutaRelativaMazoRojo).file().getAbsolutePath();

    private String rutaRelativaMazoAzul = "imagenes/sprites/mazo_azul.png";
    private String rutaAbsolutaMazoAzul = Gdx.files.internal(rutaRelativaMazoAzul).file().getAbsolutePath();

    private final Texture mazoRojo = new Texture(Gdx.files.internal(rutaAbsolutaMazoRojo));
    private final Texture mazoAzul = new Texture(Gdx.files.internal(rutaAbsolutaMazoAzul));

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    private final int CANCHA_ANCHO = (int) (540 * escalaX);
    private final int CANCHA_ALTO = (int) (320 * escalaY);
    float xCancha = (Gdx.graphics.getWidth() - CANCHA_ANCHO) / 2f;
    float yCancha = (Gdx.graphics.getHeight() - CANCHA_ALTO) / 2f;

    public PantallaJuegoCliente(Principal juego, EstadoPartida estadoPartida) {
        this.juego = juego;
        this.estadoPartida = estadoPartida;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        String rutaRelativaSkin = "ui/uiskin.json";
        String rutaAbsolutaSkin = Gdx.files.internal(rutaRelativaSkin).file().getAbsolutePath();
        skin = new Skin(Gdx.files.internal(rutaAbsolutaSkin));

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        disco = new DiscoCliente();
        mazo1 = new MazoCliente();
        mazo2 = new MazoCliente();

        mazo1.setTextura(mazoAzul);
        mazo2.setTextura(mazoRojo);

        labelEspera = new Label("Esperando al rival...", skin, "default");
        labelEspera.setColor(Color.GREEN);
        labelEspera.setFontScale(escalaFuente * 2.0f);
        labelEspera.setPosition(
            Gdx.graphics.getWidth() / 2f - labelEspera.getWidth(),
            Gdx.graphics.getHeight() / 2f
        );
        stage.addActor(labelEspera);

        labelPuntaje = new Label("0 - 0", skin, "default");
        labelPuntaje.setColor(Color.WHITE);
        labelPuntaje.setFontScale(escalaFuente * 1.5f);
        labelPuntaje.setPosition(
            Gdx.graphics.getWidth() / 2f - labelPuntaje.getWidth() / 2f,
            Gdx.graphics.getHeight() - 75
        );
        labelPuntaje.setVisible(false);
        stage.addActor(labelPuntaje);

        labelTiempo = new Label("5:00", skin, "default");
        labelTiempo.setColor(Color.WHITE);
        labelTiempo.setFontScale(escalaFuente * 1.2f);
        labelTiempo.setPosition(
            Gdx.graphics.getWidth() / 2f - labelTiempo.getWidth() / 2f,
            Gdx.graphics.getHeight() - 50
        );
        labelTiempo.setVisible(false);
        stage.addActor(labelTiempo);

        labelJugador1 = new Label(estadoPartida.getJugador1(), skin, "default");
        labelJugador1.setColor(Color.WHITE);
        labelJugador1.setFontScale(escalaFuente * 1.2f);
        labelJugador1.setPosition(
            50 * escalaX,  // Margen izquierdo
            Gdx.graphics.getHeight() - 50
        );
        labelJugador1.setVisible(false);
        stage.addActor(labelJugador1);

        labelJugador2 = new Label(estadoPartida.getJugador2(), skin, "default");
        labelJugador2.setColor(Color.WHITE);
        labelJugador2.setFontScale(escalaFuente * 1.2f);
        labelJugador2.setPosition(
            Gdx.graphics.getWidth() - 150 * escalaX,  // Margen derecho
            Gdx.graphics.getHeight() - 50
        );
        labelJugador2.setVisible(false);
        stage.addActor(labelJugador2);

        hiloCliente = new HiloCliente(this);
        hiloCliente.start();
        hiloCliente.enviarMensaje("Conectar");

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (esperandoConexion) {
            stage.act(delta);
            stage.draw();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                volverAlMenu();
            }
            return;
        }

        if (juegoIniciado && !juegoTerminado) {
            if (manejoDeInput != null) {
                manejoDeInput.actualizarMovimiento();
            }

            estadoPartida.actualizarTiempo(delta);

            actualizarLabelTiempo();
        }

        dibujarCancha();

        batch.begin();
        mazo1.dibujarConTextura(batch);
        mazo2.dibujarConTextura(batch);
        disco.dibujarConTextura(batch);
        batch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            volverAlMenu();
        }
    }

    private void dibujarCancha() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.WHITE);

        shapeRenderer.rect(xCancha, yCancha, CANCHA_ANCHO, CANCHA_ALTO);
        shapeRenderer.end();

        dibujarLineasCancha();
    }

    private void dibujarLineasCancha() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        float grosorLinea = Math.max(1f, 2f * Math.min(escalaX, escalaY));
        Gdx.gl.glLineWidth(grosorLinea);

        // Línea central
        float mitadCanchaX = xCancha + CANCHA_ANCHO / 2f;
        shapeRenderer.line(mitadCanchaX, yCancha, mitadCanchaX, yCancha + CANCHA_ALTO);

        shapeRenderer.end();

        // Dibujar arcos (semicírculos)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.WHITE);

        float radioY = CANCHA_ALTO / 4.5f;
        float radioX = radioY / 1.4f;

        // Arco izquierdo
        float centroIzquierdoX = xCancha;
        float centroIzquierdoY = yCancha + CANCHA_ALTO / 2f;

        int segmentos = Math.max(16, (int)(32 * Math.min(escalaX, escalaY)));
        for (int i = 0; i < segmentos; i++) {
            float angulo1 = (float) (-Math.PI / 2 + (Math.PI * i) / segmentos);
            float angulo2 = (float) (-Math.PI / 2 + (Math.PI * (i + 1)) / segmentos);

            float x1 = centroIzquierdoX + radioX * (float) Math.cos(angulo1);
            float y1 = centroIzquierdoY + radioY * (float) Math.sin(angulo1);
            float x2 = centroIzquierdoX + radioX * (float) Math.cos(angulo2);
            float y2 = centroIzquierdoY + radioY * (float) Math.sin(angulo2);

            shapeRenderer.triangle(centroIzquierdoX, centroIzquierdoY, x1, y1, x2, y2);
        }

        // Arco derecho
        float centroDerechoX = xCancha + CANCHA_ANCHO;
        float centroDerechoY = yCancha + CANCHA_ALTO / 2f;

        for (int i = 0; i < segmentos; i++) {
            float angulo1 = (float) (Math.PI / 2 + (Math.PI * i) / segmentos);
            float angulo2 = (float) (Math.PI / 2 + (Math.PI * (i + 1)) / segmentos);

            float x1 = centroDerechoX + radioX * (float) Math.cos(angulo1);
            float y1 = centroDerechoY + radioY * (float) Math.sin(angulo1);
            float x2 = centroDerechoX + radioX * (float) Math.cos(angulo2);
            float y2 = centroDerechoY + radioY * (float) Math.sin(angulo2);

            shapeRenderer.triangle(centroDerechoX, centroDerechoY, x1, y1, x2, y2);
        }

        shapeRenderer.end();

        // Contornos de los arcos
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        for (int i = 0; i < segmentos; i++) {
            float angulo1 = (float) (-Math.PI / 2 + (Math.PI * i) / segmentos);
            float angulo2 = (float) (-Math.PI / 2 + (Math.PI * (i + 1)) / segmentos);

            float x1 = centroIzquierdoX + radioX * (float) Math.cos(angulo1);
            float y1 = centroIzquierdoY + radioY * (float) Math.sin(angulo1);
            float x2 = centroIzquierdoX + radioX * (float) Math.cos(angulo2);
            float y2 = centroIzquierdoY + radioY * (float) Math.sin(angulo2);

            shapeRenderer.line(x1, y1, x2, y2);
        }

        for (int i = 0; i < segmentos; i++) {
            float angulo1 = (float) (Math.PI / 2 + (Math.PI * i) / segmentos);
            float angulo2 = (float) (Math.PI / 2 + (Math.PI * (i + 1)) / segmentos);

            float x1 = centroDerechoX + radioX * (float) Math.cos(angulo1);
            float y1 = centroDerechoY + radioY * (float) Math.sin(angulo1);
            float x2 = centroDerechoX + radioX * (float) Math.cos(angulo2);
            float y2 = centroDerechoY + radioY * (float) Math.sin(angulo2);

            shapeRenderer.line(x1, y1, x2, y2);
        }

        shapeRenderer.end();
        Gdx.gl.glLineWidth(1f);
    }

    @Override
    public void onConectar(int numeroJugador, float tiempoRestante, boolean jugandoPorTiempo,
                           boolean jugandoPorPuntaje, int puntajeGanador, boolean conObstaculos,
                           boolean conTirosEspeciales, boolean conModificadores, String cancha) {
        System.out.println("Conectado como jugador " + numeroJugador);
        this.miNumeroJugador = numeroJugador;

        // Aplicar configuración recibida del servidor
        estadoPartida.setTiempoRestante(tiempoRestante);
        estadoPartida.setJugandoPorTiempo(jugandoPorTiempo);
        estadoPartida.setJugandoPorPuntaje(jugandoPorPuntaje);
        estadoPartida.setPuntajeGanador(puntajeGanador);
        estadoPartida.setJugarConObstaculos(conObstaculos);
        estadoPartida.setJugarConTirosEspeciales(conTirosEspeciales);
        estadoPartida.setJugarConModificadores(conModificadores);
        estadoPartida.setCanchaSeleccionada(cancha);

        labelJugador1.setText("Jugador 1");
        labelJugador2.setText("Jugador 2");

        manejoDeInput = new ManejoDeInputCliente(hiloCliente, numeroJugador);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(manejoDeInput);
        Gdx.input.setInputProcessor(multiplexer);

        labelEspera.setText("Conectado. Esperando al rival...");
        labelEspera.setPosition(
            Gdx.graphics.getWidth() / 2f - labelEspera.getWidth(),
            Gdx.graphics.getHeight() / 2f
        );
    }

    @Override
    public void onIniciarJuego() {
        System.out.println("¡Juego iniciado!");
        esperandoConexion = false;
        juegoIniciado = true;

        // Ocultar pantalla de espera
        labelEspera.setVisible(false);

        // Mostrar todos los labels del juego
        labelPuntaje.setVisible(true);
        actualizarLabelPuntaje();

        labelTiempo.setVisible(true);

        labelJugador1.setVisible(true);
        labelJugador2.setVisible(true);
    }

    @Override
    public void onActualizarPosicionDisco(float x, float y, float velX, float velY) {
        disco.setPosicion(x, y);
    }

    @Override
    public void onActualizarPosicionMazo(int numeroJugador, float x, float y) {
        if (numeroJugador == 1) {
            mazo1.setPosicion((int) x, (int) y);
        } else if (numeroJugador == 2) {
            mazo2.setPosicion((int) x, (int) y);
        }
    }

    @Override
    public void onActualizarPuntaje(int puntaje1, int puntaje2) {
        estadoPartida.setPuntaje1(puntaje1);
        estadoPartida.setPuntaje2(puntaje2);
        actualizarLabelPuntaje();
    }

    private void actualizarLabelPuntaje() {
        String puntajeTexto = formatearPuntaje(estadoPartida.getPuntaje1()) +
            " - " +
            formatearPuntaje(estadoPartida.getPuntaje2());
        labelPuntaje.setText(puntajeTexto);
        labelPuntaje.setPosition(
            Gdx.graphics.getWidth() / 2f - labelPuntaje.getWidth() / 2f,
            Gdx.graphics.getHeight() - 50
        );
    }

    private void actualizarLabelTiempo() {
        int tiempoRestante = (int) estadoPartida.getTiempoRestante();
        int minutos = tiempoRestante / 60;
        int segundos = tiempoRestante % 60;
        String tiempoTexto = String.format("%d:%02d", minutos, segundos);
        labelTiempo.setText(tiempoTexto);

        labelTiempo.pack();
        labelTiempo.setPosition(
            Gdx.graphics.getWidth() / 2f - labelTiempo.getWidth() / 2f,
            Gdx.graphics.getHeight() - 75
        );
    }

    private String formatearPuntaje(int puntaje) {
        return puntaje < 10 ? "" + puntaje : String.valueOf(puntaje);
    }

    @Override
    public void onGol(int direccion) {
        String rutaRelativaSonido = "assets/audio/sonidos/sonido_gol.mp3";
        String rutaAbsolutaSonido = Gdx.files.internal(rutaRelativaSonido).file().getAbsolutePath();
        ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal(rutaAbsolutaSonido)));
    }

    @Override
    public void onMoverMazo(int numeroJugador, float velocidadX, float velocidadY) {

    }

    @Override
    public void onFinalizarJuego(int ganador) {
        juegoTerminado = true;

        String textoGanador;
        if (ganador == miNumeroJugador) {
            textoGanador = "¡GANASTE!";
        } else if (ganador == 0) {
            textoGanador = "¡EMPATE!";
        } else {
            textoGanador = "¡PERDISTE!";
        }

        Table tableGanador = new Table();
        stage.addActor(tableGanador);

        labelGanador = new Label(textoGanador, skin, "default");
        labelGanador.setColor(Color.RED);
        labelGanador.setFontScale(escalaFuente * 4.0f);

        tableGanador.add(labelGanador).padBottom(50 * escalaFuente).padLeft(Gdx.graphics.getWidth() / 2f);

        labelGanador.addAction(Actions.sequence(
            Actions.delay(3f),
            Actions.run(() -> volverAlMenu())
        ));
    }

    @Override
    public void onVolverAlMenu() {
        volverAlMenu();
    }

    @Override
    public void onActualizarTiempo(float tiempoRestante) {
        estadoPartida.setTiempoRestante(tiempoRestante);
    }

    private void volverAlMenu() {
        if (hiloCliente != null) {
            hiloCliente.terminar();
            try { hiloCliente.join(); } catch (Exception ignored) {}
        }

        Gdx.app.postRunnable(() -> {
            ManejoDeAudio.activarMusica("audio/musica/musica_menu.mp3", true);
            juego.setScreen(new MenuPrincipal(juego));
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (hiloCliente != null) {
            hiloCliente.terminar();
        }
        stage.dispose();
        skin.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        disco.dispose();
        mazoRojo.dispose();
        mazoAzul.dispose();
    }
}
