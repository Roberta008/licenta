package com.example.lucrarelicenta2.componente.continut.containere;

import com.example.lucrarelicenta2.controllere.ControllerElementInformatieGenerala;
import com.example.lucrarelicenta2.modele.Utilizator;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ElementInformatieGenerala {
    private final long idElement;
    private final long idCapitol;
    private final String titluElement;
    private final String mesajElement;
    private VBox containerProiect;

    public ElementInformatieGenerala(long idElement, long idCapitol, String titluElement,
                                     String mesajElement, Utilizator utilizatorAutentificat) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.mesajElement = mesajElement;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-informatii-generale.fxml"));
        try {
            fxmlLoader.setControllerFactory(aClass -> new ControllerElementInformatieGenerala(idElement, idCapitol,
                    titluElement, mesajElement, utilizatorAutentificat));
            containerProiect = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox getContainerProiect() {
        return containerProiect;
    }

    public long getIdElement() {
        return idElement;
    }

    public String getTitluElement() {
        return titluElement;
    }

    public String getMesajElement() {
        return mesajElement;
    }
}
