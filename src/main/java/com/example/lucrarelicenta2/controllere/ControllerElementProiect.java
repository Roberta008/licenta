package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.elemente.FisierProiect;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ControllerElementProiect implements Initializable {
    private final Stage radacinaAplicatiei;
    private final long idProiect;
    private final long idCapitol;
    private final String titluProiect;
    private final Timestamp termenPredare;
    private final List<String> listaObiectiveProiect;
    private String statusProiect;
    private final Utilizator utilizatorAutentificat;
    public Label labelStatusProiect;
    public Label labelTitluProiect;
    public Label labelTermenLimita;
    public VBox containerObiectiveProiect;
    @FXML
    private HBox containerTitluSiTermen;
    @FXML
    private FlowPane containerFisiere;
    @FXML
    private Button butonIncarcaFisier, butonPredare;
    @FXML
    private VBox containerElement;
    private FisierProiect fisierProiect;

    public ControllerElementProiect(Stage radacinaAplicatiei, long idProiect, long idCapitol, String titluProiect, Timestamp termenPredare,
            List<String> listaObiectiveProiect, String statusProiect, Utilizator utilizatorAutentificat) {
        this.radacinaAplicatiei = radacinaAplicatiei;
        this.idProiect = idProiect;
        this.idCapitol = idCapitol;
        this.titluProiect = titluProiect;
        this.termenPredare = termenPredare;
        this.listaObiectiveProiect = listaObiectiveProiect;
        this.statusProiect = statusProiect;
        this.utilizatorAutentificat = utilizatorAutentificat;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFlow titluProiect = new TextFlow();
        Text textTitluProiect = new Text(this.titluProiect);
        textTitluProiect.setStyle("-fx-font-size: 30; -fx-fill: #cc33ff; -fx-font-weight: bold; -fx-font-style: italic");
        textTitluProiect.setTextAlignment(TextAlignment.LEFT);
        titluProiect.getChildren().add(textTitluProiect);
        titluProiect.setPadding(new Insets(20, 0, 0, 20));
        HBox.setHgrow(titluProiect, Priority.ALWAYS);

        TextFlow termenPredare = new TextFlow();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Text textTermenPredare = new Text(simpleDateFormat.format(this.termenPredare));
        textTermenPredare.setStyle("-fx-font-size: 18; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #b3b3b3");
        Text textTermenLimita = new Text("Termen limita: ");
        textTermenLimita.setStyle("-fx-font-size: 18; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #b3b3b3");
        termenPredare.getChildren().addAll(textTermenLimita, new Text("\n"), textTermenPredare);
        termenPredare.setMaxWidth(Double.MAX_VALUE);
        termenPredare.setPadding(new Insets(20, 20, 0, 0));
        termenPredare.setTextAlignment(TextAlignment.RIGHT);
        HBox.setHgrow(termenPredare, Priority.ALWAYS);

        butonPredare.setDisable(true);

        ObservableList<Node> obiectiveProiect = containerObiectiveProiect.getChildren();
        if (listaObiectiveProiect.size() > 0) {
            for (String obiectivCurent : listaObiectiveProiect) {
                if (!obiectivCurent.isEmpty()) {
                    Label labelObiectiv = new Label(obiectivCurent);
                    if (!Character.isDigit(obiectivCurent.charAt(0)) || obiectivCurent.charAt(1) != '.') {
                        labelObiectiv.setStyle("-fx-font-style: italic; -fx-font-size: 18; -fx-text-fill: #4d004d");
                    } else {
                        labelObiectiv.setStyle("-fx-font-style: italic; -fx-font-size: 14; -fx-text-fill: #cc33ff");
                    }
                    labelObiectiv.setWrapText(true);
                    labelObiectiv.setTextAlignment(TextAlignment.CENTER);
                    obiectiveProiect.add(labelObiectiv);
                }
            }
        } else {
            obiectiveProiect.add(new Label("Deocamdata profesorul nu a adaugat niciun obiectiv proiectului!"));
        }

        ObservableList<Node> listaFisiere = containerFisiere.getChildren();

        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        try {
            if (!statusProiect.equals("Lipsa")) {
                PreparedStatement preparedStatementFisiereProiect = conexiuneBD.prepareStatement("select * from \"FisiereProiecte\" " +
                        "where \"idUtilizator\" = ? and \"idProiect\" = ?;");
                preparedStatementFisiereProiect.setLong(1, utilizatorAutentificat.getIdUtilizator());
                preparedStatementFisiereProiect.setLong(2, idProiect);
                ResultSet resultSetFisiereProiect = preparedStatementFisiereProiect.executeQuery();
                if (resultSetFisiereProiect.next()) {
                    String numeFisier = resultSetFisiereProiect.getString("numeFisier");
                    InputStream continutFisier = resultSetFisiereProiect.getBinaryStream("continutFisier");

                    fisierProiect = new FisierProiect(idProiect, numeFisier, continutFisier);

                    FontIcon fontIcon = new FontIcon("anto-file");
                    fontIcon.setIconColor(Color.PURPLE);
                    fontIcon.setIconSize(45);

                    Label labelFisier = new Label(numeFisier);
                    labelFisier.setContentDisplay(ContentDisplay.TOP);
                    labelFisier.setGraphic(fontIcon);
                    labelFisier.setPrefWidth(250.0);
                    labelFisier.setAlignment(Pos.CENTER);
                    labelFisier.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 10 25; -fx-background-radius: 25; -fx-border-color: #cc33ff; " +
                            "-fx-border-radius:  25; -fx-border-width: 3; -fx-text-fill: #330033; -fx-font-size: 18; -fx-font-style: italic");

                    listaFisiere.add(labelFisier);

//                    labelFisier.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                        @Override
//                        public void handle(MouseEvent mouseEvent) {
//                            System.out.println("statusProiect = " + statusProiect);
//                            if (!statusProiect.equals("Predat")) {
//                                listaFisiere.remove(labelFisier);
//                            }
//                        }
//                    });

                    butonIncarcaFisier.setDisable(true);
                    butonPredare.setDisable(false);
                    butonPredare.setText("Anulare Predare");
                } else {
                    statusProiect = "Lipsa";
                    PreparedStatement preparedStatement = conexiuneBD.prepareStatement("update \"StatusProiect\" set " +
                            "\"statusProiect\" = ? where \"idProiect\" = ? and \"idUtilizator\" = ?;");
                    preparedStatement.setString(1, "Lipsa");
                    preparedStatement.setLong(2, idProiect);
                    preparedStatement.setLong(3, utilizatorAutentificat.getIdUtilizator());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException exceptieBazaDeDate) {
            exceptieBazaDeDate.printStackTrace();
        }

        labelStatusProiect.setText("Proiect " + statusProiect);

        for (Node fisierCurent : listaFisiere) {
            fisierCurent.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("statusProiect = " + statusProiect);
                    if (!statusProiect.equals("Predat")) {
                        listaFisiere.remove(fisierCurent);
                    }
                }
            });
        }

        butonIncarcaFisier.setOnMouseClicked(mouseEvent -> {
            File fisierAles = new FileChooser().showOpenDialog(radacinaAplicatiei);
            if (fisierAles != null) {
                FontIcon fontIcon = new FontIcon("anto-file");
                fontIcon.setIconColor(Color.PURPLE);
                fontIcon.setIconSize(45);
                Label labelFisier = new Label(fisierAles.getName());
                labelFisier.setContentDisplay(ContentDisplay.TOP);
                labelFisier.setGraphic(fontIcon);
                labelFisier.setPrefWidth(250.0);
                labelFisier.setAlignment(Pos.CENTER);
                labelFisier.setStyle("-fx-background-color: #f7e6ff; -fx-padding: 10 25; -fx-background-radius: 25; -fx-border-color: #cc33ff; " +
                        "-fx-border-radius:  25; -fx-border-width: 3; -fx-text-fill: #330033; -fx-font-size: 18; -fx-font-style: italic");
                listaFisiere.add(labelFisier);


                labelFisier.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println("statusProiect = " + statusProiect);
                        if (!statusProiect.equals("Predat")) {
                            listaFisiere.remove(labelFisier);
                        }
                    }
                });


                try {
                    fisierProiect = new FisierProiect(idProiect, fisierAles.getName(), new FileInputStream(fisierAles));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        listaFisiere.addListener((ListChangeListener<Node>) sizeListener -> {
            int dimensiuneListaFisiere = listaFisiere.size();
            butonPredare.setDisable(dimensiuneListaFisiere == 0);
            if (dimensiuneListaFisiere > 0) {
                actualizeazaStatusProiect("Incarcat", "Proiect Incarcat", true);
            } else {
                actualizeazaStatusProiect("Nefinalizat", "Proiect Nefinalizat", false);
            }
        });



        butonPredare.setOnMouseClicked(mouseEvent -> {
            if (butonPredare.getText().equals("Anulare Predare")) {
                actualizeazaStatusProiect("Incarcat", "Proiect Incarcat", false);
                butonPredare.setText("Preadare Proiect");
                try {
                    PreparedStatement preparedStatementFisierProiect = conexiuneBD.prepareStatement("delete from \"FisiereProiecte\" " +
                            "where \"idProiect\" = ? and \"idUtilizator\" = ?");
                    preparedStatementFisierProiect.setLong(1, idProiect);
                    preparedStatementFisierProiect.setLong(2, utilizatorAutentificat.getIdUtilizator());
                    preparedStatementFisierProiect.executeUpdate();
                } catch (SQLException exceptieBazaDeDate) {
                    exceptieBazaDeDate.printStackTrace();
                }
                butonIncarcaFisier.setDisable(containerFisiere.getChildren().size() > 0);
            } else {
                try {
                    PreparedStatement preparedStatementFisierProiect = conexiuneBD.prepareStatement("insert into \"FisiereProiecte\" " +
                            "(\"idProiect\", \"idUtilizator\", \"numeFisier\", \"continutFisier\") values (?, ?, ?, ?);");
                    preparedStatementFisierProiect.setLong(1, idProiect);
                    preparedStatementFisierProiect.setLong(2, utilizatorAutentificat.getIdUtilizator());
                    preparedStatementFisierProiect.setString(3, fisierProiect.getNumeFisier());
                    preparedStatementFisierProiect.setBinaryStream(4, fisierProiect.getContinutFisier());
                    preparedStatementFisierProiect.executeUpdate();
                } catch (SQLException exceptieBazaDeDate) {
                    exceptieBazaDeDate.printStackTrace();
                }
                actualizeazaStatusProiect("Predat", "Proiect Predat", true);
                butonPredare.setText("Anulare Predare");
            }
        });

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
        containerElement.setOnMouseEntered(mouseEvent -> scaleTransition.playFromStart());
        containerElement.setOnMouseExited(mouseEvent -> scaleOutTransition.playFromStart());

        ZonedDateTime romaniaTime = ZonedDateTime.now(ZoneId.of("Europe/Bucharest"));
        Timestamp romaniaTimestamp = Timestamp.from(romaniaTime.toInstant());
        containerTitluSiTermen.getChildren().addAll(titluProiect, termenPredare);

        if (romaniaTimestamp.after(this.termenPredare)) {
            textTermenLimita.setStyle(textTermenLimita.getStyle() + "; -fx-fill: #ff0066");
            textTermenPredare.setStyle(textTermenLimita.getStyle() + "; -fx-fill: #ff0066");
            butonIncarcaFisier.setDisable(true);
            butonPredare.setDisable(true);
        }
    }

    private void actualizeazaStatusProiect(String statusProiect, String textLabelStatus, boolean butonIncarcaDisabled) {
        this.statusProiect = statusProiect;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = new BazaDeDate().getConexiuneBazaDeDate()
                    .prepareStatement("update \"StatusProiect\" set \"statusProiect\" = ? " +
                            "where \"idProiect\" = ? and \"idUtilizator\" = ?;");
            preparedStatement.setString(1, statusProiect);
            preparedStatement.setLong(2, idProiect);
            preparedStatement.setLong(3, utilizatorAutentificat.getIdUtilizator());
            preparedStatement.executeUpdate();
            labelStatusProiect.setText(textLabelStatus);
            butonIncarcaFisier.setDisable(butonIncarcaDisabled);
        } catch (SQLException exceptieBazaDeDate) {
            exceptieBazaDeDate.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException exceptieBazaDeDate) {
                    exceptieBazaDeDate.printStackTrace();
                }
            }
        }
    }
}
