package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.elemente.profesor.FisiereTemeProfesor;
import com.example.lucrarelicenta2.elemente.profesor.TemaTrimisaProfesor;
import com.example.lucrarelicenta2.modele.Profesor;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.scene.control.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControllerTemeProfesor implements Initializable {

    private final long idTema, idCapitol;
    private final String titluTema, cerintaTema;
    private final Date termenCreare;
    private final Date termenLimita;
    private final Profesor profesorConectat;
    private final Stage fereastraRadacina;
    public Button butonTrimiteMesaj;
    public DatePicker selectorTermenLimita;
    @FXML
    private VBox containerElement, containerMesaje;
    @FXML
    private Label labelTitlu, labelCerinta;
    @FXML
    private FlowPane containerTemeTrimise, containerStudentiAsignati;
    @FXML
    private ComboBox<String> comboBoxAsignareElevi;
    @FXML
    private HBox alTreileaHBox;
    @FXML
    private TextField fieldMesaj;
    private List<Label> listaMesajeUtilizatori;
    private List<Utilizator> listaUtilizatoriInscrisi;
    private List<Utilizator> listaUtilizatoriPentruInscris;

    public ControllerTemeProfesor(long idTema, long idCapitol, String titluTema, String cerintaTema,
            Date termenCreare, Date termenLimita, Profesor profesorConectat, Stage fereastraRadacina) {
        this.idTema = idTema;
        this.idCapitol = idCapitol;
        this.titluTema = titluTema;
        this.cerintaTema = cerintaTema;
        this.termenCreare = termenCreare;
        this.termenLimita = termenLimita;
        this.profesorConectat = profesorConectat;
        this.fereastraRadacina = fereastraRadacina;
        listaUtilizatoriInscrisi = new ArrayList<>();
        listaUtilizatoriPentruInscris = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        labelTitlu.setText(titluTema);
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
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"Teme\" SET \"titluElement\" = ? " +
                                "WHERE \"idElement\" = ? AND \"idCapitol\" = ?;");
                        preparedStatement.setString(1, labelTitlu.getText());
                        preparedStatement.setLong(2, idTema);
                        preparedStatement.setLong(3, idCapitol);
                        int executeUpdate = preparedStatement.executeUpdate();
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

        labelCerinta.setText(cerintaTema);
        final String[] mesajInitial = {labelCerinta.getText()};
        labelCerinta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    labelCerinta.setText("");
                    labelCerinta.requestFocus();
                }
            }
        });
        labelCerinta.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    labelCerinta.getParent().requestFocus();
                    keyEvent.consume();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"Teme\" SET \"taskElement\" = ? " +
                                "WHERE \"idElement\" = ? AND \"idCapitol\" = ?;");
                        preparedStatement.setString(1, labelCerinta.getText());
                        preparedStatement.setLong(2, idTema);
                        preparedStatement.setLong(3, idCapitol);
                        if (preparedStatement.executeUpdate() != 1) {
                            labelCerinta.setText(mesajInitial[0]);
                        } else {
                            mesajInitial[0] = labelCerinta.getText();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    String textDescriereCurs = labelCerinta.getText();
                    if (!textDescriereCurs.isEmpty()) {
                        labelCerinta.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                    }
                } else {
                    String upperCaseText = keyEvent.getText();
                    if (keyEvent.isShiftDown()) {
                        upperCaseText = upperCaseText.toUpperCase();
                    }
                    labelCerinta.setText(labelCerinta.getText() + upperCaseText);
                    keyEvent.consume();
                }
            }
        });
        labelCerinta.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelCerinta.setStyle("-fx-font-size: 18; -fx-font-style: italic; " +
                        "-fx-text-fill: #cccccc; -fx-cursor: hand");
            }
        });
        labelCerinta.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelCerinta.setStyle("-fx-font-size: 18; -fx-font-style: italic; " +
                        "-fx-text-fill: #b3b3b3; -fx-cursor: hand");
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("RO"));

        TextFlow textFlowDataCreare = new TextFlow();
        Text textDataCreare1 = new Text("Ai creat Tema pe data de: ");
        textDataCreare1.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill: #b3b3b3");
        Text textDataCreare2 = new Text(simpleDateFormat.format(termenCreare));
        textDataCreare1.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #cc33ff");
        textFlowDataCreare.getChildren().addAll(textDataCreare1, textDataCreare2);
        containerElement.getChildren().add(1, textFlowDataCreare);
        textFlowDataCreare.setTextAlignment(TextAlignment.CENTER);

        TextFlow textFlowTermenLimita = new TextFlow();
        Text textTermenLimita1 = new Text("Termenul limita este: ");
        textTermenLimita1.setStyle("-fx-font-size: 14; -fx-font-style: italic; -fx-fill: #b3b3b3");
        Text textTermenLimita2 = new Text(simpleDateFormat.format(termenLimita));
        textTermenLimita2.setStyle("-fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; -fx-fill: #cc33ff");
        textFlowTermenLimita.getChildren().addAll(textTermenLimita1, textTermenLimita2);
        containerElement.getChildren().add(2, textFlowTermenLimita);
        textFlowTermenLimita.setTextAlignment(TextAlignment.CENTER);

        List<TemaTrimisaProfesor> listaTemeTrimise = new BazaDeDate().getTemeTrimise(idTema, idCapitol);
        if (listaTemeTrimise.size() == 0) {
            Label labelNicioTema = new Label("Deocamdata studentii nu au trimis nicio tema!");
            labelNicioTema.setStyle("-fx-padding: 20; -fx-background-color: white; -fx-background-radius: 25; " +
                    "-fx-text-fill: #cc33ff; -fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1)");
            containerTemeTrimise.getChildren().add(labelNicioTema);
        } else {
            for (TemaTrimisaProfesor elementCurent : listaTemeTrimise) {
                containerTemeTrimise.getChildren().add(elementCurent);
                for (FisiereTemeProfesor fisierTema : elementCurent.getFisiereTema()) {
                    fisierTema.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            DirectoryChooser directoryChooser = new DirectoryChooser();
                            File locatieDirector = directoryChooser.showDialog(fereastraRadacina);
                            if (locatieDirector != null) {
                                String locatieFisier = locatieDirector.getAbsolutePath() + "\\" + fisierTema.getNumeFisier();
                                try (FileOutputStream fileOutputStream = new FileOutputStream(locatieFisier)) {
                                    fileOutputStream.write(fisierTema.getContinutFisier().readAllBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        }

//        Preluam utilizatorii asignat acestei teme
        if (conexiuneBD != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("SELECT DISTINCT T2.* FROM \"InscrieriTeme\" T1 " +
                        "JOIN \"Utilizatori\" T2 ON T1.\"idUtilizator\" = T2.\"idUtilizator\" " +
                        "JOIN \"Inscrieri\" I ON T2.\"idUtilizator\" = I.\"idUtilizator\" WHERE \"idTema\" = ?;");
                preparedStatement.setLong(1, idTema);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    listaUtilizatoriInscrisi.add(new Utilizator(resultSet.getLong("idUtilizator"),
                            resultSet.getString("numeUtilizator"), resultSet.getString("nume"),
                            resultSet.getString("prenume"), resultSet.getString("parola"),
                            resultSet.getString("e-mail")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (Utilizator utilizatorCurent : listaUtilizatoriInscrisi) {
            containerStudentiAsignati.getChildren().add(creazaVBoxUtilizator(utilizatorCurent));
        }

//        Preluam schimbul de mesaje intre Utilizatori din Baza de Date
        ObservableList<Node> copiiContainerMesaje = containerMesaje.getChildren();
        try {
            BazaDeDate obiectBD = new BazaDeDate();
            List<Label> listaMesajeElement = obiectBD.preluareMesajeElement(idCapitol, idTema);
            List<Label> mesajeProfesor = obiectBD.preluareMesajeProfesor(idCapitol, idTema, profesorConectat.getIdProfesor());
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

//        Utilizatorii pe care putem sa-i asignam
        if (conexiuneBD != null) {
            try {
                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("select * from \"Utilizatori\" " +
                        "join \"Inscrieri\" I on \"Utilizatori\".\"idUtilizator\" = I.\"idUtilizator\" " +
                        "full outer join \"InscrieriTeme\" IT on \"Utilizatori\".\"idUtilizator\" = IT.\"idUtilizator\" " +
                        "where \"idCurs\" = ( select \"Capitole\".\"idCurs\" from \"Capitole\" where \"idCapitol\" = ? ) and \"idTema\" is null;");
                preparedStatement.setLong(1, idCapitol);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    listaUtilizatoriPentruInscris.add(new Utilizator(resultSet.getLong("idUtilizator"),
                            resultSet.getString("numeUtilizator"), resultSet.getString("nume"),
                            resultSet.getString("prenume"), resultSet.getString("parola"),
                            resultSet.getString("e-mail")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (listaUtilizatoriPentruInscris.size() > 0) {
            for (Utilizator utilizatorCurent : listaUtilizatoriPentruInscris) {
                comboBoxAsignareElevi.getItems().add(utilizatorCurent.toString());
            }
//            comboBoxAsignareElevi.setValue(comboBoxAsignareElevi.getItems().get(0));
        }

        comboBoxAsignareElevi.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String vecheaValoare,
                    String numePrenumeUtilizator) {
                for (Utilizator utilizatorCurent : listaUtilizatoriPentruInscris) {
                    if (numePrenumeUtilizator.contains(utilizatorCurent.getNumeFamilie()) &&
                            numePrenumeUtilizator.contains(utilizatorCurent.getPrenumeUtilizator())) {
                        containerStudentiAsignati.getChildren().add(creazaVBoxUtilizator(utilizatorCurent));
                        if (conexiuneBD != null) {
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("insert into \"InscrieriTeme\" " +
                                        "(\"idUtilizator\", \"idTema\") values (?, ?);");
                                preparedStatement.setLong(1, utilizatorCurent.getIdUtilizator());
                                preparedStatement.setLong(2, idTema);
                                if (preparedStatement.executeUpdate() == 1) {
                                    PreparedStatement preparedStatement1 = conexiuneBD.prepareStatement("insert into \"StatusTeme\" " +
                                            "(\"idTema\", \"idUtilizator\", \"statusTema\") values (?, ?, ?);");
                                    preparedStatement1.setLong(1, idTema);
                                    preparedStatement1.setLong(2, utilizatorCurent.getIdUtilizator());
                                    preparedStatement1.setString(3, "Lipseste");
                                    preparedStatement1.executeUpdate();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                }
            }
        });

        butonTrimiteMesaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String mesajProfesor = fieldMesaj.getText();
                if (!mesajProfesor.isEmpty()) {;
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("INSERT INTO \"MesajeProfesori\" " +
                                "(\"idProfesor\", \"timeStampMesaj\", \"idCapitol\", \"idElement\", \"mesajProfesor\") " +
                                "VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?);");
                        preparedStatement.setLong(1, profesorConectat.getIdProfesor());
                        preparedStatement.setLong(2, idCapitol);
                        preparedStatement.setLong(3, idTema);
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

        selectorTermenLimita.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate noulTermenLimita) {
                if (noulTermenLimita != null) {
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"Teme\" SET \"termenLimita\" = ? " +
                                "WHERE \"idCapitol\" = ? AND \"idElement\" = ?;");
                        Date termenLimita = Date.valueOf(noulTermenLimita);
                        preparedStatement.setDate(1, termenLimita);
                        preparedStatement.setLong(2, idCapitol);
                        preparedStatement.setLong(3, idTema);
                        if (preparedStatement.executeUpdate() == 1) {
                            ObservableList<Node> textFlowTermenLimitaChildren = textFlowTermenLimita.getChildren();
                            ((Text) textFlowTermenLimitaChildren.get(textFlowTermenLimitaChildren.size() - 1)).setText(simpleDateFormat.format(termenLimita));
                            selectorTermenLimita.setValue(null);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private VBox creazaVBoxUtilizator(Utilizator utilizatorCurent) {
        VBox containerElementeUtilizator = new VBox();
        Tooltip tooltipUtilizator = new Tooltip("Apasa click odata pentru a afla informatii suplimentare sau dublu click pentru a sterge utilizatorul");
        Tooltip.install(containerElementeUtilizator, tooltipUtilizator);
        containerElementeUtilizator.setAlignment(Pos.CENTER);
        FontIcon userIcon = new FontIcon("anto-user");
        userIcon.setIconColor(Color.valueOf("#cc33ff"));
        userIcon.setIconSize(50);
        Label labelNumePrenume = new Label(utilizatorCurent.getNumeFamilie() + " " + utilizatorCurent.getPrenumeUtilizator());
        labelNumePrenume.setGraphic(userIcon);
        labelNumePrenume.setContentDisplay(ContentDisplay.TOP);
        labelNumePrenume.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: #cc33ff");
        Label labelNumeUtilizator = new Label("( " + utilizatorCurent.getNumeUtilizator() + " )");
        labelNumeUtilizator.setStyle("-fx-text-fill: #b3b3b3; -fx-font-size: 14; -fx-font-style: italic");
        containerElementeUtilizator.setStyle("-fx-background-color: white; -fx-padding: 35 20; " +
                "-fx-background-radius: 25; -fx-cursor: hand; -fx-font-size: 14; -fx-font-style: italic; -fx-font-weight: bold; " +
                "-fx-effect:  dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1)");
        containerElementeUtilizator.getChildren().addAll(labelNumePrenume, labelNumeUtilizator);
        adaugaAnimatie(containerElementeUtilizator);
        return containerElementeUtilizator;
    }

    private void adaugaAnimatie(Parent containerElementeUtilizator) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), containerElementeUtilizator);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        ScaleTransition scaleOutTransition = new ScaleTransition(Duration.millis(200), containerElementeUtilizator);
        scaleOutTransition.setFromX(1.05);
        scaleOutTransition.setFromY(1.05);
        scaleOutTransition.setToX(1.0);
        scaleOutTransition.setToY(1.0);

        containerElementeUtilizator.setOnMouseEntered(mouseEvent -> scaleTransition.playFromStart());
        containerElementeUtilizator.setOnMouseExited(mouseEvent -> scaleOutTransition.playFromStart());
    }

}
