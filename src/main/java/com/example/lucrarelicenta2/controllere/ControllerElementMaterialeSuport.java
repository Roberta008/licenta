package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.elemente.FisierMaterialeSuport;
import com.example.lucrarelicenta2.modele.Utilizator;

import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControllerElementMaterialeSuport implements Initializable {
    private final long idElement, idCapitol;
    private final String titluElement, mesajElement;
    private final List<FisierMaterialeSuport> listaFisiereMaterialeSuport;
    private final Stage radacinaAplicatiei;
    private final Utilizator utilizatorAutentificat;
    @FXML
    private Label labelTitluElement, labelMesajElement;
    @FXML
    private FlowPane containerFisiere;
    @FXML
    private Button butonDescarcaFisier, butonDescarcaFisiere, butonTrimiteMesaj;
    @FXML
    private VBox containerElement, containerMesaje;
    @FXML
    private TextField fieldMesaj;
    private ObservableList<Integer> listaFisiereSelectate;
    private List<Label> listaMesajeUtilizatori;

    public ControllerElementMaterialeSuport(long idElement, long idCapitol, String titluElement, String mesajElement,
            List<FisierMaterialeSuport> listaFisiereMaterialeSuport, Stage radacinaAplicatiei, Utilizator utilizatorAutentificat) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.mesajElement = mesajElement;
        this.listaFisiereMaterialeSuport = listaFisiereMaterialeSuport;
        this.radacinaAplicatiei = radacinaAplicatiei;
        this.utilizatorAutentificat = utilizatorAutentificat;
        this.listaFisiereSelectate = FXCollections.observableArrayList();
        this.listaMesajeUtilizatori = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelTitluElement.setText(labelTitluElement.getText() + titluElement);
        labelMesajElement.setText(mesajElement);
        butonDescarcaFisier.setDisable(true);

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

        ObservableList<Node> listaCopiiContainerFisiere = containerFisiere.getChildren();
        for (FisierMaterialeSuport fisierCurent : listaFisiereMaterialeSuport) {
            FontIcon fontIcon = new FontIcon("anto-file");
            fontIcon.setIconColor(Color.PURPLE);
            fontIcon.setIconSize(45);
            Label labelFisier = new Label(fisierCurent.getNumeFisier());
            labelFisier.setContentDisplay(ContentDisplay.TOP);
            labelFisier.setGraphic(fontIcon);
            labelFisier.setPrefWidth(250.0);
            labelFisier.setAlignment(Pos.CENTER);
            labelFisier.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 10 25; -fx-background-radius: 25; -fx-border-color: #cc33ff; " +
                    "-fx-border-radius:  25; -fx-border-width: 3; -fx-text-fill: #330033; -fx-font-size: 18; -fx-font-style: italic");
            listaCopiiContainerFisiere.add(labelFisier);
        }

        butonDescarcaFisiere.setOnMouseClicked(mouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File directorulSelectat = directoryChooser.showDialog(radacinaAplicatiei);
            for (FisierMaterialeSuport fisierCurent : listaFisiereMaterialeSuport) {
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(directorulSelectat.getAbsolutePath() + "\\" + fisierCurent.getNumeFisier());
                    byte[] continutFisier = fisierCurent.getContinutFisier().readAllBytes();
                    fileOutputStream.write(continutFisier);
                } catch (IOException exceptieFisier) {
                    exceptieFisier.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException exceptieFisier) {
                            exceptieFisier.printStackTrace();
                        }
                    }
                }
            }
        });

        int[] i = {0};
        containerFisiere.getChildren().forEach(copilCurent -> {
            copilCurent.setOnMouseEntered(mouseEvent -> copilCurent.setCursor(Cursor.HAND));
            copilCurent.setId(String.valueOf(i[0]++));
            copilCurent.setOnMouseClicked(mouseEvent -> {
                Integer idFisierSelectat = Integer.valueOf(copilCurent.getId());
                if (!listaFisiereSelectate.contains(idFisierSelectat)) {
                    listaFisiereSelectate.add(idFisierSelectat);
                    copilCurent.setStyle("-fx-background-color: #dda6f7; -fx-padding: 10 25; -fx-background-radius: 25; -fx-border-color: #cc33ff;" +
                            "-fx-border-radius: 25; -fx-border-width: 3; -fx-text-fill: #330033; -fx-font-size: 18; -fx-font-style: italic");
                } else {
                    listaFisiereSelectate.remove(idFisierSelectat);
                    copilCurent.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 10 25; -fx-background-radius: 25; -fx-border-color: #cc33ff; " +
                            "-fx-border-radius:  25; -fx-border-width: 3; -fx-text-fill: #330033; -fx-font-size: 18; -fx-font-style: italic");
                }
            });
        });

        listaFisiereSelectate.addListener((ListChangeListener<Integer>) sizeListener ->
                butonDescarcaFisier.setDisable(listaFisiereSelectate.size() == 0));

        butonDescarcaFisier.setOnMouseClicked(mouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File directorSelectat = directoryChooser.showDialog(radacinaAplicatiei);
            for (Integer idFisierCurent : listaFisiereSelectate) {
                FisierMaterialeSuport fisierSuport = listaFisiereMaterialeSuport.get(idFisierCurent);
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(directorSelectat.getAbsolutePath() + "\\" + fisierSuport.getNumeFisier());
                    byte[] continutFisier = fisierSuport.getContinutFisier().readAllBytes();
                    fileOutputStream.write(continutFisier);
                } catch (IOException exceptieFisier) {
                    exceptieFisier.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException exceptieFisier) {
                            exceptieFisier.printStackTrace();
                        }
                    }
                }
            }

        });

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
                    copiiContainerMesaje.add(labelMesajIntrodus);
                    fieldMesaj.clear();
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

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), this.containerElement);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);
        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.millis(200), this.containerElement);
        scaleOutTransition.setFromX(1.02);
        scaleOutTransition.setFromY(1.02);
        scaleOutTransition.setToX(1.0);
        scaleOutTransition.setToY(1.0);
        this.containerElement.setOnMouseEntered(mouseEvent -> scaleTransition.playFromStart());
        this.containerElement.setOnMouseExited(mouseEvent -> scaleOutTransition.playFromStart());
    }
}
