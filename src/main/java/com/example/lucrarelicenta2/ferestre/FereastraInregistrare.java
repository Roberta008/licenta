package com.example.lucrarelicenta2.ferestre;

import com.example.lucrarelicenta2.controllere.ControllerInregistrare;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class FereastraInregistrare {
    private static final double INALTIME_FEREASTRA = 1380;
    private static final double LATIME_FEREASTRA = 820;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Stage fereastraRadacina;
    private Scene scenaInregistrare;

    public FereastraInregistrare() {
    }

    @SuppressWarnings({"Convert2Lambda", "Convert2Diamond"})
    public FereastraInregistrare(Stage fereastraRadacina) {
        this.fereastraRadacina = fereastraRadacina;
        FXMLLoader loaderFereastraInregistrare = new FXMLLoader(getClass().getResource("/register-view.fxml"));
        loaderFereastraInregistrare.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> aClass) {
                return new ControllerInregistrare(fereastraRadacina);
            }
        });
        try {
            scenaInregistrare = new Scene(loaderFereastraInregistrare.load(), INALTIME_FEREASTRA, LATIME_FEREASTRA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScenaInregistrare() {
        return scenaInregistrare;
    }
}
