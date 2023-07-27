package com.example.lucrarelicenta2.componente.cursuri;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class CursProfesor extends VBox {
    private final long idCurs;
    private final String titluCurs;
    private final String descriereCurs;

    public CursProfesor(long idCurs, String titluCurs, String descriereCurs) {
        this.idCurs = idCurs;
        this.titluCurs = titluCurs;
        this.descriereCurs = descriereCurs;
        Label labelTitlu = new Label(titluCurs);
        labelTitlu.setStyle("-fx-text-fill: #cc33ff; -fx-font-size: 18; -fx-font-style: italic; -fx-font-weight: bold");
        labelTitlu.setWrapText(true);
        labelTitlu.setTextAlignment(TextAlignment.CENTER);
        TextFlow textFlowDescriere = new TextFlow();
        Text textDescriere = new Text(descriereCurs);
        textDescriere.setStyle("-fx-font-style: italic; -fx-fill: #bfbfbf");
        textFlowDescriere.getChildren().add(textDescriere);
        setStyle("-fx-background-color: white; -fx-padding: 50 40; -fx-text-fill: #cc33ff; " +
                " -fx-background-radius: 25; -fx-cursor: hand; -fx-font-size: 14; -fx-font-style: italic; -fx-font-weight: bold");
        getChildren().addAll(labelTitlu, textFlowDescriere);
        setMaxWidth(450.0);

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

    public long getIdCurs() {
        return idCurs;
    }

    public String getTitluCurs() {
        return titluCurs;
    }

    public String getDescriereCurs() {
        return descriereCurs;
    }
}
