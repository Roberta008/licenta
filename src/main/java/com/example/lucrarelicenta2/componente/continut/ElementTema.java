package com.example.lucrarelicenta2.componente.continut;

import com.example.lucrarelicenta2.controllere.ControllerElementTema;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;

public class ElementTema {
    private final long idElement;
    private final String titluElement;
    private final long idCapitol;
    private final Timestamp dataPostarii;
    private final Timestamp termenLimita;
    private final String taskElement;
    private final Utilizator utilizatorAutentificat;
    private final Stage radacinaAplicatiei;
    private VBox vBoxElement;

    public ElementTema(long idElement, String titluElement, long idCapitol, Timestamp dataPostarii,
                       Timestamp termenLimita, String taskElement, Utilizator utilizatorAutentificat, Stage radacinaAplicatiei) {
        this.idElement = idElement;
        this.titluElement = titluElement;
        this.idCapitol = idCapitol;
        this.dataPostarii = dataPostarii;
        this.termenLimita = termenLimita;
        this.taskElement = taskElement;
        this.utilizatorAutentificat = utilizatorAutentificat;
        this.radacinaAplicatiei = radacinaAplicatiei;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-tema.fxml"));
        try {
            fxmlLoader.setControllerFactory(aClass -> new ControllerElementTema(idElement, titluElement, idCapitol,
                    dataPostarii, termenLimita, taskElement, utilizatorAutentificat, radacinaAplicatiei));
            vBoxElement = fxmlLoader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public VBox getContainerTema() {
        return vBoxElement;
    }

    public String getTitluElement() {
        return titluElement;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public Timestamp getDataPostarii() {
        return dataPostarii;
    }

    public String getTaskElement() {
        return taskElement;
    }


    public Utilizator getUtilizatorAutentificat() {
        return utilizatorAutentificat;
    }

    public VBox getvBoxElement() {
        return vBoxElement;
    }
}
