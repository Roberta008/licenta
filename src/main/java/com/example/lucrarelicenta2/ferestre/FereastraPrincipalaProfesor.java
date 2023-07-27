package com.example.lucrarelicenta2.ferestre;

import static com.example.lucrarelicenta2.ferestre.FereastraPrincipala.INALTIME_FEREASTRA;
import static com.example.lucrarelicenta2.ferestre.FereastraPrincipala.LATIME_FEREASTRA;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.controllere.profesor.ControllerFereastraPrincipalaProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FereastraPrincipalaProfesor {

    private final Profesor profesorConectat;
    private Scene scenaFereastraProfesor;

    public FereastraPrincipalaProfesor(Profesor profesorConectat, Stage fereastraRadacina) {
        this.profesorConectat = profesorConectat;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/pagina-principala-profesor.fxml"));
        try {
            fxmlLoader.setControllerFactory(aClass -> new ControllerFereastraPrincipalaProfesor(profesorConectat, fereastraRadacina));
            scenaFereastraProfesor = new Scene(fxmlLoader.load(), INALTIME_FEREASTRA, LATIME_FEREASTRA);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Scene getScenaFereastraProfesor() {
        return scenaFereastraProfesor;
    }
}
