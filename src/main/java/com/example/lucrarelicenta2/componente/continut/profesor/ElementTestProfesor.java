package com.example.lucrarelicenta2.componente.continut.profesor;

import com.example.lucrarelicenta2.controllere.ControllerElementTest;
import com.example.lucrarelicenta2.controllere.profesor.ControllerTestProfesor;
import com.example.lucrarelicenta2.modele.Intrebare;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ElementTestProfesor extends VBox {
    private final long idElement;
    private final long idCapitol;
    private final String titluElement;
    private final List<Intrebare> listaIntrebari;

    public ElementTestProfesor(long idTest, long idCapitol, String titluTest, List<Intrebare> listaIntrebari) {
        this.idElement = idTest;
        this.idCapitol = idCapitol;
        this.titluElement = titluTest;
        this.listaIntrebari = listaIntrebari;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-test-profesor.fxml"));
        try {
            fxmlLoader.setControllerFactory(aClass -> new ControllerTestProfesor(idElement, idCapitol, titluElement,
                    listaIntrebari));
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
