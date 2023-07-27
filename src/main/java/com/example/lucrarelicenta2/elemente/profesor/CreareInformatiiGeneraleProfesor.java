package com.example.lucrarelicenta2.elemente.profesor;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.controllere.profesor.ControllerCreareInformatiiGenerale;
import com.example.lucrarelicenta2.modele.Profesor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class CreareInformatiiGeneraleProfesor extends VBox {

    public CreareInformatiiGeneraleProfesor(long idCapitol, Profesor profesorConectat, FlowPane containerContinutCurs) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/elemente-curs/creare-informatii-generale.fxml"));
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerCreareInformatiiGenerale(idCapitol, profesorConectat, containerContinutCurs);
                }
            });
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
