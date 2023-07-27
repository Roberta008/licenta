package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementInformatiiGeneraleProfesor;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementMaterialeSuportProfesor;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementProiectProfesor;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementTemeProfesor;
import com.example.lucrarelicenta2.componente.continut.profesor.ElementTestProfesor;
import com.example.lucrarelicenta2.componente.cursuri.CursProfesor;
import com.example.lucrarelicenta2.elemente.profesor.CreareInformatiiGeneraleProfesor;
import com.example.lucrarelicenta2.elemente.profesor.CreareMaterialeSuportProfesor;
import com.example.lucrarelicenta2.elemente.profesor.CreareProiecteProfesor;
import com.example.lucrarelicenta2.elemente.profesor.CreareTemeProfesor;
import com.example.lucrarelicenta2.modele.Profesor;
import com.example.lucrarelicenta2.modele.Utilizator;

import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControllerFereastraContinutCursProfesor implements Initializable {
    private final Profesor profesorConectat;
    private final CursProfesor cursCurent;
    private final Stage fereastraRadacina;
    public VBox containerElement;
    @FXML
    private FlowPane containerContinutCurs;
    @FXML
    private Label labelTitluCurs;
    @FXML
    private Label labelDescriereCurs;
    public FlowPane containerUtilizatoriInscrisi;
    public ComboBox<String> comboBoxUtilizatori;
    @FXML
    private FlowPane containerButoaneElemente;
    @FXML
    private FlowPane containerStatistici;
    private final List<Utilizator> listaUtilizatoriInscrisi;
    private ToggleGroup toggleGroupButoaneElemente;
    private List<Utilizator> listaUtilizatoriPentruInscris;
    private List<Integer> statisticiCurs;

    public ControllerFereastraContinutCursProfesor(Profesor profesorConectat, CursProfesor cursCurent, Stage fereastraRadacina) {
        this.profesorConectat = profesorConectat;
        this.cursCurent = cursCurent;
        this.fereastraRadacina = fereastraRadacina;
        this.listaUtilizatoriInscrisi = new ArrayList<>();
        this.listaUtilizatoriPentruInscris = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tooltip tooltipEditare = new Tooltip("Apasa dublu click pentru a incepe sa editezi campul!");
        labelTitluCurs.setText(cursCurent.getTitluCurs());
        labelTitluCurs.setTooltip(tooltipEditare);
        labelTitluCurs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                adaugaFunctionalitateEditareLabels(true);
            }
        });
        labelDescriereCurs.setText(cursCurent.getDescriereCurs());
        labelDescriereCurs.setTooltip(tooltipEditare);
        labelDescriereCurs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                adaugaFunctionalitateEditareLabels(false);
            }
        });

        // Adaugam utilizatorii inscrisi la cursul curent!
        adaugaUtilizatoriInscrisi();

        // Adaugam utilizatorii ce pot fi inscrisi la curs in ComboBox
        adaugaUtilizatoriPentruInscriere();

