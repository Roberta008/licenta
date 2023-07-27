package com.example.lucrarelicenta2.elemente;

import java.io.InputStream;

public class FisierMaterialeSuport {

    private final long idFisier;
    private final long idCapitol;
    private final String numeFisier;
    private final InputStream continutFisier;

    public FisierMaterialeSuport(long idFisier, long idCapitol, String numeFisier, InputStream continutFisier) {
        this.idFisier = idFisier;
        this.idCapitol = idCapitol;
        this.numeFisier = numeFisier;
        this.continutFisier = continutFisier;
    }

    public long getIdFisier() {
        return idFisier;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public String getNumeFisier() {
        return numeFisier;
    }

    public InputStream getContinutFisier() {
        return continutFisier;
    }

}
