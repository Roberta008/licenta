package com.example.lucrarelicenta2.elemente.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class TemaTrimisaProfesor extends VBox {

    private final long idTema;
    private final long idCapitol;
    private final long idUtilizator;
    private final String statusTema;
    private final int notaTema;
    private final String numeUtilizator;
    private final String numeFamilie;
    private final String prenumeUtilizator;
    private final List<FisiereTemeProfesor> fisiereTema;

    public TemaTrimisaProfesor(long idTema, long idCapitol, long idUtilizator, String statusTema, int notaTema,
            String numeUtilizator, String numeFamilie, String prenumeUtilizator, List<FisiereTemeProfesor> fisiereTema) {
        super();
        this.idTema = idTema;
        this.idCapitol = idCapitol;
        this.idUtilizator = idUtilizator;
        this.statusTema = statusTema;
        this.notaTema = notaTema;
        this.numeUtilizator = numeUtilizator;
        this.numeFamilie = numeFamilie;
        this.prenumeUtilizator = prenumeUtilizator;
        this.fisiereTema = fisiereTema;

        TextFlow textFlowPrimaParte = new TextFlow();
        textFlowPrimaParte.setTextAlignment(TextAlignment.CENTER);

        Text textPrimaParte1 = new Text(numeFamilie + " " + prenumeUtilizator);
        textPrimaParte1.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill: #b3b3b3; -fx-font-weight: bold");
        Text textPrimaParte2 = new Text(" ( ");
        textPrimaParte2.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill:  #b3b3b3");
        Text textPrimaParte3 = new Text(numeUtilizator);
        textPrimaParte3.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #cc33ff");
        Text textPrimaParte4 = new Text(" ) - Status: ");
        textPrimaParte4.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill:  #b3b3b3");
        Text textPrimaParte5 = new Text(statusTema);
        textPrimaParte5.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #cc33ff");
        Text textPrimaParte6 = new Text(" - Nota: ");
        textPrimaParte6.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill:  #b3b3b3");
        Text textPrimaParte7 = new Text(String.valueOf(notaTema));
        textPrimaParte7.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #cc33ff; " +
                "-fx-cursor: hand");
        textPrimaParte7.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    textPrimaParte7.setText("");
                    textPrimaParte7.requestFocus();
                }
            }
        });
        String[] notaInitiala = {textPrimaParte7.getText()};
        textPrimaParte7.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                    textPrimaParte7.getParent().requestFocus();
                    keyEvent.consume();
                    try {
                        int nouaNota = Integer.parseInt(textPrimaParte7.getText());
                        if (nouaNota < 0 || nouaNota > 10) {
                            textPrimaParte7.setText(notaInitiala[0]);
                        } else {
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"StatusTeme\" " +
                                        "SET \"notaTema\" = ? WHERE \"idTema\" = ? AND \"idUtilizator\" = ?;");
                                preparedStatement.setInt(1, nouaNota);
                                preparedStatement.setLong(2, idTema);
                                preparedStatement.setLong(3, idUtilizator);
                                int executeUpdate = preparedStatement.executeUpdate();
                                if (executeUpdate != 1) {
                                    textPrimaParte7.setText(notaInitiala[0]);
                                } else {
                                    notaInitiala[0] = textPrimaParte7.getText();
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (NumberFormatException e) {
                        textPrimaParte7.setText(notaInitiala[0]);
                    }
                } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    String textDescriereCurs = textPrimaParte7.getText();
                    if (!textDescriereCurs.isEmpty()) {
                        textPrimaParte7.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                    }
                } else {
                    textPrimaParte7.setText(textPrimaParte7.getText() + keyEvent.getText());
                    keyEvent.consume();
                }
            }
        });
        textPrimaParte7.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                textPrimaParte7.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-fill: #d966ff; -fx-cursor: hand");
            }
        });
        textPrimaParte7.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                textPrimaParte7.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-fill: #cc33ff; -fx-cursor: hand");
            }
        });


        textFlowPrimaParte.getChildren().addAll(textPrimaParte1, textPrimaParte2, textPrimaParte3, textPrimaParte4,
                textPrimaParte5, textPrimaParte6, textPrimaParte7);

        FlowPane flowPaneFisiere = new FlowPane();
        flowPaneFisiere.setStyle("-fx-background-color: #f7e6ff; -fx-background-radius: 25; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1); -fx-padding: 10");
        flowPaneFisiere.setAlignment(Pos.CENTER);
        VBox.setMargin(flowPaneFisiere, new Insets(10.0, 0.0, 0.0, 0.0));
        for (FisiereTemeProfesor elementCurent : fisiereTema) {
            flowPaneFisiere.getChildren().add(elementCurent);
        }

        Label labelTextInformativ = new Label("Fisier/ele trimis/e de catre Student:");
        labelTextInformativ.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-text-fill:  #b3b3b3");
        VBox.setMargin(labelTextInformativ, new Insets(10.0, 0.0, 0.0, 0.0));
        getChildren().addAll(textFlowPrimaParte, labelTextInformativ, flowPaneFisiere);


        setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-padding: 10; " +
                "-fx-effect:  dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1)");

        setAlignment(Pos.CENTER);
    }

    public long getIdTema() {
        return idTema;
    }

    public long getIdCapitol() {
        return idCapitol;
    }

    public long getIdUtilizator() {
        return idUtilizator;
    }

    public String getStatusTema() {
        return statusTema;
    }

    public int getNotaTema() {
        return notaTema;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public String getNumeFamilie() {
        return numeFamilie;
    }

    public String getPrenumeUtilizator() {
        return prenumeUtilizator;
    }

    public List<FisiereTemeProfesor> getFisiereTema() {
        return fisiereTema;
    }
}
