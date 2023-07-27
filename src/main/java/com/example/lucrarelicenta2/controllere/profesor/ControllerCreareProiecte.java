package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementProiectProfesor;
import com.example.lucrarelicenta2.modele.Utilizator;
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
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCreareProiecte implements Initializable {
    private final long idCurs;
    private final long idCapitol;
    private final Stage fereastraRadacina;
    private final FlowPane containerElemente;
    private final List<Utilizator> listaUtilizatori;
    @FXML
    private TextField fieldTitlu, fieldObiective;
    @FXML
    private DatePicker selectorTermenLimita;
    @FXML
    private Button butonCreareElement;
    private LocalDate termenulLimitaSelectat;

    public ControllerCreareProiecte(long idCurs, long idCapitol, Stage fereastraRadacina,
            FlowPane containerElemente, List<Utilizator> listaUtilizatori) {
        this.idCurs = idCurs;
        this.idCapitol = idCapitol;
        this.fereastraRadacina = fereastraRadacina;
        this.containerElemente = containerElemente;
        this.listaUtilizatori = listaUtilizatori;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectorTermenLimita.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate vechiulTermenLimita,
                    LocalDate noulTermenLimita) {
                termenulLimitaSelectat = noulTermenLimita;
            }
        });

        butonCreareElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String titluText = fieldTitlu.getText();
                String obiectiveText = fieldObiective.getText();

                if (termenulLimitaSelectat != null && titluText.length() > 0 &&
                         obiectiveText.length() > 0){
                    Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("insert into \"Proiecte\" " +
                                "(\"idCapitol\", \"titluProiect\", \"termenPredare\", \"idCurs\") values (?, ?, ?, ?);");
                        preparedStatement.setLong(1, idCapitol);
                        preparedStatement.setString(2, titluText);
                        preparedStatement.setDate(3, Date.valueOf(termenulLimitaSelectat));
                        preparedStatement.setLong(4, idCurs);
                        if (preparedStatement.executeUpdate() == 1) {
                            PreparedStatement preparedStatementSelectID = conexiuneBD.prepareStatement("select \"idProiect\" from \"Proiecte\" " +
                                    "where \"idCapitol\" = ? and \"idCurs\" = ? and \"titluProiect\" = ?;");
                            preparedStatementSelectID.setLong(1, idCapitol);
                            preparedStatementSelectID.setLong(2, idCurs);
                            preparedStatementSelectID.setString(3, titluText);
                            ResultSet resultSet = preparedStatementSelectID.executeQuery();
                            if (resultSet.next()) {
                                long idProiect = resultSet.getLong("idProiect");
                                PreparedStatement preparedStatementObiective = conexiuneBD.prepareStatement("insert into \"ObiectiveProiect\" " +
                                        "(\"idProiect\", \"obiectivProiect\") values (?, ?);");
                                preparedStatementObiective.setLong(1, idProiect);
                                preparedStatementObiective.setString(2, obiectiveText);
                                if (preparedStatementObiective.executeUpdate() == 1) {
                                    PreparedStatement preparedStatementStatusProiecte = conexiuneBD.prepareStatement("insert into \"StatusProiect\" " +
                                            "(\"idProiect\", \"idUtilizator\", \"statusProiect\") values (?, ?, ?);");
                                    for (Utilizator utilizatorCurent : listaUtilizatori) {
                                        preparedStatementStatusProiecte.setLong(1, idProiect);
                                        preparedStatementStatusProiecte.setLong(2, utilizatorCurent.getIdUtilizator());
                                        preparedStatementStatusProiecte.setString(3, "Lipsa");
                                        preparedStatementStatusProiecte.executeUpdate();
                                    }
                                    containerElemente.getChildren().add(new ElementProiectProfesor(idProiect, idCurs,
                                            idCapitol, titluText, Date.valueOf(termenulLimitaSelectat), fereastraRadacina));
                                }
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
