package com.example.lucrarelicenta2.modele;

import java.util.List;

public class Intrebare {
    private final long idIntrebare;
    private final String textIntrebare;
    private final List<String> listaRaspunsuriIntrebare;
    private final String raspunsCorect;


    public Intrebare(long idIntrebare, String textIntrebare, List<String> listaRaspunsuriIntrebare, String raspunsCorect) {
        this.idIntrebare = idIntrebare;
        this.textIntrebare = textIntrebare;
        this.listaRaspunsuriIntrebare = listaRaspunsuriIntrebare;
        this.raspunsCorect = raspunsCorect;
    }

    public long getIdIntrebare() {
        return idIntrebare;
    }

    public List<String> getListaRaspunsuriIntrebare() {
        return listaRaspunsuriIntrebare;
    }

    public String getRaspunsCorect() {
        return raspunsCorect;
    }

    public String getTextIntrebare() {
        return textIntrebare;
    }
}
