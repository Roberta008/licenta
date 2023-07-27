package com.example.lucrarelicenta2.componente.continut.profesor;

import com.example.lucrarelicenta2.Main;
import com.example.lucrarelicenta2.controllere.profesor.ControllerProiecteProfesor;

import java.io.IOException;
import java.sql.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ElementProiectProfesor extends VBox {

    private final long idProiect;
    private final long idCurs;
    private final long idCapitol;
    private final String titluProiect;
    private final Date termenPredare;

    public ElementProiectProfesor(long idProiect, long idCurs, long idCapitol, String titluProiect, Date termenPredare, Stage fereastraRadacina) {
        this.idProiect = idProiect;
        this.idCurs = idCurs;
        this.idCapitol = idCapitol;
        this.titluProiect = titluProiect;
        this.termenPredare = termenPredare;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/elemente-curs/element-proiect-profesor.fxml"));
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerProiecteProfesor(idProiect, idCurs, idCapitol, titluProiect, termenPredare, fereastraRadacina);
                }
            });
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getIdProiect() {
        return idProiect;
    }

    public long getIdCurs() {
        return idCurs;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public String getTitluProiect() {
        return titluProiect;
    }

    public Date getTermenPredare() {
        return termenPredare;
    }
}
