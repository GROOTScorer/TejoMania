package io.github.grootscorer.tejomania.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.grootscorer.tejomania.Principal;
import io.github.grootscorer.tejomania.enums.DificultadCPU;
import io.github.grootscorer.tejomania.enums.TipoJuegoLibre;
import io.github.grootscorer.tejomania.estado.EstadoPartida;
import io.github.grootscorer.tejomania.utiles.ManejoDeAudio;

public class MenuOpcionesJuego extends ScreenAdapter {
    private final Principal juego;
    private final TipoJuegoLibre tipoJuegoLibre;
    private EstadoPartida estadoPartida = new EstadoPartida();

    private Stage stage;
    private Skin skin;
    private boolean uiInicializada = false;
    private Label titulo;
    private Label[] opciones;
    private Label[] valores;
    private CheckBox[] checkboxes;

    private int opcionActual = OPCION_EMPEZAR;
    private boolean eleccionModificador = true;
    private boolean elecionObstaculo = true;
    private boolean eleccionJugarPorPuntaje = false;
    private boolean eleccionJugarPorTiempo = true;
    private boolean eleccionTirosEspeciales = true;

    private int tiempo = TIEMPO_POR_DEFECTO, puntajeGanador = PUNTAJE_POR_DEFECTO;
    private DificultadCPU dificultad = DificultadCPU.FACIL;

    private static final int CANT_OPCIONES = 9;

    private static final int TIEMPO_MINIMO = 1;
    private static final int TIEMPO_MAXIMO = 30;
    private static final int PUNTAJE_MINIMO = 1;
    private static final int PUNTAJE_MAXIMO = 30;
    private static final int TIEMPO_POR_DEFECTO = 5;
    private static final int PUNTAJE_POR_DEFECTO = 2;

    private static final int OPCION_EMPEZAR = 0;
    private static final int OPCION_CONDICION_JUEGO = 1;
    private static final int OPCION_PUNTAJE = 2;
    private static final int OPCION_TIEMPO = 3;
    private static final int OPCION_DIFICULTAD = 4;
    private static final int OPCION_MODIFICADORES = 5;
    private static final int OPCION_TIROS_ESPECIALES = 6;
    private static final int OPCION_OBSTACULOS = 7;
    private static final int OPCION_SALIR = 8;

    private static final int CHECKBOX_MODIFICADORES = 0;
    private static final int CHECKBOX_TIROS_ESPECIALES = 1;
    private static final int CHECKBOX_OBSTACULOS = 2;

    float escalaX = (float) Gdx.graphics.getWidth() / 640f;
    float escalaY = (float) Gdx.graphics.getHeight() / 480f;
    float escalaFuente = Math.max(escalaX, escalaY);

    public MenuOpcionesJuego(Principal juego, TipoJuegoLibre tipoJuegoLibre, EstadoPartida estadoPartida) {
        this.juego = juego;
        this.tipoJuegoLibre = tipoJuegoLibre;
        this.estadoPartida = estadoPartida;
    }

    public void show() {
        Gdx.input.setCursorCatched(true);
        if (!uiInicializada) {
            crearInterfaz();
            uiInicializada = true;
        }
        actualizarValores();
        Gdx.input.setInputProcessor(stage);
    }

    private void crearInterfaz() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        titulo = new Label("Opciones de juego", skin, "default");
        titulo.setFontScale(3f * escalaFuente);
        Table tituloContenedor = new Table();
        tituloContenedor.setFillParent(true);
        tituloContenedor.top();
        tituloContenedor.add(titulo).expandX().center().padTop(20);
        stage.addActor(tituloContenedor);

        opciones = new Label[CANT_OPCIONES];

