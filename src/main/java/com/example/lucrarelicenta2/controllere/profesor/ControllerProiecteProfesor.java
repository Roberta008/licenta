package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.elemente.profesor.ProiectTrimisProfesor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ControllerProiecteProfesor implements Initializable {
    private final long idProiect;
    private final long idCurs;
    private final long idCapitol;
    private final String titluProiect;
    private final Date termenPredare;
    private final Stage fereastraRadacina;
    public VBox containerElement;
    public Label labelTitluProiect;
    public VBox containerObiectiveProiect;
    public FlowPane containerFisiereProiecte;
    public DatePicker selectorNoulTermen;

    public ControllerProiecteProfesor(long idProiect, long idCurs, long idCapitol, String titluProiect,
            Date termenPredare, Stage fereastraRadacina) {
        this.idProiect = idProiect;
        this.idCurs = idCurs;
        this.idCapitol = idCapitol;
        this.titluProiect = titluProiect;
        this.termenPredare = termenPredare;
        this.fereastraRadacina = fereastraRadacina;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        labelTitluProiect.setText(labelTitluProiect.getText() + " " + titluProiect);
        final String[] titluProiectInitial = {labelTitluProiect.getText()};
        labelTitluProiect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    labelTitluProiect.setText("");
                    labelTitluProiect.requestFocus();
                }
            }
        });
        labelTitluProiect.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    labelTitluProiect.getParent().requestFocus();
                    keyEvent.consume();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"Proiecte\" SET \"titluProiect\" = ? " +
                                "WHERE \"idProiect\" = ? AND \"idCurs\" = ? AND \"idCapitol\" = ?;");
                        preparedStatement.setString(1, labelTitluProiect.getText());
                        preparedStatement.setLong(2, idProiect);
                        preparedStatement.setLong(3, idCurs);
                        preparedStatement.setLong(4, idCapitol);
                        int executeUpdate = preparedStatement.executeUpdate();
                        if (executeUpdate != 1) {
                            labelTitluProiect.setText(titluProiectInitial[0]);
                        } else {
                            titluProiectInitial[0] = labelTitluProiect.getText();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    String textDescriereCurs = labelTitluProiect.getText();
                    if (!textDescriereCurs.isEmpty()) {
                        labelTitluProiect.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                    }
                } else {
                    String upperCaseText = keyEvent.getText();
                    if (keyEvent.isShiftDown()) {
                        upperCaseText = upperCaseText.toUpperCase();
                    }
                    labelTitluProiect.setText(labelTitluProiect.getText() + upperCaseText);
                    keyEvent.consume();
                }
            }
        });
        labelTitluProiect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelTitluProiect.setStyle("-fx-font-size: 36; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-text-fill: #d966ff; -fx-cursor: hand");
            }
        });
        labelTitluProiect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelTitluProiect.setStyle("-fx-font-size: 36; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-text-fill: #cc33ff; -fx-cursor: hand");
            }
        });


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("RO"));
        TextFlow textFlowTermenLimita = new TextFlow();
        Text textTermenLimita1 = new Text("Termenul limita este: ");
        textTermenLimita1.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill: #b3b3b3");
        Text textTermenLimita2 = new Text(simpleDateFormat.format(termenPredare));
        textTermenLimita2.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #cc33ff");
        textFlowTermenLimita.getChildren().addAll(textTermenLimita1, textTermenLimita2);
        containerElement.getChildren().add(1, textFlowTermenLimita);
        textFlowTermenLimita.setTextAlignment(TextAlignment.CENTER);

        if (conexiuneBD != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("SELECT * FROM \"ObiectiveProiect\" WHERE \"idProiect\" = ?;");
                preparedStatement.setLong(1, idProiect);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String[] obiectiveProiect = resultSet.getString("obiectivProiect").split("\\\\n");
                    for (String obiectivCurent : obiectiveProiect) {
                        Label labelCerintaCurenta = new Label(obiectivCurent);
                        labelCerintaCurenta.setStyle("-fx-font-size: 16;");
                        labelCerintaCurenta.setWrapText(true);
                        labelCerintaCurenta.setTextAlignment(TextAlignment.CENTER);
                        containerObiectiveProiect.getChildren().add(labelCerintaCurenta);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conexiuneBD != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("SELECT \"idFisier\", \"idProiect\", " +
                        "T1.\"idUtilizator\", \"numeFisier\", \"continutFisier\", \"numeUtilizator\", nume, prenume " +
                        "FROM \"FisiereProiecte\" T1 JOIN \"Utilizatori\" T2 ON T2.\"idUtilizator\" = T1.\"idUtilizator\" " +
                        "WHERE \"idProiect\" = ?;");
                preparedStatement.setLong(1, idProiect);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long idFisier = resultSet.getLong("idFisier");
                    long idUtilizator = resultSet.getLong("idUtilizator");
                    String numeFisier = resultSet.getString("numeFisier");
                    InputStream continutFisier = resultSet.getBinaryStream("continutFisier");
                    String numeUtilizator = resultSet.getString("numeUtilizator");
                    String numeFamilie = resultSet.getString("nume");
                    String prenumeUtilizator = resultSet.getString("prenume");

                    ProiectTrimisProfesor fisierProiect = new ProiectTrimisProfesor(idProiect, idFisier, idUtilizator,
                            numeFisier, continutFisier, numeUtilizator, numeFamilie, prenumeUtilizator);
                    fisierProiect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            DirectoryChooser directoryChooser = new DirectoryChooser();
                            File directorSelectat = directoryChooser.showDialog(fereastraRadacina);
                            if (directorSelectat != null) {
                                String caleFisier = directorSelectat.getAbsolutePath() + "\\" + fisierProiect.getNumeFisier();
                                try (FileOutputStream fileOutputStream = new FileOutputStream(caleFisier)) {
                                    fileOutputStream.write(fisierProiect.getContinutFisier().readAllBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    containerFisiereProiecte.getChildren().add(fisierProiect);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            selectorNoulTermen.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate noulTermenLimita) {
                    if (noulTermenLimita != null) {
                        try {
                            PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"Proiecte\" SET \"termenPredare\" = ? " +
                                    "WHERE \"idProiect\" = ? AND \"idCurs\" = ? AND \"idCapitol\" = ?;");
                            Date termenLimita = Date.valueOf(noulTermenLimita);
                            preparedStatement.setDate(1, termenLimita);
                            preparedStatement.setLong(2, idProiect);
                            preparedStatement.setLong(3, idCurs);
                            preparedStatement.setLong(4, idCapitol);
                            if (preparedStatement.executeUpdate() == 1) {
                                ObservableList<Node> textFlowTermenLimitaChildren = textFlowTermenLimita.getChildren();
                                ((Text) textFlowTermenLimitaChildren.get(textFlowTermenLimitaChildren.size() - 1)).setText(simpleDateFormat.format(termenLimita));
                                selectorNoulTermen.setValue(null);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
