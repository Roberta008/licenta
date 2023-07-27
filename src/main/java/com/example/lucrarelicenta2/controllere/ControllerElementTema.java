package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class ControllerElementTema implements Initializable {
    private final Stage radacinaAplicatiei;
    private final Timestamp termenLimita;
    public HBox containerTermenLimita;
    @FXML
    private Button butonIncarcaFisier, butonTrimiteTema;
    @FXML
    private TextField fieldMesaj;
    @FXML
    private FlowPane containerFisiere;
    @FXML
    private VBox containerElement, containerMesaje;
    @FXML
    private HBox alTreileaHBox;
    @FXML
    private Label labelTitlu, labelTermenPredare, labelDataPostarii, labelTask, labelStatus;
    private final String titluElement;
    private final long idElement, idCapitol;
    private Utilizator utilizatorAutentificat;
    private final Timestamp dataPostarii;
    private final String taskElement;
    private List<Label> listaMesajeUtilizatori;


    public ControllerElementTema(long idElement, String titluElement, long idCapitol,
                                 Timestamp dataPostarii, Timestamp termenLimita, String taskElement,
                                 Utilizator utilizatorAutentificat, Stage radacinaAplicatiei) {
        this.idElement = idElement;
        this.titluElement = titluElement;
        this.idCapitol = idCapitol;
        this.dataPostarii = dataPostarii;
        this.taskElement = taskElement;
        this.termenLimita = termenLimita;
        this.utilizatorAutentificat = utilizatorAutentificat;
        this.radacinaAplicatiei = radacinaAplicatiei;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelTitlu.setText(titluElement);

        butonTrimiteTema.setDisable(true);

        Connection conexiuneBazaDeDate = new BazaDeDate().getConexiuneBazaDeDate();
        containerFisiere.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                butonTrimiteTema.setDisable(containerFisiere.getChildren().size() == 0);
                containerFisiere.getChildren().forEach(copilCurent -> copilCurent.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
//                        TODO: sa nu poata sa stearga fisiere daca tema este trimisa!
                        try {
                            PreparedStatement preparedStatementFisiereTeme = conexiuneBazaDeDate
                                    .prepareStatement("delete from \"FisiereTeme\" where \"idUtilizator\" = ? " +
                                            "and \"idTema\" = ? and \"numeFisier\" = ?;");
                            preparedStatementFisiereTeme.setLong(1, utilizatorAutentificat.getIdUtilizator());
                            preparedStatementFisiereTeme.setLong(2, idElement);
                            preparedStatementFisiereTeme.setString(3, ((Label) copilCurent).getText());
                            if (preparedStatementFisiereTeme.executeUpdate() == 1) {
                                containerFisiere.getChildren().remove(copilCurent);
                            }
                        } catch (SQLException exceptieBazaDeDate) {
                            throw new RuntimeException(exceptieBazaDeDate);
                        }
                    }
                }));
            }
        });

//        Selectam Status cand intram pe curs
        try {
            PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select * from \"StatusTeme\" " +
                    "where \"idTema\" = ? and \"idUtilizator\" = ?;");
            preparedStatement.setLong(1, idElement);
            preparedStatement.setLong(2, utilizatorAutentificat.getIdUtilizator());
            ResultSet resultSetStatus = preparedStatement.executeQuery();
            if (resultSetStatus.next()) {
                String statusTema = resultSetStatus.getString("statusTema");
                if (statusTema.equals("Trimisa")) {
                    butonIncarcaFisier.setDisable(true);
                    butonTrimiteTema.setText("Anuleaza trimiterea");
                } else {
                    butonTrimiteTema.setDisable(true);
                    butonTrimiteTema.setText("Trimite Tema");
                }
                labelStatus.setText(statusTema);
            }
        } catch (SQLException exceptieBazaDeDate) {
            exceptieBazaDeDate.printStackTrace();
        }

        try {
            listaMesajeUtilizatori = new BazaDeDate().preluareMesajeElement(idCapitol, idElement);
            ObservableList<Node> copiiContainerMesaje = containerMesaje.getChildren();
            for (Label labelMesajCurent : listaMesajeUtilizatori) {
                String textMasaj = labelMesajCurent.getText();
                if (textMasaj.contains(utilizatorAutentificat.getNumeFamilie()) && textMasaj.contains(utilizatorAutentificat.getPrenumeUtilizator())) {
                    labelMesajCurent.setMaxWidth(Double.MAX_VALUE);
                    labelMesajCurent.setStyle("-fx-background-color:  #cc33ff; -fx-padding: 5 10; -fx-background-radius: 25; -fx-alignment: center-right;" +
                            " -fx-font-size: 16; -fx-font-style: italic");
                    copiiContainerMesaje.add(labelMesajCurent);
                } else {
                    labelMesajCurent.setMaxWidth(Double.MAX_VALUE);
                    labelMesajCurent.setStyle("-fx-background-color:  #cc33ff; -fx-padding: 5 10; -fx-background-radius: 25; -fx-alignment: center-left;" +
                            " -fx-font-size: 16; -fx-font-style: italic");
                    copiiContainerMesaje.add(labelMesajCurent);
                }
            }
        } catch (SQLException exceptieMesaje) {
            exceptieMesaje.printStackTrace();
        }

