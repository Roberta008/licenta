package com.example.lucrarelicenta2.modele;

public class Profesor {
    private final long idProfesor;
    private final String numeProfesor, prenumeProfesor, titluProfesor, usernameProfesor, parolaProfesor;

    public Profesor(long idProfesor, String numeProfesor, String prenumeProfesor, String titluProfesor,
                    String usernameProfesor, String parolaProfesor) {
        this.idProfesor = idProfesor;
        this.numeProfesor = numeProfesor;
        this.prenumeProfesor = prenumeProfesor;
        this.titluProfesor = titluProfesor;
        this.usernameProfesor = usernameProfesor;
        this.parolaProfesor = parolaProfesor;
    }

    public long getIdProfesor() {
        return idProfesor;
    }

    public String getNumeProfesor() {
        return numeProfesor;
    }

    public String getPrenumeProfesor() {
        return prenumeProfesor;
    }

    public String getTitluProfesor() {
        return titluProfesor;
    }

    public String getUsernameProfesor() {
        return usernameProfesor;
    }

    public String getParolaProfesor() {
        return parolaProfesor;
    }
}
