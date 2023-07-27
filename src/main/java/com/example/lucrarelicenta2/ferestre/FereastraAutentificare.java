package com.example.lucrarelicenta2.ferestre;

import com.example.lucrarelicenta2.controllere.ControllerAutentificare;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class FereastraAutentificare {
    private static final double INALTIME_FEREASTRA = 1380;
    private static final double LATIME_FEREASTRA = 820;
    private Scene scenaAutentificare;

    public FereastraAutentificare() {

    }

    @SuppressWarnings({"Convert2Lambda", "Convert2Diamond"})
    public FereastraAutentificare(Stage fereastraRadacina) {
        FXMLLoader loaderFereastraInregistrare = new FXMLLoader(getClass().getResource("/login-view.fxml"));
        loaderFereastraInregistrare.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> aClass) {
                return new ControllerAutentificare(fereastraRadacina);
            }
        });
        try {
            scenaAutentificare = new Scene(loaderFereastraInregistrare.load(), INALTIME_FEREASTRA, LATIME_FEREASTRA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScenaAutentificare() {
        return scenaAutentificare;
    }
}
