package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementTemeProfesor;
import com.example.lucrarelicenta2.modele.Profesor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ControllerCreareTemeProfesor extends VBox implements Initializable {

    private final long idCapitol;
    private final Profesor profesorConectat;
    private final Stage fereastraRadacina;
    private final FlowPane containerElemente;
    @FXML
    private TextField fieldTitlu;
    @FXML
    private TextField fieldCerinta;
    @FXML
    private DatePicker selectorTermenLimita;
    @FXML
    private Button butonCreareElement;
    private LocalDate termenLimitaSelectat;

    public ControllerCreareTemeProfesor(long idCapitol, Profesor profesorConectat, Stage fereastraRadacina, FlowPane containerElemente) {
        this.idCapitol = idCapitol;
        this.profesorConectat = profesorConectat;
        this.fereastraRadacina = fereastraRadacina;
        this.containerElemente = containerElemente;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectorTermenLimita.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue,
                    LocalDate vechiulTermenLimita, LocalDate noulTermenLimita) {
                termenLimitaSelectat = noulTermenLimita;
            }
        });

        butonCreareElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String titluText = fieldTitlu.getText();
                String cerintaText = fieldCerinta.getText();
                if (termenLimitaSelectat != null && titluText.length() > 0 && cerintaText.length() > 0) {
                    Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("insert into \"Teme\" " +
                                "(\"idCapitol\", \"titluElement\", \"taskElement\", \"termenPostare\", \"termenLimita\") " +
                                "values (?, ?, ?, ?, ?);");
                        preparedStatement.setLong(1, idCapitol);
                        preparedStatement.setString(2, titluText);
                        preparedStatement.setString(3, cerintaText);
                        Date termenCreare = Date.valueOf(LocalDate.now());
                        preparedStatement.setDate(4, termenCreare);
                        Date termenLimita = Date.valueOf(termenLimitaSelectat);
                        preparedStatement.setDate(5, termenLimita);
                        if (preparedStatement.executeUpdate() == 1) {
                            PreparedStatement preparedStatementID = conexiuneBD.prepareStatement("select \"idElement\" " +
                                    "from \"Teme\" where \"titluElement\" = ? and \"taskElement\" = ?;");
                            preparedStatementID.setString(1, titluText);
                            preparedStatementID.setString(2, cerintaText);
                            ResultSet resultSet = preparedStatementID.executeQuery();
                            if (resultSet.next()) {
                                long idElement = resultSet.getLong("idElement");
                                containerElemente.getChildren().add(new ElementTemeProfesor(idElement, idCapitol, titluText, cerintaText, termenCreare,
                                        termenLimita, profesorConectat, fereastraRadacina));
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
