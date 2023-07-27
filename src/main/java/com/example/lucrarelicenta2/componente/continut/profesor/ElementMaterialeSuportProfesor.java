package com.example.lucrarelicenta2.componente.continut.profesor;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.controllere.profesor.ControllerMaterialeSuportProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ElementMaterialeSuportProfesor extends VBox {
    private final long idElement;
    private final long idCapitol;
    private final String titluElement;
    private final String mesajElement;

    public ElementMaterialeSuportProfesor(long idElement, long idCapitol, String titluElement,
            String mesajElement, Profesor profesorConectat, Stage fereastraRadacina) {
        super();
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.mesajElement = mesajElement;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/elemente-curs/element-materiale-suport-profesor.fxml"));
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerMaterialeSuportProfesor(idElement, idCapitol, titluElement,
                            mesajElement, profesorConectat, fereastraRadacina);
                }
            });
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

}
