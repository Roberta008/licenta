package com.example.lucrarelicenta2.elemente.profesor;

import org.kordamp.ikonli.javafx.FontIcon;

import java.io.InputStream;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class ProiectTrimisProfesor extends VBox {

    private final long idProiect;
    private final long idFisier;
    private final long idUtilizator;
    private final String numeFisier;
    private final InputStream continutFisier;
    private final String numeUtilizator;
    private final String numeFamilie;
    private final String prenumeUtilizator;

    public ProiectTrimisProfesor(long idProiect, long idFisier, long idUtilizator,
            String numeFisier, InputStream continutFisier, String numeUtilizator,
            String numeFamilie, String prenumeUtilizator) {
        super();
        this.idProiect = idProiect;
        this.idFisier = idFisier;
        this.idUtilizator = idUtilizator;
        this.numeFisier = numeFisier;
        this.continutFisier = continutFisier;
        this.numeUtilizator = numeUtilizator;
        this.numeFamilie = numeFamilie;
        this.prenumeUtilizator = prenumeUtilizator;

        TextFlow textFlowPrimaParte = new TextFlow();
        textFlowPrimaParte.setTextAlignment(TextAlignment.CENTER);
        Text textPrimaParte1 = new Text(numeFamilie + " " + prenumeUtilizator);
        textPrimaParte1.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill: #b3b3b3; -fx-font-weight: bold");
        Text textPrimaParte2 = new Text(" ( ");
        textPrimaParte2.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill:  #b3b3b3");
        Text textPrimaParte3 = new Text(numeUtilizator);
        textPrimaParte3.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #cc33ff");
        Text textPrimaParte4 = new Text(" ) ");
        textPrimaParte4.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill:  #b3b3b3");
        textFlowPrimaParte.getChildren().addAll(textPrimaParte1, textPrimaParte2, textPrimaParte3, textPrimaParte4);

        Label labelFisier = creazaLabelTitluFisier(numeFisier);

        setAlignment(Pos.CENTER);

//        VBox vBox = new VBox(textFlowPrimaParte, labelFisier);
//        vBox.setAlignment(Pos.CENTER);
//        vBox.setMaxWidth(300.0);
//        vBox.setStyle("-fx-background-color: #f7e6ff; -fx-background-radius: 25; -fx-padding: 10; " +
//                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1)");

        getChildren().addAll(textFlowPrimaParte, labelFisier);
        setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-padding: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1); -fx-cursor: hand");
        setMaxWidth(300.0);

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

    private Label creazaLabelTitluFisier(String numeFisier) {
        Label labelNumeFisier = new Label(numeFisier);
        FontIcon iconFisier = new FontIcon("anto-file");
        iconFisier.setIconColor(Color.valueOf("#b3b3b3"));
        iconFisier.setIconSize(25);
        labelNumeFisier.setGraphic(iconFisier);
        labelNumeFisier.setContentDisplay(ContentDisplay.TOP);
        labelNumeFisier.setStyle("-fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #cc33ff");
        return labelNumeFisier;
    }

    public long getIdProiect() {
        return idProiect;
    }

    public long getIdFisier() {
        return idFisier;
    }

    public long getIdUtilizator() {
        return idUtilizator;
    }

    public String getNumeFisier() {
        return numeFisier;
    }

    public InputStream getContinutFisier() {
        return continutFisier;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public String getNumeFamilie() {
        return numeFamilie;
    }

    public String getPrenumeUtilizator() {
        return prenumeUtilizator;
    }
}
