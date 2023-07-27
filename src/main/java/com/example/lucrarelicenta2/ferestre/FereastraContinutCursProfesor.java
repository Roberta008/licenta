package com.example.lucrarelicenta2.ferestre;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.componente.cursuri.CursProfesor;
import com.example.lucrarelicenta2.controllere.profesor.ControllerFereastraContinutCursProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FereastraContinutCursProfesor {

    private ScrollPane containerContinutCurs;

    public FereastraContinutCursProfesor(Profesor profesorConectat, CursProfesor cursCurent, Stage fereastraRadacina) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fereastra-continut-curs-profesor.fxml"));
//        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerFereastraContinutCursProfesor(profesorConectat, cursCurent, fereastraRadacina);
                }
            });
            containerContinutCurs = fxmlLoader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public ScrollPane getContainerContinutCurs() {
        return containerContinutCurs;
    }
}
