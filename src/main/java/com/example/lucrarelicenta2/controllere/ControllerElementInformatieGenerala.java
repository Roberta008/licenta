package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.modele.Utilizator;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ControllerElementInformatieGenerala implements Initializable {
    private final long idElement, idCapitol;
    private final String titluElement, mesajElement;
    private List<Label> listaMesajeUtilizatori;
    private final Utilizator utilizatorAutentificat;
    @FXML
    private Label labelTitlu, labelMesaj;
    @FXML
    private VBox containerElement, containerMesaje;
    @FXML
    private TextField fieldMesaj;
    @FXML
    private Button butonTrimiteMesaj;

    public ControllerElementInformatieGenerala(long idElement, long idCapitol, String titluElement, String mesajElement, Utilizator utilizatorAutentificat) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.mesajElement = mesajElement;
        this.listaMesajeUtilizatori = new ArrayList<>();
        this.utilizatorAutentificat = utilizatorAutentificat;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelTitlu.setText(labelTitlu.getText() + titluElement);
        labelMesaj.setText(mesajElement);

        ObservableList<Node> copiiContainerMesaje = containerMesaje.getChildren();
        try {
            BazaDeDate obiectBD = new BazaDeDate();
            List<Label> listaMesajeElement = obiectBD.preluareMesajeElement(idCapitol, idElement);
            List<Label> mesajeProfesor = obiectBD.preluareMesajeProfesor(idCapitol, idElement, utilizatorAutentificat.getIdUtilizator());
            mesajeProfesor.sort(Comparator.comparing(Labeled::getText));
            listaMesajeElement.addAll(mesajeProfesor);
            for (Label labelMesajCurent : listaMesajeElement) {
                String textMasaj = labelMesajCurent.getText();
                if (textMasaj.contains(utilizatorAutentificat.getNumeFamilie()) && textMasaj.contains(utilizatorAutentificat.getPrenumeUtilizator())) {
                    labelMesajCurent.setMaxWidth(Double.MAX_VALUE);
                    labelMesajCurent.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 5 10; -fx-background-radius: 25; " +
                            "-fx-alignment: center-right; -fx-font-size: 16; -fx-font-style: italic");
                    copiiContainerMesaje.add(labelMesajCurent);
                } else {
                    labelMesajCurent.setMaxWidth(Double.MAX_VALUE);
                    labelMesajCurent.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 5 10; -fx-background-radius: 25; " +
                            "-fx-alignment: center-left; -fx-font-size: 16; -fx-font-style: italic");
                    copiiContainerMesaje.add(labelMesajCurent);
                }
            }
        } catch (SQLException exceptieMesaje) {
            exceptieMesaje.printStackTrace();
        }

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), containerElement);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);
        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.millis(200), containerElement);
        scaleOutTransition.setFromX(1.02);
        scaleOutTransition.setFromY(1.02);
        scaleOutTransition.setToX(1.0);
        scaleOutTransition.setToY(1.0);
        containerElement.setOnMouseEntered(mouseEvent -> scaleTransition.playFromStart());
        containerElement.setOnMouseExited(mouseEvent -> scaleOutTransition.playFromStart());

        butonTrimiteMesaj.setOnMouseClicked(mouseEvent -> {
            String fieldMesajText = fieldMesaj.getText();
            if (fieldMesajText != null && !fieldMesajText.isEmpty()) {
                PreparedStatement preparedStatementMesaje = null;
                try {
                    preparedStatementMesaje = new BazaDeDate().getConexiuneBazaDeDate().prepareStatement("insert into \"MesajeUtilizatori\" " +
                            "(\"idUtilizator\", \"mesajUtilizator\", \"idCapitol\", \"idElement\") values (?, ?, ?, ?);");
                    preparedStatementMesaje.setLong(1, utilizatorAutentificat.getIdUtilizator());
                    preparedStatementMesaje.setString(2, fieldMesajText);
                    preparedStatementMesaje.setLong(3, idCapitol);
                    preparedStatementMesaje.setLong(4, idElement);
                    preparedStatementMesaje.executeUpdate();
                    String oraMesaj = new SimpleDateFormat("HH:mm").format(Timestamp.from(new Date().toInstant()));
                    Label labelMesajIntrodus = new Label(oraMesaj + " " + utilizatorAutentificat.getNumeFamilie()
                            + " " + utilizatorAutentificat.getPrenumeUtilizator() + ": " + fieldMesajText);
                    labelMesajIntrodus.setMaxWidth(Double.MAX_VALUE);
                    labelMesajIntrodus.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 5 10; " +
                            "-fx-background-radius: 25; -fx-alignment: center-right; -fx-font-size: 16; -fx-font-style: italic");
                    containerMesaje.getChildren().add(labelMesajIntrodus);
                } catch (SQLException exceptieMesaje) {
                    exceptieMesaje.printStackTrace();
                } finally {
                    if (preparedStatementMesaje != null) {
                        try {
                            preparedStatementMesaje.close();
                        } catch (SQLException exceptieMesaje) {
                            exceptieMesaje.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
