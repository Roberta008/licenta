package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.componente.continut.containere.ElementInformatieGenerala;
import com.example.lucrarelicenta2.componente.continut.containere.ElementMaterialeSuport;
import com.example.lucrarelicenta2.componente.continut.containere.ElementProiect;
import com.example.lucrarelicenta2.componente.continut.containere.ElementTestGrila;
import com.example.lucrarelicenta2.componente.cursuri.Curs;
import com.example.lucrarelicenta2.componente.continut.ElementTema;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerContinutCurs implements Initializable {
    private final Curs elementCurs;
    private final Stage radacinaAplicatiei;
    private final Utilizator utilizatorAutentificat;
    @FXML
    private VBox containerElemente;

    public ControllerContinutCurs(Curs elementCurs, Stage radacinaAplicatiei, Utilizator utilizatorAutentificat) {
        this.elementCurs = elementCurs;
        this.radacinaAplicatiei = radacinaAplicatiei;
        this.utilizatorAutentificat = utilizatorAutentificat;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label labelTitluCurs = new Label(this.elementCurs.getTitluCurs());
        labelTitluCurs.setStyle("-fx-font-family: Georgia; -fx-font-style: italic; -fx-font-weight: bold; -fx-font-size: 36");
        Label labelDescriereCurs = new Label(this.elementCurs.getDescriereCurs());
        labelDescriereCurs.setStyle("-fx-font-family: Georgia; -fx-font-style: italic; -fx-font-size: 18");
        containerElemente.getChildren().addAll(labelTitluCurs, labelDescriereCurs);
        BazaDeDate bazaDeDate = new BazaDeDate();
        Connection sqlConnection = bazaDeDate.getConexiuneBazaDeDate();
        containerElemente.setSpacing(25.0);
        if (sqlConnection != null) {
            try {
                PreparedStatement preparedStatementCapitole = sqlConnection.prepareStatement("select * from \"Capitole\" where \"idCurs\" = ? order by \"titluCapitol\";");
                preparedStatementCapitole.setInt(1, (int) elementCurs.getIdCurs());
                ResultSet resultSetCapitole = preparedStatementCapitole.executeQuery();
                while (resultSetCapitole.next()) {
                    int idCapitol = resultSetCapitole.getInt("idCapitol");
                    String titluCapitol = resultSetCapitole.getString("titluCapitol");
                    Label labelCapitol = new Label(titluCapitol);
                    labelCapitol.setStyle("-fx-text-alignment: center; -fx-background-color: #cc33ff;" +
                            "-fx-background-radius: 25; -fx-text-fill: white; -fx-font-family: Georgia; -fx-font-size: 24; -fx-font-weight: bold; -fx-font-style: italic;" +
                            "-fx-padding: 10 0 10 0");
                    labelCapitol.setAlignment(Pos.CENTER);
                    labelCapitol.setMinWidth(1100.0);
                    VBox.setMargin(labelCapitol, new Insets(15.0, 0.0, 15.0, 0.0));
                    containerElemente.getChildren().add(labelCapitol);

                    if (titluCapitol.equals("Teme")) {
                        List<ElementTema> listaTeme = new BazaDeDate().preluareListaTeme(idCapitol, utilizatorAutentificat, radacinaAplicatiei);
                        for (ElementTema elementTema : listaTeme) {
                            containerElemente.getChildren().add(elementTema.getContainerTema());
                        }
                    } else if (titluCapitol.equals("Proiect")) {
                        List<ElementProiect> elementeProiecte = new BazaDeDate().preluareProiecteUtilizator(idCapitol, radacinaAplicatiei, utilizatorAutentificat);
                        for (ElementProiect proiectCurent : elementeProiecte) {
                            containerElemente.getChildren().add(proiectCurent.getContainerProiect());
                        }
                    } else if (titluCapitol.equals("Informatii Generale")) {
                        List<ElementInformatieGenerala> elementeInformatiiGenerale = new BazaDeDate().preluareElementeInformatiiGenerale(idCapitol, utilizatorAutentificat);
                        for (ElementInformatieGenerala informatieGeneralaCurenta : elementeInformatiiGenerale) {
                            containerElemente.getChildren().add(informatieGeneralaCurenta.getContainerProiect());
                        }
                    } else if (titluCapitol.equals("Materiale Suport")) {
                        List<ElementMaterialeSuport> listaElementeMaterialeSuport = new BazaDeDate().preluareElementeMaterialeSuport(idCapitol, radacinaAplicatiei, utilizatorAutentificat);
                        for (ElementMaterialeSuport elementMaterialeSuport : listaElementeMaterialeSuport) {
                            containerElemente.getChildren().add(elementMaterialeSuport.getContainerProiect());
                        }
                    } else if (titluCapitol.equals("Teste")) {
                        List<ElementTestGrila> listaTesteGrila = new BazaDeDate().getTesteUtilizator(idCapitol, utilizatorAutentificat);
                        for (ElementTestGrila testCurent : listaTesteGrila) {
                            containerElemente.getChildren().add(testCurent.getContainerProiect());
                        }
                    }
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
