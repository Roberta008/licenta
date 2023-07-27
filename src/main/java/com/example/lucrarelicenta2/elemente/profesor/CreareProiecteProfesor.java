package com.example.lucrarelicenta2.elemente.profesor;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.controllere.profesor.ControllerCreareProiecte;
import com.example.lucrarelicenta2.controllere.profesor.ControllerCreareTemeProfesor;
import com.example.lucrarelicenta2.modele.Utilizator;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class CreareProiecteProfesor extends VBox {

    public CreareProiecteProfesor(long idCurs, long idCapitol, Stage fereastraRadacina, FlowPane containerElemente,
            List<Utilizator> listaUtilizatori) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/elemente-curs/creare-proiecte.fxml"));
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerCreareProiecte(idCurs, idCapitol, fereastraRadacina, containerElemente, listaUtilizatori);
                }
            });
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
