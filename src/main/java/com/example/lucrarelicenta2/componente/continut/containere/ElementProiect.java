package com.example.lucrarelicenta2.componente.continut.containere;

import com.example.lucrarelicenta2.controllere.ControllerElementProiect;
import com.example.lucrarelicenta2.modele.Utilizator;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ElementProiect {
    private final long idProiect;
    private final long idCapitol;
    private final String titluProiect;
    private final Timestamp termenPredate;
    private final List<String> listaObiectiveProiect;
    private final String statusProiect;
    private VBox containerProiect;

    public ElementProiect(Stage radacinaAplicatiei, long idProiect, long idCapitol, String titluProiect,
                          Timestamp termenPredare, List<String> listaObiectiveProiect, String statusProiect, Utilizator utilizatorAutentificat) {
        this.idProiect = idProiect;
        this.idCapitol = idCapitol;
        this.titluProiect = titluProiect;
        this.termenPredate = termenPredare;
        this.listaObiectiveProiect = listaObiectiveProiect;
        this.statusProiect = statusProiect;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-proiect.fxml"));
        try {
            fxmlLoader.setControllerFactory(aClass -> new ControllerElementProiect(radacinaAplicatiei, idProiect, idCapitol,
                    titluProiect, termenPredare, listaObiectiveProiect, statusProiect, utilizatorAutentificat));
            containerProiect = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox getContainerProiect() {
        return containerProiect;
    }

    public long getIdProiect() {
        return idProiect;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public String getTitluProiect() {
        return titluProiect;
    }

    public Timestamp getTermenPredate() {
        return termenPredate;
    }

    public List<String> getListaObiectiveProiect() {
        return listaObiectiveProiect;
    }

    public String getStatusProiect() {
        return statusProiect;
    }
}
