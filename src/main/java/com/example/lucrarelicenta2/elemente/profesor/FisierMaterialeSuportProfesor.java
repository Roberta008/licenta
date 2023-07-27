package com.example.lucrarelicenta2.elemente.profesor;

import org.kordamp.ikonli.javafx.FontIcon;

import java.io.InputStream;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FisierMaterialeSuportProfesor extends VBox {

    private final long idFisier;
    private final long idCapitol;
    private final String numeFisier;
    private final InputStream continutFisier;

    public FisierMaterialeSuportProfesor(long idFisier, long idCapitol, String numeFisier, InputStream continutFisier) {
        super();
        this.idFisier = idFisier;
        this.idCapitol = idCapitol;
        this.numeFisier = numeFisier;
        this.continutFisier = continutFisier;

        Label labelNumeFisier = creazaLabelTitluFisier(numeFisier);
        getChildren().add(labelNumeFisier);

        setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 25; " +
                "-fx-effect:  dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1); -fx-cursor: hand");
        setPrefWidth(150.0);
        setAlignment(Pos.CENTER);
        adaugaAnimatie();
        adaugaTooltip();
    }

    private Label creazaLabelTitluFisier(String numeFisier) {
        Label labelNumeFisier = new Label(numeFisier);
        FontIcon iconFisier = new FontIcon("anto-file");
        iconFisier.setIconColor(Color.valueOf("#b3b3b3"));
        iconFisier.setIconSize(40);
        labelNumeFisier.setGraphic(iconFisier);
        labelNumeFisier.setContentDisplay(ContentDisplay.TOP);
        labelNumeFisier.setStyle("-fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: #cc33ff");
        return labelNumeFisier;
    }

    private void adaugaTooltip() {
        Tooltip tooltipFisier = new Tooltip("Apasa dublu click pentru a sterge fisierul!");
        tooltipFisier.setStyle("-fx-background-color: #f7e6ff; -fx-text-fill: #cc33ff; -fx-font-size: 18; fx-font-style: italic");
        Tooltip.install(this, tooltipFisier);
    }

    private void adaugaAnimatie() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), this);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.millis(200), this);
        scaleOutTransition.setFromX(1.05);
        scaleOutTransition.setFromY(1.05);
        scaleOutTransition.setToX(1.0);
        scaleOutTransition.setToY(1.0);

        this.setOnMouseEntered(mouseEvent -> scaleTransition.playFromStart());
        this.setOnMouseExited(mouseEvent -> scaleOutTransition.playFromStart());
    }

    public long getIdFisier() {
        return idFisier;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public String getNumeFisier() {
        return numeFisier;
    }

    public InputStream getContinutFisier() {
        return continutFisier;
    }


}
