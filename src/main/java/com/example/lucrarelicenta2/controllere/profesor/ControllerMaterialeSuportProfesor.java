package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.elemente.profesor.FisierMaterialeSuportProfesor;
import com.example.lucrarelicenta2.modele.Profesor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControllerMaterialeSuportProfesor implements Initializable {

    private final long idElement, idCapitol;
    private final String titluElement, mesajElement;
    private final Profesor profesorConectat;
    private final Stage fereastraRadacina;
    public TextField fieldMesaj;
    public Button butonTrimiteMesaj;
    @FXML
    private VBox containerMesaje;
    @FXML
    private FlowPane containerFisiere;
    @FXML
    private Button butonIncarcaFisier;
    @FXML
    private Label labelTitlu, labelMesaj;
    private List<Label> mesajeUtilizatori;

    public ControllerMaterialeSuportProfesor(long idElement, long idCapitol, String titluElement, String mesajElement,
            Profesor profesorConectat, Stage fereastraRadacina) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.mesajElement = mesajElement;
        this.profesorConectat = profesorConectat;
        this.fereastraRadacina = fereastraRadacina;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        labelTitlu.setText(labelTitlu.getText() + titluElement);
        final String[] textTitluInitial = {labelTitlu.getText()};
        labelTitlu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    labelTitlu.setText("");
                    labelTitlu.requestFocus();
                }
            }
        });
        labelTitlu.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    labelTitlu.getParent().requestFocus();
                    keyEvent.consume();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"MaterialeSuport\" " +
                                "SET \"titluElement\" = ? WHERE \"idElement\" = ? AND \"idCapitol\" = ?;");
                        preparedStatement.setString(1, labelTitlu.getText());
                        preparedStatement.setLong(2, idElement);
                        System.out.println("idElement = " + idElement);
                        System.out.println("idCapitol = " + idCapitol);
                        System.out.println("labelTitlu.getText() = " + labelTitlu.getText());
                        preparedStatement.setLong(3, idCapitol);
                        int executeUpdate = preparedStatement.executeUpdate();
                        System.out.println("executeUpdate = " + executeUpdate);
                        if (executeUpdate != 1) {
                            labelTitlu.setText(textTitluInitial[0]);
                        } else {
                            textTitluInitial[0] = labelTitlu.getText();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    String textDescriereCurs = labelTitlu.getText();
                    if (!textDescriereCurs.isEmpty()) {
                        labelTitlu.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                    }
                } else {
                    String upperCaseText = keyEvent.getText();
                    if (keyEvent.isShiftDown()) {
                        upperCaseText = upperCaseText.toUpperCase();
                    }
                    labelTitlu.setText(labelTitlu.getText() + upperCaseText);
                    keyEvent.consume();
                }
            }
        });
        labelTitlu.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelTitlu.setStyle("-fx-font-size: 36; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-text-fill: #d966ff; -fx-cursor: hand");
            }
        });
        labelTitlu.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelTitlu.setStyle("-fx-font-size: 36; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-text-fill: #cc33ff; -fx-cursor: hand");
            }
        });

        labelMesaj.setText(mesajElement);
        final String[] mesajInitial = {labelMesaj.getText()};
        labelMesaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    labelMesaj.setText("");
                    labelMesaj.requestFocus();
                }
            }
        });
        labelMesaj.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    labelMesaj.getParent().requestFocus();
                    keyEvent.consume();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"MaterialeSuport\" " +
                                "SET \"mesajElement\" = ? WHERE \"idElement\" = ? AND \"idCapitol\" = ?;");
                        preparedStatement.setString(1, labelMesaj.getText());
                        preparedStatement.setLong(2, idElement);
                        preparedStatement.setLong(3, idCapitol);
                        if (preparedStatement.executeUpdate() != 1) {
                            labelMesaj.setText(mesajInitial[0]);
                        } else {
                            mesajInitial[0] = labelMesaj.getText();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    String textDescriereCurs = labelMesaj.getText();
                    if (!textDescriereCurs.isEmpty()) {
                        labelMesaj.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                    }
                } else {
                    String upperCaseText = keyEvent.getText();
                    if (keyEvent.isShiftDown()) {
                        upperCaseText = upperCaseText.toUpperCase();
                    }
                    labelMesaj.setText(labelMesaj.getText() + upperCaseText);
                    keyEvent.consume();
                }
            }
        });
        labelMesaj.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelMesaj.setStyle("-fx-font-size: 18; -fx-font-style: italic; " +
                        "-fx-text-fill: #cccccc; -fx-cursor: hand");
            }
        });
        labelMesaj.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelMesaj.setStyle("-fx-font-size: 18; -fx-font-style: italic; " +
                        "-fx-text-fill: #b3b3b3; -fx-cursor: hand");
            }
        });



        adaugaFisiere();
        // TODO: adauga mesajele trimise de catre profesor in Baza de Date!
        adaugaMesajeUtilizatori();
        adaugaStilizareButonului(butonIncarcaFisier);
        adaugaFunctionalitateIncarcareFisiere();
        adaugaStilizareButonului(butonTrimiteMesaj);
    }

    private void adaugaFunctionalitateIncarcareFisiere() {
        butonIncarcaFisier.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fileChooser = new FileChooser();
                List<File> fisiereSelectate = fileChooser.showOpenMultipleDialog(fereastraRadacina);
//                TODO: adauga fisierul in Baza de Date
                if (fisiereSelectate != null) {
                    for (File fisierCurent : fisiereSelectate) {
                        try {
                            Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                            FileInputStream inputStream = new FileInputStream(fisierCurent);
                            FisierMaterialeSuportProfesor fisierSelectat = new FisierMaterialeSuportProfesor(0L, idElement,
                                    fisierCurent.getName(), inputStream);
                            PreparedStatement preparedStatement = conexiuneBD.prepareStatement("INSERT INTO \"FisiereMaterialeSuport\" " +
                                    "(\"idElement\", \"numeFisier\", \"continutFisier\") VALUES (?, ?, ?);");
                            preparedStatement.setLong(1, idElement);
                            preparedStatement.setString(2, fisierCurent.getName());
                            preparedStatement.setBinaryStream(3, inputStream, fisierCurent.length());
                            if (preparedStatement.executeUpdate() == 1) {
                                containerFisiere.getChildren().add(fisierSelectat);
                                fisierSelectat.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        if (mouseEvent.getClickCount() == 2) {
                                            try {
                                                PreparedStatement preparedStatementDelete = conexiuneBD.prepareStatement("DELETE FROM \"FisiereMaterialeSuport\" " +
                                                        "WHERE \"idFisier\" = ? AND \"idElement\" = ?;");
                                                preparedStatementDelete.setLong(1, fisierSelectat.getIdFisier());
                                                preparedStatementDelete.setLong(2, idElement);
                                                if (preparedStatementDelete.executeUpdate() == 1) {
                                                    containerFisiere.getChildren().remove(fisierSelectat);
                                                }
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }
                        } catch (FileNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    private void adaugaStilizareButonului(Button butonCurent) {
        butonCurent.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                butonCurent.setStyle("-fx-background-color: #f7e6ff; -fx-background-radius: 25; " +
                        "-fx-text-fill: #cc33ff; -fx-border-color: #cc33ff; -fx-border-radius: 25; " +
                        "-fx-border-width: 2; -fx-padding: 5 50; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1)");
            }
        });
        butonCurent.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                butonCurent.setStyle("-fx-background-color: #cc33ff; -fx-background-radius: 25; " +
                        "-fx-text-fill: white; -fx-border-color: #cc33ff; -fx-border-radius: 25; " +
                        "-fx-border-width: 2; -fx-padding: 5 50; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1)");
            }
        });
    }

    private void adaugaFisiere() {
        List<FisierMaterialeSuportProfesor> fisiereElement = new BazaDeDate().getFisiereMaterialeSuportProfesor(idElement);
        for (FisierMaterialeSuportProfesor elementCurent : fisiereElement) {
            containerFisiere.getChildren().add(elementCurent);
            elementCurent.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        containerFisiere.getChildren().remove(elementCurent);
                        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                        try {
                            PreparedStatement preparedStatementDelete = conexiuneBD.prepareStatement("DELETE FROM \"FisiereMaterialeSuport\" " +
                                    "WHERE \"idFisier\" = ? AND \"idElement\" = ?;");
                            preparedStatementDelete.setLong(1, elementCurent.getIdFisier());
                            preparedStatementDelete.setLong(2, idElement);
                            if (preparedStatementDelete.executeUpdate() == 1) {
                                containerFisiere.getChildren().remove(elementCurent);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void adaugaMesajeUtilizatori() {
        ObservableList<Node> copiiContainerMesaje = containerMesaje.getChildren();
        try {
            BazaDeDate obiectBD = new BazaDeDate();
            List<Label> listaMesajeElement = obiectBD.preluareMesajeElement(idCapitol, idElement);
            List<Label> mesajeProfesor = obiectBD.preluareMesajeProfesor(idCapitol, idElement, profesorConectat.getIdProfesor());
            mesajeProfesor.sort(Comparator.comparing(Labeled::getText));
            listaMesajeElement.addAll(mesajeProfesor);
            for (Label labelMesajCurent : listaMesajeElement) {
                String textMasaj = labelMesajCurent.getText();
                if (textMasaj.contains(profesorConectat.getNumeProfesor()) && textMasaj.contains(profesorConectat.getPrenumeProfesor())) {
                    labelMesajCurent.setMaxWidth(Double.MAX_VALUE);
                    labelMesajCurent.setStyle("-fx-background-color: #ffccff; -fx-padding: 5 10; -fx-background-radius: 25; " +
                            "-fx-alignment: center-right; -fx-font-size: 16; -fx-font-style: italic");
                    copiiContainerMesaje.add(labelMesajCurent);
                } else {
                    labelMesajCurent.setMaxWidth(Double.MAX_VALUE);
                    labelMesajCurent.setStyle("-fx-background-color: #ffe6ff; -fx-padding: 5 10; -fx-background-radius: 25; " +
                            "-fx-alignment: center-left; -fx-font-size: 16; -fx-font-style: italic");
                    copiiContainerMesaje.add(labelMesajCurent);
                }
            }
        } catch (SQLException exceptieMesaje) {
            exceptieMesaje.printStackTrace();
        }
        butonTrimiteMesaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String mesajProfesor = fieldMesaj.getText();
                if (!mesajProfesor.isEmpty()) {
                    Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("INSERT INTO \"MesajeProfesori\" " +
                                "(\"idProfesor\", \"timeStampMesaj\", \"idCapitol\", \"idElement\", \"mesajProfesor\") " +
                                "VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?);");
                        preparedStatement.setLong(1, profesorConectat.getIdProfesor());
                        preparedStatement.setLong(2, idCapitol);
                        preparedStatement.setLong(3, idElement);
                        preparedStatement.setString(4, mesajProfesor);
                        if (preparedStatement.executeUpdate() == 1) {
                            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
                            String timeStampMesajFormatat = DateTimeFormatter.ofPattern("HH:mm").format(localDateTime);
                            Label labelMesaj = new Label(timeStampMesajFormatat + " " + profesorConectat.getNumeProfesor() + " "
                                    + profesorConectat.getPrenumeProfesor() + ": " + mesajProfesor);
                            labelMesaj.setStyle("-fx-background-color: #ffccff; -fx-padding: 5 10; -fx-background-radius: 25; " +
                                    "-fx-alignment: center-right; -fx-font-size: 16; -fx-font-style: italic");
                            labelMesaj.setMaxWidth(Double.MAX_VALUE);
                            copiiContainerMesaje.add(labelMesaj);
                            fieldMesaj.clear();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
