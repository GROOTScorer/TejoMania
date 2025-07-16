package io.github.grootscorer.tejomania.entidades.tirosespeciales;

import io.github.grootscorer.tejomania.entidades.Disco;

public class TiroInvisible extends TiroEspecial {
    private Disco disco = getDisco();
    public TiroInvisible() {
        if(isActivo()) {
            disco.setImagenDisco(null);
        }   else {
            disco.setImagenDisco(disco.getTexturaDisco());
        }
    }
}