        opciones[OPCION_EMPEZAR] = new Label("Jugar", skin, "default");
        opciones[OPCION_EMPEZAR].setColor(Color.RED);
        opciones[OPCION_CONDICION_JUEGO] = new Label("Jugar por:", skin, "default");
        opciones[OPCION_PUNTAJE] = new Label("Elegir puntaje:", skin, "default");
        opciones[OPCION_TIEMPO] = new Label("Elegir tiempo:", skin, "default");
        opciones[OPCION_DIFICULTAD] = new Label("Elegir dificultad:", skin, "default");
        opciones[OPCION_MODIFICADORES] = new Label("¿Habilitar modificadores?", skin, "default");
        opciones[OPCION_TIROS_ESPECIALES] = new Label("¿Habilitar tiros especiales?", skin, "default");
        opciones[OPCION_OBSTACULOS] = new Label("¿Habilitar obstaculos?", skin, "default");
        opciones[OPCION_SALIR] = new Label("Salir", skin, "default");

        final int CANT_CHECKBOXES = 3;
        checkboxes = new CheckBox[CANT_CHECKBOXES];

        for (int i = 0; i < CANT_CHECKBOXES; i++) {
            checkboxes[i] = new CheckBox("", skin);
            checkboxes[i].setChecked(true);
        }

        final int CANT_VALORES = 9;
        valores = new Label[CANT_VALORES];

        valores[OPCION_EMPEZAR] = new Label("", skin);
        valores[OPCION_CONDICION_JUEGO] = new Label("", skin);
        valores[OPCION_PUNTAJE] = new Label("", skin);
        valores[OPCION_TIEMPO] = new Label("", skin);
        valores[OPCION_DIFICULTAD] = new Label("", skin);
        valores[OPCION_MODIFICADORES] = new Label("", skin);
        valores[OPCION_TIROS_ESPECIALES] = new Label("", skin);
        valores[OPCION_OBSTACULOS] = new Label("", skin);
        valores[OPCION_SALIR] = new Label("", skin);

