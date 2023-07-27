package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementMaterialeSuportProfesor;
import com.example.lucrarelicenta2.modele.Profesor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCreareMaterialeSuport implements Initializable {
    private final long idCapitol;
    private final Profesor profesorConectat;
    private final Stage scenaRadacina;
    private final FlowPane containerElemente;
    @FXML
    private TextField fieldTitlu;
    @FXML
    private TextField fieldMesaj;
    @FXML
    private FlowPane containerFisiere;
    @FXML
    private Button butonAdaugaFisiere;
    @FXML
    private Button butonCreareElement;
    private List<File> fisiereSelectate;

    public ControllerCreareMaterialeSuport(long idCapitol, Profesor profesorConectat, Stage scenaRadacina, FlowPane containerElemente) {
        this.idCapitol = idCapitol;
        this.profesorConectat = profesorConectat;
        this.scenaRadacina = scenaRadacina;
        this.containerElemente = containerElemente;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        butonAdaugaFisiere.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fileChooser = new FileChooser();
                fisiereSelectate = fileChooser.showOpenMultipleDialog(scenaRadacina);
                if (fisiereSelectate != null) {
                    containerFisiere.getChildren().addAll(fisiereSelectate.stream()
                            .map(fisierCurent -> {
                                Label labelFisier = new Label(fisierCurent.getName());
                                labelFisier.setStyle("-fx-background-color: #f7e6ff; -fx-background-radius: 25; " +
                                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1); -fx-padding: 20");
                                return labelFisier;
                            })
                            .toList());
                }
            }
        });

        butonCreareElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String titluText = fieldTitlu.getText();
                String mesajText = fieldMesaj.getText();
                if (titluText.length() > 0 && mesajText.length() > 0) {
                    Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("insert into \"MaterialeSuport\" " +
                                "(\"idCapitol\", \"titluElement\", \"mesajElement\") values (?, ?, ?);");
                        preparedStatement.setLong(1, idCapitol);
                        preparedStatement.setString(2, titluText);
                        preparedStatement.setString(3, mesajText);
                        if (preparedStatement.executeUpdate() == 1) {
                            PreparedStatement preparedStatementSelectID = conexiuneBD.prepareStatement("select \"idElement\" " +
                                    "from \"MaterialeSuport\" where \"idCapitol\" = ? and \"titluElement\" = ? and \"mesajElement\" = ?;");
                            preparedStatementSelectID.setLong(1, idCapitol);
                            preparedStatementSelectID.setString(2, titluText);
                            preparedStatementSelectID.setString(3, mesajText);
                            ResultSet resultSet = preparedStatementSelectID.executeQuery();
                            long idElement = 0;
                            if (resultSet.next()) {
                                if (fisiereSelectate.size() > 0) {
                                    idElement = resultSet.getLong("idElement");
                                    PreparedStatement preparedStatementInsertFisiere = conexiuneBD.prepareStatement("insert into " +
                                            "\"FisiereMaterialeSuport\" (\"idElement\", \"numeFisier\", \"continutFisier\") values (?, ?, ?);");
                                    for (File fisierCurent : fisiereSelectate) {
                                        preparedStatementInsertFisiere.setLong(1, idElement);
                                        preparedStatementInsertFisiere.setString(2, fisierCurent.getName());
                                        preparedStatementInsertFisiere.setBinaryStream(3, new FileInputStream(fisierCurent), fisierCurent.length());
                                        preparedStatementInsertFisiere.executeUpdate();
                                    }
                                }
                            }
                            containerElemente.getChildren().add(new ElementMaterialeSuportProfesor(idElement, idCapitol,
                                    titluText, mesajText, profesorConectat, scenaRadacina));
                        }
                    } catch (SQLException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
