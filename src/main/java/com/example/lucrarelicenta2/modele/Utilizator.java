package com.example.lucrarelicenta2.modele;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class Utilizator {
    private final long idUtilizator;
    private final String numeUtilizator;
    private final String numeFamilie;
    private final String prenumeUtilizator;
    private final String parolaUtilizator;
    private final String emailUtilizator;

//    private final List<Cursuri> listaCursuriInscris

    public Utilizator(long idUtilizator, String numeUtilizator, String numeFamilie, String prenumeUtilizator, String parolaUtilizator, String emailUtilizator) {
        this.idUtilizator = idUtilizator;
        this.numeUtilizator = numeUtilizator;
        this.numeFamilie = numeFamilie;
        this.prenumeUtilizator = prenumeUtilizator;
        this.parolaUtilizator = parolaUtilizator;
        this.emailUtilizator = emailUtilizator;
    }

    public long getIdUtilizator() {
        return idUtilizator;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public String getNumeFamilie() {
        return numeFamilie;
    }

    public String getPrenumeUtilizator() {
        return prenumeUtilizator;
    }

    public String getParolaUtilizator() {
        return parolaUtilizator;
    }

    public String getEmailUtilizator() {
        return emailUtilizator;
    }

    @Override
    public String toString() {
        return numeFamilie + " " + prenumeUtilizator + " ( " + numeUtilizator + " )";
    }
}
