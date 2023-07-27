package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.ferestre.FereastraAutentificare;
import com.example.lucrarelicenta2.ferestre.FereastraInregistrare;
import com.example.lucrarelicenta2.ferestre.FereastraPrincipala;
import com.example.lucrarelicenta2.ferestre.FereastraPrincipalaProfesor;
import com.example.lucrarelicenta2.modele.Profesor;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ControllerAutentificare implements Initializable {
    public CheckBox autentificareCaProfesor;
    @FXML
    private Label labelInapoi;
    @FXML
    private Label labelInregistrare;
    @FXML
    private Label labelEroare;
    @FXML
    private TextField fieldNumeUtilizator;
    @FXML
    private PasswordField fieldParola;
    @FXML
    private Button butonAutentificare;
    private Stage fereastraRadacina;

    public ControllerAutentificare() {

    }

    public ControllerAutentificare(Stage fereastraRadacina) {
        this.fereastraRadacina = fereastraRadacina;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelInregistrare.setOnMouseClicked(mouseEvent -> {
            FereastraInregistrare fereastraInregistrare = new FereastraInregistrare(fereastraRadacina);
            fereastraRadacina.setTitle("Inregistrare");
            fereastraRadacina.setScene(fereastraInregistrare.getScenaInregistrare());
        });
        butonAutentificare.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String numeUtilizator = fieldNumeUtilizator.getText();
                String parolaUtilizator = fieldParola.getText();
                if (numeUtilizator.length() > 0 && parolaUtilizator.length() > 0) {
                    if (autentificareCaProfesor.isSelected()) {
                        Connection conexiuneBazaDeDate = new BazaDeDate().getConexiuneBazaDeDate();
                        try {
                            PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select * from \"Profesori\" where \"usernameProfesor\" = ? and \"parolaProfesor\" = ?;");
                            preparedStatement.setString(1, numeUtilizator);
                            preparedStatement.setString(2, parolaUtilizator);
                            ResultSet resultSet = preparedStatement.executeQuery();
                            if (resultSet.next()) {
                                fereastraRadacina.setScene(new FereastraPrincipalaProfesor(new Profesor(resultSet.getLong("idProfesor"),
                                        resultSet.getString("numeProfesor"), resultSet.getString("prenumeProfesor"), resultSet.getString("titluProfesor"),
                                        resultSet.getString("usernameProfesor"), resultSet.getString("parolaProfesor")), fereastraRadacina).getScenaFereastraProfesor());
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else {
                        BazaDeDate bazaDeDate = new BazaDeDate();
                        Utilizator utilizatorGasit = bazaDeDate.autenficareUtilizator(numeUtilizator, parolaUtilizator);
                        if (utilizatorGasit != null) {
                            fereastraRadacina.setScene(new FereastraPrincipala(fereastraRadacina, utilizatorGasit).getMainScene());
                        } else {
                            labelEroare.setVisible(true);
                            labelEroare.setText("Utilizatorul nu a fost gasit!");
                        }
                    }
                } else {
                    labelEroare.setVisible(true);
                    labelEroare.setText("Completeaza ambele fielduri!");
                }


            }
        });
        labelInapoi.setOnMouseClicked(mouseEvent -> fereastraRadacina.setScene(new FereastraPrincipala(fereastraRadacina, null).getMainScene()));
    }
}
