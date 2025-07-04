package io.github.grootscorer.tejomania.enums;

public enum Pais {
    ARGENTINA("Argentina", TipoCompetencia.LIGA, "imagenes/banderas/bandera_argentina.png"),
    BRASIL("Brasil", TipoCompetencia.LIGA, "imagenes/banderas/bandera_brasil.png"),
    CHINA("China", TipoCompetencia.LIGA, "imagenes/banderas/bandera_china.png"),
    ESTADOS_UNIDOS("Estados Unidos", TipoCompetencia.LIGA, "imagenes/banderas/bandera_estados_unidos.png"),
    RUSIA("Rusia", TipoCompetencia.LIGA, "imagenes/banderas/bandera_rusia.png"),
    ALEMANIA("Alemania", TipoCompetencia.LIGA, "imagenes/banderas/bandera_alemania.png"),
    ITALIA("Italia", TipoCompetencia.TORNEO, "imagenes/banderas/bandera_italia.png"),
    NIGERIA("Nigeria", TipoCompetencia.LIGA, "imagenes/banderas/bandera_nigeria.png"),
    JAPON("Japon", TipoCompetencia.LIGA, "imagenes/banderas/bandera_japon.png"),
    FRANCIA("Francia", TipoCompetencia.TORNEO, "imagenes/banderas/bandera_francia.png"),
    REINO_UNIDO("Reino Unido", TipoCompetencia.LIGA, "imagenes/banderas/bandera_reino_unido.png"),
    URUGUAY("Uruguay", TipoCompetencia.TORNEO, "imagenes/banderas/bandera_uruguay.png"),
    AUSTRALIA("Australia", TipoCompetencia.LIGA, "imagenes/banderas/bandera_australia.png"),
    INDIA("India", TipoCompetencia.TORNEO, "imagenes/banderas/bandera_india.png"),
    SUDAFRICA("Sudafrica", TipoCompetencia.TORNEO,  "imagenes/banderas/bandera_sudafrica.png"),
    INDONESIA("Indonesia", TipoCompetencia.TORNEO, "imagenes/banderas/bandera_indonesia.png");

    private String nombre;
    private TipoCompetencia tipoCompetencia;
    private String textura;

    Pais(String nombre, TipoCompetencia tipoCompetencia, String textura) {
        this.nombre = nombre;
        this.tipoCompetencia = tipoCompetencia;
        this.textura = textura;
    }

    public String getnombre() {
        return this.nombre;
    }

    public TipoCompetencia getTipoTorneo() {
        return tipoCompetencia;
    }

    public String getTextura() {
        return textura;
    }
}