//        Adaugam utilizatorul in cursul curent
        comboBoxUtilizatori.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String vechiulUtilizatorSelectat, String noulUtilizatorSelectat) {
                if (noulUtilizatorSelectat != null) {
                    int endIndex = noulUtilizatorSelectat.indexOf('(');
                    if (endIndex != -1) {
                        String numePrenumeUtilizator = noulUtilizatorSelectat.substring(0, endIndex).strip();
                        Utilizator utilizatorGasit = gasesteUtilizatorul(numePrenumeUtilizator);
                        if (utilizatorGasit != null) {
                            Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("insert into \"Inscrieri\" " +
                                        "(\"idUtilizator\", \"idCurs\") values (?, ?);");
                                preparedStatement.setLong(1, utilizatorGasit.getIdUtilizator());
                                preparedStatement.setLong(2, cursCurent.getIdCurs());
                                if (preparedStatement.executeUpdate() == 1) {
                                    listaUtilizatoriInscrisi.add(utilizatorGasit);
                                    VBox elementUtilizator = creazaVBoxUtilizator(utilizatorGasit);
                                    containerUtilizatoriInscrisi.getChildren().add(elementUtilizator);
                                    elementUtilizator.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent mouseEvent) {
                                            adaugaFunctionalitateStergere(elementUtilizator, url, resourceBundle);
                                        }
                                    });
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

//        Pentru a sterge Utilizatorul din cursul curent
        containerUtilizatoriInscrisi.getChildren().forEach(new Consumer<Node>() {
            @Override
            public void accept(Node copilCurent) {
                adaugaFunctionalitateStergere(copilCurent, url, resourceBundle);
            }
        });

        // Adaugam statisticile cursului
        adaugaStatisticiCurs();

        // Adaugam o vizualizare a elementelor ( ca sa le putem modifica )
        BazaDeDate bazaDeDate = new BazaDeDate();
        List<ElementInformatiiGeneraleProfesor> informatiiGeneraleProfesor = bazaDeDate.getInformatiiGeneraleProfesor(cursCurent.getIdCurs(), profesorConectat);
        for (ElementInformatiiGeneraleProfesor elementCurent : informatiiGeneraleProfesor) {
//            containerInformatiiGenerale.getChildren().add(elementCurent.getContainerElement());
            containerContinutCurs.getChildren().add(elementCurent);
        }
        List<ElementMaterialeSuportProfesor> materialeSuport = bazaDeDate.getMaterialeSuportProfesor(cursCurent.getIdCurs(),
                profesorConectat, fereastraRadacina);
        for (ElementMaterialeSuportProfesor elementCurent : materialeSuport) {
            containerContinutCurs.getChildren().add(elementCurent);
        }
        List<ElementTemeProfesor> elementeTeme = bazaDeDate.getElementeTemeProfesor(cursCurent.getIdCurs(), profesorConectat, fereastraRadacina);
        for (ElementTemeProfesor elementCurent : elementeTeme) {
            containerContinutCurs.getChildren().add(elementCurent);
        }
        List<ElementProiectProfesor> elementeProiect = bazaDeDate.getProiecteProfesor(cursCurent.getIdCurs(), fereastraRadacina);
        for (ElementProiectProfesor elementCurent : elementeProiect) {
            containerContinutCurs.getChildren().add(elementCurent);
        }
        List<ElementTestProfesor> elementeTest = bazaDeDate.getTesteProfesor(cursCurent.getIdCurs());
        for (ElementTestProfesor elementCurent : elementeTest) {
            containerContinutCurs.getChildren().add(elementCurent);
        }

        // Pentru a adauga elemente noi
        toggleGroupButoaneElemente = new ToggleGroup();
        for (Node butonCurent : containerButoaneElemente.getChildren()) {
            if (butonCurent instanceof RadioButton radioButtonCurent) {
                radioButtonCurent.setToggleGroup(toggleGroupButoaneElemente);
                radioButtonCurent.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean valoareaVeche, Boolean nouaValoare) {
                        radioButtonCurent.setStyle(
                                (nouaValoare == true)
                                        ? "-fx-text-fill: #cc33ff; -fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold;"
                                        : "-fx-text-fill: #b3b3b3; -fx-font-size: 16; -fx-font-style: italic; -fx-font-weight: bold;"
                        );
                    }
                });
            }
        }
        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        toggleGroupButoaneElemente.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle noulButonSelectat) {
                if (noulButonSelectat != null) {
                    String careButon = ((RadioButton) noulButonSelectat).getText();
                    final ObservableList<Node> containerElementChildren = containerElement.getChildren();
                    switch (careButon) {
                        case "Informatii Generale" -> {
                            if (containerElementChildren.get(containerElementChildren.size() - 1) instanceof VBox) {
                                containerElementChildren.remove(containerElementChildren.size() - 1);
                            }
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("select \"idCapitol\" from \"Capitole\" " +
                                        "where \"idCurs\" = ? and \"titluCapitol\" = 'Informatii Generale';");
                                preparedStatement.setLong(1, cursCurent.getIdCurs());
                                ResultSet resultSet = preparedStatement.executeQuery();
                                if (resultSet.next()) {
                                    long idCapitol = resultSet.getLong("idCapitol");
                                    CreareInformatiiGeneraleProfesor creareElement = new CreareInformatiiGeneraleProfesor(idCapitol,
                                            profesorConectat, containerContinutCurs);
                                    VBox.setMargin(creareElement, new Insets(15.0, 0.0, 15.0, 0.0));
                                    containerElementChildren.add(creareElement);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        case "Materiale Suport" -> {
                            if (containerElementChildren.get(containerElementChildren.size() - 1) instanceof VBox) {
                                containerElementChildren.remove(containerElementChildren.size() - 1);
                            }
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("select \"idCapitol\" from \"Capitole\" " +
                                        "where \"idCurs\" = ? and \"titluCapitol\" = 'Materiale Suport';");
                                preparedStatement.setLong(1, cursCurent.getIdCurs());
                                ResultSet resultSet = preparedStatement.executeQuery();
                                if (resultSet.next()) {
                                    long idCapitol = resultSet.getLong("idCapitol");
                                    CreareMaterialeSuportProfesor creareElement = new CreareMaterialeSuportProfesor(idCapitol,
                                            profesorConectat, fereastraRadacina, containerContinutCurs);
                                    VBox.setMargin(creareElement, new Insets(15.0, 0.0, 15.0, 0.0));
                                    containerElementChildren.add(creareElement);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        case "Teme" -> {
                            if (containerElementChildren.get(containerElementChildren.size() - 1) instanceof VBox) {
                                containerElementChildren.remove(containerElementChildren.size() - 1);
                            }
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("select \"idCapitol\" from \"Capitole\" " +
                                        "where \"idCurs\" = ? and \"titluCapitol\" = 'Teme';");
                                preparedStatement.setLong(1, cursCurent.getIdCurs());
                                ResultSet resultSet = preparedStatement.executeQuery();
                                if (resultSet.next()) {
                                    long idCapitol = resultSet.getLong("idCapitol");
                                    CreareTemeProfesor creareElement = new CreareTemeProfesor(idCapitol, profesorConectat, fereastraRadacina, containerContinutCurs);
                                    VBox.setMargin(creareElement, new Insets(15.0, 0.0, 15.0, 0.0));
                                    containerElementChildren.add(creareElement);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        case "Proiecte" -> {
                            if (containerElementChildren.get(containerElementChildren.size() - 1) instanceof VBox) {
                                containerElementChildren.remove(containerElementChildren.size() - 1);
                            }
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("select \"idCapitol\" from \"Capitole\" " +
                                        "where \"idCurs\" = ? and \"titluCapitol\" = 'Proiect';");
                                preparedStatement.setLong(1, cursCurent.getIdCurs());
                                ResultSet resultSet = preparedStatement.executeQuery();
                                if (resultSet.next()) {
                                    long idCapitol = resultSet.getLong("idCapitol");
                                    CreareProiecteProfesor creareElement = new CreareProiecteProfesor(cursCurent.getIdCurs(), idCapitol,
                                            fereastraRadacina, containerContinutCurs, listaUtilizatoriInscrisi);
                                    VBox.setMargin(creareElement, new Insets(15.0, 0.0, 15.0, 0.0));
                                    containerElementChildren.add(creareElement);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    private void adaugaFunctionalitateStergere(Node copilCurent, URL url, ResourceBundle resourceBundle) {
        copilCurent.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                String numePrenumeUtilizator = ((Label) ((VBox) copilCurent).getChildren().get(0)).getText();
                Utilizator utilizatorGasit = null;
                for (Utilizator utilizatorCurent : listaUtilizatoriInscrisi) {
                    if (numePrenumeUtilizator.contains(utilizatorCurent.getNumeFamilie()) &&
                            numePrenumeUtilizator.contains(utilizatorCurent.getPrenumeUtilizator())) {
                        utilizatorGasit = utilizatorCurent;
                        break;
                    }
                }
                if (utilizatorGasit != null) {
                    try {
                        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                        PreparedStatement preparedStatementStergere = conexiuneBD.prepareStatement("delete from \"Inscrieri\" " +
                                "where \"idUtilizator\" = ? and \"idCurs\" = ?;");
                        long idUtilizator = utilizatorGasit.getIdUtilizator();
                        preparedStatementStergere.setLong(1, idUtilizator);
                        preparedStatementStergere.setLong(2, cursCurent.getIdCurs());
                        if (preparedStatementStergere.executeUpdate() == 1) {
                            PreparedStatement preparedStetentInscrieriTeme = conexiuneBD.prepareStatement("delete from " +
                                    "\"InscrieriTeme\" where \"idUtilizator\" = ?;");
                            preparedStetentInscrieriTeme.setLong(1, idUtilizator);
                            if (preparedStetentInscrieriTeme.executeUpdate() == 1) {
                                PreparedStatement preparedStatementStatusTeme = conexiuneBD.prepareStatement("delete " +
                                        "from \"StatusTeme\" where \"idUtilizator\" = ?;");
                                preparedStatementStatusTeme.setLong(1, idUtilizator);
                                preparedStatementStatusTeme.executeUpdate();
                                PreparedStatement preparedStatementStatusProiecte = conexiuneBD.prepareStatement("delete from " +
                                        "\"StatusProiect\" where \"idUtilizator\" = ?;");
                                preparedStatementStatusProiecte.setLong(1, idUtilizator);
                                preparedStatementStatusProiecte.executeUpdate();
                                PreparedStatement preparedStatementFisiereProiecte = conexiuneBD.prepareStatement("delete " +
                                        "from \"FisiereProiecte\" where \"idUtilizator\" = ?;");
                                preparedStatementFisiereProiecte.setLong(1, idUtilizator);
                                preparedStatementFisiereProiecte.executeUpdate();
                                PreparedStatement preparedStatementFisiereTeme = conexiuneBD.prepareStatement("delete from " +
                                        "\"FisiereTeme\" where \"idUtilizator\" = ?;");
                                preparedStatementFisiereTeme.setLong(1, idUtilizator);
                                preparedStatementFisiereTeme.executeUpdate();
                                PreparedStatement preparedStatementMesaje = conexiuneBD.prepareStatement("delete from " +
                                        "\"MesajeUtilizatori\" where \"idUtilizator\" = ?;");
                                preparedStatementMesaje.setLong(1, idUtilizator);
                                preparedStatementMesaje.executeUpdate();
                                PreparedStatement preparedStatementStatusTeste = conexiuneBD.prepareStatement("delete from " +
                                        "\"StatusTesteStudenti\" where \"idUtilizator\" = ?;");
                                preparedStatementStatusTeste.setLong(1, idUtilizator);
                                preparedStatementStatusTeste.executeUpdate();
                                containerUtilizatoriInscrisi.getChildren().remove(copilCurent);
                                listaUtilizatoriInscrisi.remove(utilizatorGasit);
                                containerUtilizatoriInscrisi.getChildren().remove(copilCurent);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Utilizator gasesteUtilizatorul(String numePrenumeUtilizator) {
        Utilizator utilizatorGasit = null;
        for (Utilizator utilizatoriPentruInscris : listaUtilizatoriPentruInscris) {
            if (numePrenumeUtilizator.contains(utilizatoriPentruInscris.getNumeFamilie()) &&
                    numePrenumeUtilizator.contains(utilizatoriPentruInscris.getPrenumeUtilizator())) {
                utilizatorGasit = utilizatoriPentruInscris;
                break;
            }
        }
        return utilizatorGasit;
    }

    private void adaugaStatisticiCurs() {
        statisticiCurs = new BazaDeDate().getStatisticiCurs(cursCurent.getIdCurs());
        TextFlow textFlowInformatiiGenerale = creazaTextFlow("Ai postat un total de ", statisticiCurs.get(0), " elemente informatii generale!");
        TextFlow textFlowMaterialeSuport = creazaTextFlow("Ai postat un total de ", statisticiCurs.get(1), " elemente materiale suport!");
        TextFlow textFlowTeme = creazaTextFlow("Ai postat un total de ", statisticiCurs.get(2), " elemente teme!");
        TextFlow textFlowProiecte = creazaTextFlow("Ai postat un total de ", statisticiCurs.get(3), " elemente proiecte!");
        VBox containerInformatiiGenerale = creazaContainerTextFlow(textFlowInformatiiGenerale);
        VBox containerMaterialeSuport = creazaContainerTextFlow(textFlowMaterialeSuport);
        VBox containerTeme = creazaContainerTextFlow(textFlowTeme);
        VBox containerProiecte = creazaContainerTextFlow(textFlowProiecte);
        containerStatistici.getChildren().addAll(containerInformatiiGenerale, containerMaterialeSuport,
                containerTeme, containerProiecte);
        for (Node elementCurent : containerStatistici.getChildren()) {
            adaugaAnimatie((Parent) elementCurent);
        }
    }

    private VBox creazaContainerTextFlow(TextFlow textFlowInformatiiGenerale) {
        VBox containerInformatiiGenerale = new VBox(textFlowInformatiiGenerale);
        containerInformatiiGenerale.setAlignment(Pos.CENTER);
        containerInformatiiGenerale.setMaxHeight(90.0);
        containerInformatiiGenerale.setPrefWidth(225.0);
        containerInformatiiGenerale.setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-padding: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1)");
        return containerInformatiiGenerale;
    }

    private TextFlow creazaTextFlow(String primaParte, int aDouaParte, String aTreiaParte) {
        TextFlow textFlowInformatiiGenerale = new TextFlow();
        textFlowInformatiiGenerale.setTextAlignment(TextAlignment.CENTER);

        Text textInformatiiGenerale1 = new Text(primaParte);
        textInformatiiGenerale1.setStyle("-fx-font-size: 15; -fx-font-style: italic; -fx-fill: #b3b3b3; -fx-font-weight: bold");

        Text textInformatiiGenerale2 = new Text(String.valueOf(aDouaParte));
        textInformatiiGenerale2.setStyle("-fx-font-size: 20; -fx-font-style: italic; -fx-fill: #cc33ff; -fx-font-weight: bold");

        Text textInformatiiGenerale3 = new Text(aTreiaParte);
        textInformatiiGenerale3.setStyle("-fx-font-size: 15; -fx-font-style: italic; -fx-fill: #b3b3b3; -fx-font-weight: bold");

        textFlowInformatiiGenerale.getChildren().addAll(textInformatiiGenerale1, textInformatiiGenerale2, textInformatiiGenerale3);

        return textFlowInformatiiGenerale;
    }

    private void adaugaUtilizatoriPentruInscriere() {
        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        try {
            PreparedStatement preparedStatement = conexiuneBD.prepareStatement("select distinct T1.\"idUtilizator\", " +
                    "\"numeUtilizator\", nume, prenume, parola, \"e-mail\" from \"Utilizatori\" T1 full outer join " +
                    "\"Inscrieri\" T2 on T1.\"idUtilizator\" = T2.\"idUtilizator\" order by T1.\"idUtilizator\";");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long idUtilizator = resultSet.getLong("idUtilizator");
                boolean utilizatorInscrisLaCurs = false;
                for (Utilizator utilizatorCurent : listaUtilizatoriInscrisi) {
                    if (utilizatorCurent.getIdUtilizator() == idUtilizator) {
                        utilizatorInscrisLaCurs = true;
                        break;
                    }
                }
                if (!utilizatorInscrisLaCurs) {
                    Utilizator utilizatorCurent = new Utilizator(idUtilizator, resultSet.getString("numeUtilizator"),
                            resultSet.getString("nume"), resultSet.getString("prenume"),
                            resultSet.getString("parola"), resultSet.getString("e-mail"));
                    listaUtilizatoriPentruInscris.add(utilizatorCurent);
                    comboBoxUtilizatori.getItems().add(utilizatorCurent.toString());
                }
            }
            if (comboBoxUtilizatori.getItems().size() > 0) {
                comboBoxUtilizatori.setValue(comboBoxUtilizatori.getItems().get(0));
            }
            comboBoxUtilizatori.setMinWidth(350.0);
            comboBoxUtilizatori.setMinHeight(25.0);
            comboBoxUtilizatori.setPadding(new Insets(10, 20, 10, 20));
            comboBoxUtilizatori.setStyle("-fx-border-color: #cc33ff; -fx-border-width: 2; -fx-border-radius: 25; -fx-background-color: transparent");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void adaugaUtilizatoriInscrisi() {
        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        try {
            PreparedStatement preparedStatement = conexiuneBD.prepareStatement("select T1.\"idCurs\", T2.* from \"Inscrieri\" T1 " +
                    "join \"Utilizatori\" T2 on T2.\"idUtilizator\" = T1.\"idUtilizator\" where T1.\"idCurs\" = ?;");
            preparedStatement.setLong(1, cursCurent.getIdCurs());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                long idUtilizator, String numeUtilizator, String numeFamilie, String prenumeUtilizator, String parolaUtilizator, String emailUtilizator
                listaUtilizatoriInscrisi.add(new Utilizator(resultSet.getLong("idUtilizator"), resultSet.getString("numeUtilizator"),
                        resultSet.getString("nume"), resultSet.getString("prenume"),
                        resultSet.getString("parola"), resultSet.getString("e-mail")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        containerUtilizatoriInscrisi.getChildren().addAll(
                listaUtilizatoriInscrisi.stream()
                        .map(this::creazaVBoxUtilizator)
                        .toList()
        );
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

    private void adaugaFunctionalitateEditareLabels(boolean editamTitlul) {
        labelDescriereCurs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    if (editamTitlul) {
                        labelTitluCurs.setText("");
                        labelTitluCurs.requestFocus();
                    } else {
                        labelDescriereCurs.setText("");
                        labelDescriereCurs.requestFocus();
                    }
                }
            }
        });
        if (editamTitlul) {
            labelTitluCurs.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        labelTitluCurs.getParent().requestFocus();
                        keyEvent.consume();
                        // Am terminat de actualizat descrierea / titlul, salvam in Baza de Date!
                        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                        try {
                            String queryEditareCurs = "update \"Cursuri\" set %s = ? where \"idCurs\" = ?;"
                                    .formatted("\"titluCurs\"");
                            PreparedStatement preparedStatement = conexiuneBD.prepareStatement(queryEditareCurs);
                            preparedStatement.setString(1, labelTitluCurs.getText());
                            preparedStatement.setLong(2, cursCurent.getIdCurs());
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                        String textDescriereCurs = labelTitluCurs.getText();
                        if (!textDescriereCurs.isEmpty()) {
                            labelTitluCurs.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                        }
                    } else {
                        String upperCaseText = keyEvent.getText();
                        if (keyEvent.isShiftDown()) {
                            upperCaseText = upperCaseText.toUpperCase();
                        }
                        labelTitluCurs.setText(labelTitluCurs.getText() + upperCaseText);
                        keyEvent.consume();
                    }
                }
            });
        } else {
            labelDescriereCurs.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        labelDescriereCurs.getParent().requestFocus();
                        keyEvent.consume();

                        // Am terminat de actualizat descrierea / titlul, salvam in Baza de Date!
                        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
                        try {
                            String queryEditareCurs = "update \"Cursuri\" set %s = ? where \"idCurs\" = ?;"
                                    .formatted("\"descriereaCursului\"");
                            PreparedStatement preparedStatement = conexiuneBD.prepareStatement(queryEditareCurs);
                            preparedStatement.setString(1, labelDescriereCurs.getText());
                            preparedStatement.setLong(2, cursCurent.getIdCurs());
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                        String textDescriereCurs = labelDescriereCurs.getText();
                        if (!textDescriereCurs.isEmpty()) {
                            labelDescriereCurs.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                        }
                    } else {
                        String upperCaseText = keyEvent.getText();
                        if (keyEvent.isShiftDown()) {
                            upperCaseText = upperCaseText.toUpperCase();
                        }
                        labelDescriereCurs.setText(labelDescriereCurs.getText() + upperCaseText);
                        keyEvent.consume();
                    }
                }
            });
        }
    }
}
