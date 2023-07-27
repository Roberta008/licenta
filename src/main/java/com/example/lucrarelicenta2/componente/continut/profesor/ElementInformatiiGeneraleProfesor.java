package com.example.lucrarelicenta2.componente.continut.profesor;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.controllere.profesor.ControllerInformatiiGeneraleProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ElementInformatiiGeneraleProfesor extends VBox {
    private final long idElement;
    private final long idCapitol;
    private String titluElement;
    private String mesajElement;

//    private VBox containerElement = new VBox();

    public ElementInformatiiGeneraleProfesor(long idElement, long idCapitol, String titluElement, String mesajElement, Profesor profesorConectat) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.mesajElement = mesajElement;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/elemente-curs/element-informatii-generale-profesor.fxml"));
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerInformatiiGeneraleProfesor(idElement, idCapitol, titluElement, mesajElement, profesorConectat);
                }
            });
//            containerElement = fxmlLoader.load();
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getIdElement() {
        return idElement;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public String getTitluElement() {
        return titluElement;
    }

    public String getMesajElement() {
        return mesajElement;
    }

//    public VBox getContainerElement() {
//        return containerElement;
//    }


    public void setTitluElement(String titluElement) {
        this.titluElement = titluElement;
    }

    public void setMesajElement(String mesajElement) {
        this.mesajElement = mesajElement;
    }
}
