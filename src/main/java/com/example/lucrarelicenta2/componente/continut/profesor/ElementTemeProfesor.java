package com.example.lucrarelicenta2.componente.continut.profesor;

import com.example.lucrarelicenta2.controllere.profesor.ControllerTemeProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.io.IOException;
import java.sql.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ElementTemeProfesor extends VBox {
    private final long idTema;
    private final long idCapitol;
    private final String titluTema;
    private final String cerintaTema;
    private final Date termenCreare;
    private final Date termenLimita;
    private final Profesor profesorConectat;

    public ElementTemeProfesor(long idTema, long idCapitol, String titluTema, String cerintaTema,
            Date termenCreare, Date termenLimita, Profesor profesorConectat, Stage fereastraRadacina) {
        super();
        this.idTema = idTema;
        this.idCapitol = idCapitol;
        this.titluTema = titluTema;
        this.cerintaTema = cerintaTema;
        this.termenCreare = termenCreare;
        this.termenLimita = termenLimita;
        this.profesorConectat = profesorConectat;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-teme-profesor.fxml"));
        try {
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    return new ControllerTemeProfesor(idTema, idCapitol, titluTema, cerintaTema,
                            termenCreare, termenLimita, profesorConectat, fereastraRadacina);
                }
            });
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
            adaugaTooltip();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adaugaTooltip() {
        Tooltip tooltipFisier = new Tooltip("Apasa dublu click pe nota pentru a o modifica sau click pe fisier pentru a-l descarca");
        tooltipFisier.setStyle("-fx-background-color: #f7e6ff; -fx-text-fill: #cc33ff; -fx-font-size: 18; fx-font-style: italic");
        Tooltip.install(this, tooltipFisier);
    }

}
