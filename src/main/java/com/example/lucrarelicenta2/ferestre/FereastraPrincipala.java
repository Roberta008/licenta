package com.example.lucrarelicenta2.ferestre;

import com.example.lucrarelicenta2.controllere.ControllerFereastraPrincipala;
import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.modele.Utilizator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//TODO: vezi cum poti scapa de aceste warninguri!
public class FereastraPrincipala {

    public static final int INALTIME_FEREASTRA = 1380;
    public static final int LATIME_FEREASTRA = 820;
    private Scene mainScene;

    public FereastraPrincipala() {

    }

    public FereastraPrincipala(Stage scenaRadacina, Utilizator utilizatorAutentificat) {
        FXMLLoader loaderFereastraPrincipala = new FXMLLoader(Main.class.getResource("/pagina-principala.fxml"));
        try {
            loaderFereastraPrincipala.setControllerFactory(clasaController -> new ControllerFereastraPrincipala(scenaRadacina, utilizatorAutentificat));
            mainScene = new Scene(loaderFereastraPrincipala.load(), INALTIME_FEREASTRA, LATIME_FEREASTRA);
            //scenaRadacina.setTitle("Pagina principala");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Scene getMainScene() {
        return mainScene;
    }
}