//        Luam fisierele cand intram prima data intr-un Curs
        try {
            PreparedStatement preparedStatementFisiereTeme = conexiuneBazaDeDate.prepareStatement("select * from \"FisiereTeme\" where \"idTema\" = ? " +
                    "and \"idUtilizator\" = ?;");
            preparedStatementFisiereTeme.setLong(1, idElement);
            preparedStatementFisiereTeme.setLong(2, utilizatorAutentificat.getIdUtilizator());
            ResultSet resultSetFisiereTeme = preparedStatementFisiereTeme.executeQuery();
            ObservableList<Node> listaCopiiContainerFisiere = containerFisiere.getChildren();
            while (resultSetFisiereTeme.next()) {
                String numeFisier = resultSetFisiereTeme.getString("numeFisier");
                FontIcon fontIcon = new FontIcon("anto-file");
                fontIcon.setIconColor(Color.PURPLE);
                fontIcon.setIconSize(45);
                Label labelTema = new Label(numeFisier);
                labelTema.setGraphic(fontIcon);
                labelTema.setContentDisplay(ContentDisplay.TOP);
                labelTema.setStyle("-fx-background-color: #f7e6ff; -fx-border-color: #cc33ff; -fx-padding: 25; " +
                        "-fx-background-radius: 25; -fx-border-radius: 25; -fx-text-fill: #330033; -fx-border-width: 3;" +
                        " -fx-font-size: 18; -fx-font-style: italic");
                listaCopiiContainerFisiere.add(labelTema);
            }
        } catch (SQLException exceptieBazaDeDate) {
            exceptieBazaDeDate.printStackTrace();
        }

        FontIcon fontIcon = new FontIcon("bi-bookmarks");
        fontIcon.setIconColor(Color.valueOf("#cc33ff"));
        fontIcon.setIconSize(25);
        labelTitlu.setGraphic(fontIcon);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy - HH:mm");

        labelDataPostarii.setText(dateFormat.format(dataPostarii));
        TextFlow textFlow = new TextFlow();
        Text textTermenLimita = new Text(dateFormat.format(termenLimita));
        textTermenLimita.setStyle("-fx-font-size: 16; -fx-fill: #bfbfbf; -fx-font-weight: bold; -fx-font-style: italic");
        textFlow.getChildren().add(textTermenLimita);
        textFlow.setPadding(new Insets(20, 20, 0, 0));
        containerTermenLimita.getChildren().add(textFlow);
        labelTask.setText(taskElement);

        FontIcon fontIconMesaj = new FontIcon("bi-chat-left-dots");
        fontIconMesaj.setIconColor(Color.valueOf("#cc33ff"));
        fontIconMesaj.setIconSize(25);
        alTreileaHBox.getChildren().add(0, fontIconMesaj);

