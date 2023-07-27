package com.example.lucrarelicenta2.elemente;

import java.io.InputStream;

public class FisierProiect {
    private final long idProiect;
    private final String numeFisier;
    private final InputStream continutFisier;

    public FisierProiect(long idProiect, String numeFisier, InputStream continutFisier) {
        this.idProiect = idProiect;
        this.numeFisier = numeFisier;
        this.continutFisier = continutFisier;
    }

    public long getIdProiect() {
        return idProiect;
    }

    public String getNumeFisier() {
        return numeFisier;
    }

    public InputStream getContinutFisier() {
        return continutFisier;
    }

}
