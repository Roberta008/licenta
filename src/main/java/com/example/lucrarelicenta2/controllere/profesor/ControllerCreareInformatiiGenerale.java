package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementInformatiiGeneraleProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

public class ControllerCreareInformatiiGenerale implements Initializable {
    private final long idCapitol;
    private final Profesor profesorConectat;
    private final FlowPane containerContinutCurs;
    @FXML
    private TextField fieldTitlu, fieldMesaj;
    @FXML
    private Button butonCreareElement;

    public ControllerCreareInformatiiGenerale(long idCapitol, Profesor profesorConectat, FlowPane containerContinutCurs) {
        this.idCapitol = idCapitol;
        this.profesorConectat = profesorConectat;
        this.containerContinutCurs = containerContinutCurs;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        butonCreareElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String textTitlu = fieldTitlu.getText();
                String textMesaj = fieldMesaj.getText();
                if (!textTitlu.isEmpty() && !textMesaj.isEmpty()) {
                    Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("INSERT INTO \"InformatiiGenerale\" " +
                                "(\"idCapitol\", \"titluElement\", \"mesajElement\") VALUES (?, ?, ?);");
                        preparedStatement.setLong(1, idCapitol);
                        preparedStatement.setString(2, textTitlu);
                        preparedStatement.setString(3, textMesaj);
                        if (preparedStatement.executeUpdate() == 1) {
                            PreparedStatement preparedStatementSelect = conexiuneBD.prepareStatement("SELECT \"idElement\" " +
                                    "FROM \"InformatiiGenerale\" WHERE \"titluElement\" = ? AND \"mesajElement\" = ?;");
                            preparedStatementSelect.setString(1, textTitlu);
                            preparedStatementSelect.setString(2, textMesaj);
                            ResultSet resultSet = preparedStatementSelect.executeQuery();
                            if (resultSet.next()) {
                                long idElement = resultSet.getLong("idElement");
                                containerContinutCurs.getChildren().add(new ElementInformatiiGeneraleProfesor(idElement,
                                        idCapitol, textTitlu, textMesaj, profesorConectat));
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
