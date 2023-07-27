package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.componente.cursuri.CursProfesor;
import com.example.lucrarelicenta2.ferestre.FereastraContinutCursProfesor;
import com.example.lucrarelicenta2.ferestre.FereastraPrincipala;
import com.example.lucrarelicenta2.ferestre.FereastraPrincipalaProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerFereastraPrincipalaProfesor implements Initializable {

    private final Profesor profesorConectat;
    private final Stage fereastraRadacina;
    public FlowPane containerCursuri;
    public Label labelBunVenit, labelPanouAdministrativ, butonDeconectare, fieldDescriereCurs;
    public VBox containerContinutDinamic;
    public TextField fieldTitluCurs;
    public Button butonAdaugaCurs;

    public ControllerFereastraPrincipalaProfesor(Profesor profesorConectat, Stage fereastraRadacina) {
        this.profesorConectat = profesorConectat;
        this.fereastraRadacina = fereastraRadacina;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelBunVenit.setText("Buna " + profesorConectat.getPrenumeProfesor() + ", ne bucuram sa te revedem!");

        // Adaugam in containerCursuri cursurile create de Profesor
        List<CursProfesor> cursuriProfesor = new BazaDeDate().getCursuriProfesor(profesorConectat.getIdProfesor());
        containerCursuri.getChildren().addAll(cursuriProfesor);

        // Salvam lista cu copii aici pentru a nu o tot reapela getChildren()
        ObservableList<Node> copiiContainerContinutDinamic = containerContinutDinamic.getChildren();

        // Adaugam functionalitatea de a intra in fereastra continutului cursului atunci cand dam click pe un curs!
        for (CursProfesor cursCurent : cursuriProfesor) {
            cursCurent.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    copiiContainerContinutDinamic.clear();
                    ScrollPane containerContinutCurs = new FereastraContinutCursProfesor(profesorConectat, cursCurent,
                            fereastraRadacina).getContainerContinutCurs();
                    VBox.setVgrow(containerContinutCurs, Priority.ALWAYS);
                    copiiContainerContinutDinamic.add(containerContinutCurs);
                }
            });
        }

        labelPanouAdministrativ.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fereastraRadacina.setScene(new FereastraPrincipalaProfesor(profesorConectat, fereastraRadacina).getScenaFereastraProfesor());
            }
        });

        butonDeconectare.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fereastraRadacina.setScene(new FereastraPrincipala(fereastraRadacina, null).getMainScene());
            }
        });

        butonAdaugaCurs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String titluCurs = fieldTitluCurs.getText();
                String descriereCurs = fieldDescriereCurs.getText();
                if (titluCurs.length() > 0 && descriereCurs.length() > 0) {
                    Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("insert into \"Cursuri\" " +
                                "(\"idProfesor\", \"titluCurs\", \"descriereaCursului\") values (?, ?, ?);");
                        preparedStatement.setLong(1, profesorConectat.getIdProfesor());
                        preparedStatement.setString(2, titluCurs);
                        preparedStatement.setString(3, descriereCurs);
                        preparedStatement.executeUpdate();
                            PreparedStatement preparedStatementID = conexiuneBD.prepareStatement("select \"idCurs\" from \"Cursuri\" " +
                                    "where \"idProfesor\" = ? and \"titluCurs\" = ? and \"descriereaCursului\" = ?;");
                            preparedStatementID.setLong(1, profesorConectat.getIdProfesor());
                            preparedStatementID.setString(2, titluCurs);
                            preparedStatementID.setString(3, descriereCurs);
                            ResultSet resultSet = preparedStatementID.executeQuery();
                            if (resultSet.next()) {
                                long idCurs = resultSet.getLong("idCurs");
                                PreparedStatement preparedStatementInsert = conexiuneBD.prepareStatement("insert into \"Capitole\" (\"idCurs\", \"titluCapitol\") values " +
                                        "(?, 'Informatii Generale'), (?, 'Materiale Suport'), (?, 'Teme'), (?, 'Proiect'), (?, 'Teste');");
                                preparedStatementInsert.setLong(1, idCurs);
                                preparedStatementInsert.setLong(2, idCurs);
                                preparedStatementInsert.setLong(3, idCurs);
                                preparedStatementInsert.setLong(4, idCurs);
                                preparedStatementInsert.setLong(5, idCurs);
                                preparedStatementInsert.executeUpdate();
                                CursProfesor cursCurent = new CursProfesor(idCurs, titluCurs, descriereCurs);
                                containerCursuri.getChildren().add(cursCurent);
                                cursCurent.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        copiiContainerContinutDinamic.clear();
                                        ScrollPane containerContinutCurs = new FereastraContinutCursProfesor(profesorConectat, cursCurent,
                                                fereastraRadacina).getContainerContinutCurs();
                                        VBox.setVgrow(containerContinutCurs, Priority.ALWAYS);
                                        copiiContainerContinutDinamic.add(containerContinutCurs);
                                    }
                                });
                                }


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
