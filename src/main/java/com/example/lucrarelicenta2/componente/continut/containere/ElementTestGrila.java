package com.example.lucrarelicenta2.componente.continut.containere;

import com.example.lucrarelicenta2.controllere.ControllerElementTest;
import com.example.lucrarelicenta2.modele.Intrebare;
import com.example.lucrarelicenta2.modele.Utilizator;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ElementTestGrila {
    private final long idElement;
    private final long idCapitol;
    private final String titluElement;
    private final List<Intrebare> listaIntrebari;
    private VBox containerProiect;

    public ElementTestGrila(long idElement, long idCapitol, String titluElement, List<Intrebare> listaIntrebari, Utilizator utilizatorAutentificat) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.listaIntrebari = listaIntrebari;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-test.fxml"));
        try {
            fxmlLoader.setControllerFactory(aClass -> new ControllerElementTest(idElement, idCapitol, titluElement, listaIntrebari, utilizatorAutentificat));
            containerProiect = fxmlLoader.load();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public VBox getContainerProiect() {
        return containerProiect;
    }

    public long getIdElement() {
        return idElement;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public List<Intrebare> getListaIntrebari() {
        return listaIntrebari;
    }

    public String getTitluElement() {
        return titluElement;
    }
}
