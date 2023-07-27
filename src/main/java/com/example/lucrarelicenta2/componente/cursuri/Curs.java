package com.example.lucrarelicenta2.componente.cursuri;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Curs {
    private final long idCurs;
    private final String titluCurs;
    private final String descriereCurs;
    private final String numePrenumeProfesor;
    private final List<String> listaLectii;
    private final List<String> listaTeme;

    private final VBox containerCurs, containerInformatiiCurs, containerInformatiiTeme, containerOptiuni;

    public Curs(long idCurs, String titluCurs, String descriereCurs, String numePrenumeProfesor, List<String> listaLectii, String... titluriTeme) {
        this.idCurs = idCurs;
        this.titluCurs = titluCurs;
        this.descriereCurs = descriereCurs;
        this.numePrenumeProfesor = numePrenumeProfesor;
        this.listaTeme = new ArrayList<>(of(titluriTeme));
        this.containerCurs = new VBox();
        this.containerInformatiiCurs = new VBox();
        this.containerInformatiiCurs.setId("containerInformatiiCurs");
        this.containerOptiuni = new VBox();
        this.containerInformatiiTeme = new VBox();

        this.listaLectii = listaLectii;


        TextFlow textFlowTitluCurs = new TextFlow();
        textFlowTitluCurs.setId("labelTitluCurs");
        Text textTitluCurs = new Text(titluCurs);
        textTitluCurs.setStyle("-fx-fill: #cc33ff; -fx-font-weight: bold; -fx-font-size: 16");
        textFlowTitluCurs.setTextAlignment(TextAlignment.CENTER);
        textFlowTitluCurs.getChildren().add(textTitluCurs);
        containerInformatiiCurs.getChildren().add(textFlowTitluCurs);



        TextFlow textFlowDescriere = new TextFlow();
        textFlowDescriere.setTextAlignment(TextAlignment.CENTER);
        Text textDescriereCurs = new Text(descriereCurs);
        textDescriereCurs.setStyle("-fx-text-fill: #878787");
        textFlowDescriere.getChildren().add(textDescriereCurs);
        this.containerInformatiiCurs.getChildren().add(textFlowDescriere);



        Label labelNumeProfesor = new Label(this.numePrenumeProfesor);
        labelNumeProfesor.setWrapText(true);
        labelNumeProfesor.setTextAlignment(TextAlignment.RIGHT);
        this.containerInformatiiCurs.getChildren().add(labelNumeProfesor);


        this.containerInformatiiCurs.setPrefHeight(100.0);
        this.containerInformatiiTeme.setPrefHeight(100.0);
        this.containerOptiuni.setPrefHeight(220.0 - this.containerInformatiiCurs.getPrefHeight() - this.containerInformatiiTeme.getPrefHeight());


        this.containerInformatiiTeme.getChildren().addAll(listaTeme.stream().map(Label::new).toList());
        this.containerCurs.getChildren().addAll(this.containerInformatiiCurs, this.containerInformatiiTeme, this.containerOptiuni);


        this.containerCurs.setPrefWidth(200.0);
        this.containerCurs.setPrefHeight(220.0);
        this.containerCurs.getStylesheets().add("/style.css");
        this.containerCurs.getStyleClass().add("containerCurs");
        this.containerInformatiiCurs.getStyleClass().add("containerCursInformatiiCurs");
        this.containerInformatiiTeme.getStyleClass().add("containerCursInformatiiTeme");
        this.containerOptiuni.getStyleClass().add("containerCursOptiuni");


        VBox.setVgrow(this.containerInformatiiCurs, Priority.ALWAYS);
        VBox.setVgrow(this.containerInformatiiTeme, Priority.ALWAYS);
        VBox.setVgrow(this.containerOptiuni, Priority.ALWAYS);



        FlowPane.setMargin(containerCurs, new Insets(10.0, 0.0, 0.0, 10.0));

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), containerCurs);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.millis(200), containerCurs);
        scaleOutTransition.setFromX(1.05);
        scaleOutTransition.setFromY(1.05);
        scaleOutTransition.setToX(1.0);
        scaleOutTransition.setToY(1.0);

        containerCurs.setOnMouseEntered(mouseEvent -> scaleTransition.playFromStart());
        containerCurs.setOnMouseExited(mouseEvent -> scaleOutTransition.playFromStart());
    }

    public VBox getContainerCurs() {
        return containerCurs;
    }

    public List<String> getListaLectii() {
        return listaLectii;
    }

    public List<String> getListaTeme() {
        return listaTeme;
    }

    public String getTitluCurs() {
        return titluCurs;
    }

    public String getDescriereCurs ()
    {
        return descriereCurs;
    }

    public long getIdCurs ()
    {
        return idCurs;
    }
}