// animatie pentru a glisa containerul
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), containerElement);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);
        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.millis(200), containerElement);
        scaleOutTransition.setFromX(1.02);
        scaleOutTransition.setFromY(1.02);
        scaleOutTransition.setToX(1.0);
        scaleOutTransition.setToY(1.0);
        containerElement.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scaleTransition.playFromStart();
            }
        });
        containerElement.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scaleOutTransition.playFromStart();
            }
        });

        butonIncarcaFisier.setOnMouseClicked(mouseEvent -> {
            FileChooser fileChooser = new FileChooser();
            File fisierSelectat = fileChooser.showOpenDialog(radacinaAplicatiei);
            FileInputStream fileInputStream = null;
            PreparedStatement preparedStatementFisierTeme = null;
            try {
                preparedStatementFisierTeme = conexiuneBazaDeDate.prepareStatement("insert into \"FisiereTeme\" (\"idTema\", \"idUtilizator\", " +
                        "\"numeFisier\", \"continutFisier\") values (?, ?, ?, ?);");
                preparedStatementFisierTeme.setLong(1, idElement);
                preparedStatementFisierTeme.setLong(2, utilizatorAutentificat.getIdUtilizator());
                preparedStatementFisierTeme.setString(3, fisierSelectat.getName());
                fileInputStream = new FileInputStream(fisierSelectat);
                preparedStatementFisierTeme.setBinaryStream(4, fileInputStream, fisierSelectat.length());
                preparedStatementFisierTeme.executeUpdate();
                FontIcon fontIconFisier = new FontIcon("anto-file");
                fontIconFisier.setIconColor(Color.PURPLE);
                fontIconFisier.setIconSize(45);
                Label labelTema = new Label(fisierSelectat.getName());
                labelTema.setGraphic(fontIconFisier);
                labelTema.setContentDisplay(ContentDisplay.TOP);
                labelTema.setStyle("-fx-background-color: #f7e6ff; -fx-border-color: #cc33ff; -fx-padding: 25; " +
                        "-fx-background-radius: 25; -fx-border-radius: 25; -fx-text-fill: #330033; -fx-border-width: 3;" +
                        " -fx-font-size: 18; -fx-font-style: italic");
                containerFisiere.getChildren().add(labelTema);
            } catch (SQLException exceptieBazaDeDate) {
                exceptieBazaDeDate.printStackTrace();
            } catch (FileNotFoundException exceptieFisier) {
                exceptieFisier.printStackTrace();
            } finally {
                if (preparedStatementFisierTeme != null) {
                    try {
                        preparedStatementFisierTeme.close();
                    } catch (SQLException exceptieBazaDeDate) {
                        exceptieBazaDeDate.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException exceptieFisier) {
                        exceptieFisier.printStackTrace();
                    }
                }
            }
        });

        butonTrimiteTema.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String noulStatus = labelStatus.getText().equals("Trimisa") ? "Lipseste" : "Trimisa";
                    PreparedStatement preparedStatementFisiereTema = conexiuneBazaDeDate
                            .prepareStatement("update \"StatusTeme\" set \"statusTema\" = " + "'" + noulStatus + "' " +
                                    "where \"idTema\" = ? and \"idUtilizator\" = ?;");
                    preparedStatementFisiereTema.setLong(1, idElement);
                    preparedStatementFisiereTema.setLong(2, utilizatorAutentificat.getIdUtilizator());
                    if (preparedStatementFisiereTema.executeUpdate() == 1) {
                        if (noulStatus.equals("Lipseste")) {
                            labelStatus.setText("Lipseste");
                            butonTrimiteTema.setText("Trimite Tema");
                            butonIncarcaFisier.setDisable(false);
                            butonTrimiteTema.setDisable(false);
                        } else {
                            labelStatus.setText("Trimisa");
                            butonTrimiteTema.setText("Anuleaza trimiterea");
                            butonIncarcaFisier.setDisable(true);
                            butonTrimiteTema.setDisable(false);
                        }
                    }
                } catch (SQLException exceptieBazaDeDate) {
                    exceptieBazaDeDate.printStackTrace();
                }
            }
        });

        fieldMesaj.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                String mesajUtilizator = fieldMesaj.getText();
                PreparedStatement preparedStatementMesaje = null;
                try {
                    preparedStatementMesaje = conexiuneBazaDeDate.prepareStatement("insert into \"MesajeUtilizatori\" " +
                            "(\"idUtilizator\", \"mesajUtilizator\", \"idCapitol\", \"idElement\") values (?, ?, ?, ?);");
                    preparedStatementMesaje.setLong(1, utilizatorAutentificat.getIdUtilizator());
                    preparedStatementMesaje.setString(2, mesajUtilizator);
                    preparedStatementMesaje.setLong(3, idCapitol);
                    preparedStatementMesaje.setLong(4, idElement);
                    preparedStatementMesaje.executeUpdate();
                    String oraMesaj = new SimpleDateFormat("HH:mm").format(Timestamp.from(new Date().toInstant()));
                    Label labelMesaj = new Label(oraMesaj + " " + utilizatorAutentificat.getNumeFamilie()
                            + " " + utilizatorAutentificat.getPrenumeUtilizator() + ": " + mesajUtilizator);
                    labelMesaj.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 5 10; -fx-background-radius: 25; -fx-alignment: center-right;" +
                            " -fx-font-size: 16; -fx-font-style: italic");
                    labelMesaj.setPrefWidth(Double.MAX_VALUE);
                    containerMesaje.getChildren().add(labelMesaj);
                    fieldMesaj.clear();
                } catch (SQLException exceptieBazaDeDate) {
                    exceptieBazaDeDate.printStackTrace();
                } finally {
                    if (preparedStatementMesaje != null) {
                        try {
                            preparedStatementMesaje.close();
                        } catch (SQLException exceptieBazaDeDate) {
                            exceptieBazaDeDate.printStackTrace();
                        }
                    }
                }
            }
        });

        ZonedDateTime romaniaTime = ZonedDateTime.now(ZoneId.of("Europe/Bucharest"));
        Timestamp romaniaTimestamp = Timestamp.from(romaniaTime.toInstant());

        if (romaniaTimestamp.after(termenLimita)) {
            textTermenLimita.setStyle(textTermenLimita.getStyle() + "; -fx-fill: #ff0066");
            butonIncarcaFisier.setDisable(true);
            butonTrimiteTema.setDisable(true);
        }
    }
}