        for (int i = 0; i < CANT_OPCIONES; i++) {
            if (opciones[i] != null) {
                if (i == OPCION_PUNTAJE && !eleccionJugarPorPuntaje) continue;
                if (i == OPCION_TIEMPO && !eleccionJugarPorTiempo) continue;
                if (i == OPCION_DIFICULTAD && tipoJuegoLibre != TipoJuegoLibre.CPU) continue;

                opciones[i].setFontScale(1.5f * escalaFuente);
                table.add(opciones[i]).pad(10);

                if (valores[i] != null) {
                    table.add(valores[i]).pad(10);
                }

                if (i == OPCION_MODIFICADORES || i == OPCION_TIROS_ESPECIALES || i == OPCION_OBSTACULOS) {
                    table.add(checkboxes[i - 5]);
                }

                table.row();
            }
        }
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            moverSeleccion(-1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            moverSeleccion(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            manejarFlechaIzquierda();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            manejarFlechaDerecha();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            manejarEnter();
        }

        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        for(Label opcion: opciones) {
            opcion.setFontScale(1.5f * escalaFuente);
        }

        for(Label valor: valores) {
            valor.setFontScale(1.5f * escalaFuente);
        }

        titulo.setFontScale(3f * escalaFuente);

        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void actualizarSeleccion() {
        for (int i = 0; i < CANT_OPCIONES; i++) {
            if (opcionActual == i) {
                opciones[i].setColor(Color.RED);
            } else {
                if (opciones[i] != null) {
                    opciones[i].setColor(Color.WHITE);
                }
            }
        }
        ManejoDeAudio.activarSonido(String.valueOf(Gdx.files.internal("audio/sonidos/sonido_seleccion.mp3")));
    }

    private void moverSeleccion(int direccion) {
        int nuevoIndice = opcionActual;
        do {
            nuevoIndice = (nuevoIndice + direccion + CANT_OPCIONES) % CANT_OPCIONES;
        } while (!isOpcionVisible(nuevoIndice) && nuevoIndice != opcionActual);

        if (nuevoIndice != opcionActual) {
            opcionActual = nuevoIndice;
            actualizarSeleccion();
        }
    }

    private boolean isOpcionVisible(int indice) {
        if (opciones[indice] == null) return false;

        if (indice == OPCION_PUNTAJE && !eleccionJugarPorPuntaje) {
            return false;
        }

        if (indice == OPCION_TIEMPO && !eleccionJugarPorTiempo) {
            return false;
        }

        if (indice == OPCION_DIFICULTAD && tipoJuegoLibre != TipoJuegoLibre.CPU) {
            return false;
        }

        return true;
    }

    private void manejarEnter() {
        switch (opcionActual) {
            case OPCION_EMPEZAR:
                if(eleccionJugarPorTiempo) {
                    estadoPartida.setTiempoRestante(tiempo * 60);
                    estadoPartida.setJugandoPorTiempo(true);
                    estadoPartida.setJugandoPorPuntaje(false);
                } else {
                    estadoPartida.setJugandoPorTiempo(false);
                    estadoPartida.setJugandoPorPuntaje(true);
                    estadoPartida.setPuntajeGanador(puntajeGanador);
                }

                estadoPartida.setJugarConObstaculos(elecionObstaculo);
                estadoPartida.setJugarConTirosEspeciales(eleccionTirosEspeciales);
                estadoPartida.setJugarConModificadores(eleccionModificador);

                juego.setScreen(new EleccionNombre(juego, tipoJuegoLibre, estadoPartida));
                break;
            case OPCION_MODIFICADORES:
                eleccionModificador = !eleccionModificador;
                checkboxes[CHECKBOX_MODIFICADORES].setChecked(eleccionModificador);
                break;
            case OPCION_TIROS_ESPECIALES:
                eleccionTirosEspeciales = !eleccionTirosEspeciales;
                checkboxes[CHECKBOX_TIROS_ESPECIALES].setChecked(eleccionTirosEspeciales);
                break;
            case OPCION_OBSTACULOS:
                elecionObstaculo = !elecionObstaculo;
                checkboxes[CHECKBOX_OBSTACULOS].setChecked(elecionObstaculo);
                break;
            case OPCION_SALIR:
                juego.setScreen(new MenuJuegoLibre(juego));
                break;
        }
        actualizarValores();
    }

    private void manejarFlechaIzquierda() {
        if (opcionActual == OPCION_CONDICION_JUEGO) {
            eleccionJugarPorTiempo = !eleccionJugarPorTiempo;
            eleccionJugarPorPuntaje = !eleccionJugarPorPuntaje;

            int anterior = opcionActual;
            reconstruirInterfaz();
            opcionActual = isOpcionVisible(anterior) ? anterior : encontrarOpcionVisibleCercana(anterior, -1);
            actualizarSeleccion();
            return;
        } else if (opcionActual == OPCION_PUNTAJE && eleccionJugarPorPuntaje && puntajeGanador > PUNTAJE_MINIMO) {
            puntajeGanador--;
        } else if (opcionActual == OPCION_TIEMPO && eleccionJugarPorTiempo && tiempo > TIEMPO_MINIMO) {
            tiempo--;
        } else if (opcionActual == OPCION_DIFICULTAD && tipoJuegoLibre == TipoJuegoLibre.CPU) {
            dificultad = dificultadAnterior(dificultad);
        }
        actualizarValores();
    }

    private void manejarFlechaDerecha() {
        if (opcionActual == OPCION_CONDICION_JUEGO) {
            eleccionJugarPorTiempo = !eleccionJugarPorTiempo;
            eleccionJugarPorPuntaje = !eleccionJugarPorPuntaje;

            int anterior = opcionActual;
            reconstruirInterfaz();
            opcionActual = isOpcionVisible(anterior) ? anterior : encontrarOpcionVisibleCercana(anterior, 1);
            actualizarSeleccion();
            return;
        } else if (opcionActual == OPCION_PUNTAJE && eleccionJugarPorPuntaje && puntajeGanador < PUNTAJE_MAXIMO) {
            puntajeGanador++;
        } else if (opcionActual == OPCION_TIEMPO && eleccionJugarPorTiempo && tiempo < TIEMPO_MAXIMO) {
            tiempo++;
        } else if (opcionActual == OPCION_DIFICULTAD && tipoJuegoLibre == TipoJuegoLibre.CPU) {
            dificultad = siguienteDificultad(dificultad);
        }
        actualizarValores();
    }

    private int encontrarOpcionVisibleCercana(int opcionOrigen, int direccion) {
        int nuevoIndice = opcionOrigen;
        for (int i = 0; i < CANT_OPCIONES; i++) {
            nuevoIndice = (nuevoIndice + direccion + CANT_OPCIONES) % CANT_OPCIONES;
            if (isOpcionVisible(nuevoIndice)) {
                return nuevoIndice;
            }
        }
        return 0;
    }

    private void actualizarValores() {
        valores[OPCION_CONDICION_JUEGO].setText(eleccionJugarPorTiempo ? "Tiempo" : "Puntaje");
        valores[OPCION_PUNTAJE].setText("Puntaje: " + puntajeGanador);
        valores[OPCION_TIEMPO].setText("Minutos: " + tiempo);
        valores[OPCION_MODIFICADORES].setText(eleccionModificador ? "Si" : "No");
        valores[OPCION_TIROS_ESPECIALES].setText(eleccionTirosEspeciales ? "Si" : "No");
        valores[OPCION_OBSTACULOS].setText(elecionObstaculo ? "Si" : "No");

        checkboxes[CHECKBOX_MODIFICADORES].setChecked(eleccionModificador);
        checkboxes[CHECKBOX_TIROS_ESPECIALES].setChecked(eleccionTirosEspeciales);
        checkboxes[CHECKBOX_OBSTACULOS].setChecked(elecionObstaculo);

        if (tipoJuegoLibre == TipoJuegoLibre.CPU && valores[OPCION_DIFICULTAD] != null) {
            valores[OPCION_DIFICULTAD].setText(dificultad.getNombre());
        }
    }

    private void reconstruirInterfaz() {
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label titulo = new Label("Opciones de juego", skin, "default");
        titulo.setFontScale(3f * escalaFuente);

        Table tituloContenedor = new Table();
        tituloContenedor.setFillParent(true);
        tituloContenedor.top();
        tituloContenedor.add(titulo).expandX().center().padTop(20);
        stage.addActor(tituloContenedor);

        for (int i = 0; i < CANT_OPCIONES; i++) {
            if (opciones[i] == null) continue;
            if (i == OPCION_PUNTAJE && !eleccionJugarPorPuntaje) continue;
            if (i == OPCION_TIEMPO && !eleccionJugarPorTiempo) continue;
            if (i == OPCION_DIFICULTAD && tipoJuegoLibre != TipoJuegoLibre.CPU) continue;

            opciones[i].setFontScale(1.5f * escalaFuente);
            table.add(opciones[i]).pad(10);

            if (valores[i] != null) {
                table.add(valores[i]).pad(10);
            }

            if (i == OPCION_MODIFICADORES || i == OPCION_TIROS_ESPECIALES || i == OPCION_OBSTACULOS) {
                table.add(checkboxes[i - 5]);
            }

            table.row();
        }

        actualizarValores();
    }

    private DificultadCPU siguienteDificultad(DificultadCPU actual) {
        switch (actual) {
            case FACIL:
                return DificultadCPU.INTERMEDIO;
            case INTERMEDIO:
                return DificultadCPU.DIFICIL;
            case DIFICIL:
                return DificultadCPU.FACIL;
        }
        return actual;
    }

    private DificultadCPU dificultadAnterior(DificultadCPU actual) {
        switch (actual) {
            case FACIL:
                return DificultadCPU.DIFICIL;
            case INTERMEDIO:
                return DificultadCPU.FACIL;
            case DIFICIL:
                return DificultadCPU.INTERMEDIO;
        }
        return actual;
    }
}
