package com.example.lucrarelicenta2.elemente.profesor;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.controllere.profesor.ControllerCreareMaterialeSuport;
import com.example.lucrarelicenta2.controllere.profesor.ControllerCreareTemeProfesor;
import com.example.lucrarelicenta2.modele.Profesor;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Date;

public class CreareTemeProfesor extends VBox {

    public CreareTemeProfesor(long idCapitol, Profesor profesorConectat, Stage fereastraRadacina, FlowPane containerElemente) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/elemente-curs/creare-teste.fxml"));
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerCreareTemeProfesor(idCapitol, profesorConectat, fereastraRadacina, containerElemente);
                }
            });
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
