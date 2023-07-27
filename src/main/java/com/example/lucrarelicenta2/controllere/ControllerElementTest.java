package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.modele.Intrebare;
import com.example.lucrarelicenta2.modele.Utilizator;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ControllerElementTest implements Initializable {
    private final long idElement;
    private final long idCapitol;
    private final String titluElement;
    private final List<Intrebare> listaIntrebari;
    private final Utilizator utilizatorAutentificat;
    @FXML
    private Label labelTitluElement;
    @FXML
    private Button butonTrimiteTest;
    @FXML
    private VBox containerElement;

    public ControllerElementTest(long idElement, long idCapitol, String titluElement, List<Intrebare> listaIntrebari, Utilizator utilizatorAutentificat) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.listaIntrebari = listaIntrebari;
        this.utilizatorAutentificat = utilizatorAutentificat;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelTitluElement.setText(titluElement);

        String statusTest = "";
        int punctajTest = 0;

        Connection conexiuneBazaDeDate = new BazaDeDate().getConexiuneBazaDeDate();
        try {
            PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("select \"statusTest\", \"punctajTest\" " +
                    "from \"StatusTesteStudenti\" where \"idTest\" = ? and \"idCapitol\" = ? and \"idUtilizator\" = ?;");
            preparedStatement.setLong(1, idElement);
            preparedStatement.setLong(2, idCapitol);
            preparedStatement.setLong(3, utilizatorAutentificat.getIdUtilizator());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                statusTest = resultSet.getString("statusTest");
                punctajTest = resultSet.getInt("punctajTest");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        boolean testulAMaiFostCompletat = !statusTest.isEmpty();
        if (testulAMaiFostCompletat) {
            Label labelStatusTest = new Label();
            labelStatusTest.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: #99ff33");
            if (statusTest.equals("TRECUT")) {
                labelStatusTest.setText("Ai trecut cu succes testul cu %s puncte!".formatted(punctajTest));
                containerElement.setStyle(containerElement.getStyle() + "; -fx-border-color: #99ff33");
            } else {
                labelStatusTest.setText("Din pacate ai picat testul, punctajul tau este %s".formatted(punctajTest));
                labelStatusTest.setStyle(labelStatusTest.getStyle() + "; -fx-text-fill: #ff0066");
                containerElement.setStyle(containerElement.getStyle() + "; -fx-border-color: #ff0066");
            }
            containerElement.getChildren().add(labelStatusTest);
        }

        for (int i = 0; i < listaIntrebari.size(); i++) {
            Intrebare intrebareCurenta = listaIntrebari.get(i);
            VBox containerIntrebare = new VBox(new Label(i + 1 + ". " + intrebareCurenta.getTextIntrebare()));
            VBox.setMargin(containerIntrebare, new Insets(10.0, 15.0, 0.0, 15.0));
            containerIntrebare.setStyle("-fx-border-color: #bfbfbf; -fx-border-width: 2; -fx-border-radius: 25; " +
                    "-fx-padding: 15; -fx-pref-width: 100; -fx-font-size: 18; -fx-background-radius: 25");
            List<String> listaRaspunsuriIntrebare = intrebareCurenta.getListaRaspunsuriIntrebare();
            ToggleGroup radioToggleGroup = new ToggleGroup();
            for (String raspunsCurent : listaRaspunsuriIntrebare) {
                RadioButton radioButtonRaspuns = new RadioButton(raspunsCurent);
                radioButtonRaspuns.setDisable(testulAMaiFostCompletat);
                radioButtonRaspuns.setToggleGroup(radioToggleGroup);
                VBox.setMargin(radioButtonRaspuns, new Insets(5.0, 0.0, 0.0, 20.0));
                radioButtonRaspuns.setStyle("-fx-font-style: italic");
                containerIntrebare.getChildren().add(radioButtonRaspuns);
            }
            containerElement.getChildren().add(containerIntrebare);
        }

        butonTrimiteTest.setDisable(testulAMaiFostCompletat);

        containerElement.getChildren().remove(butonTrimiteTest);
        containerElement.getChildren().add(butonTrimiteTest);

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

        butonTrimiteTest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean toateIntrebarileCompletate = true;
                int punctajTotal = listaIntrebari.size() * 10;
                System.out.println("punctajTotal = " + punctajTotal);
                int punctajTest = 0;
//                Parcurgem elementele Testului ( sunt de forma Label titluTest ... intrebari ... Buton trimitere Test )
                for (int i = 0; i < containerElement.getChildren().size(); i++) {
//                    Preluam elementul curent
                    Node elementCurent = containerElement.getChildren().get(i);
//                    Verificam daca e instanta de VBox ( daca da, inseamna ca avem de a face cu o intrebare )
                    if (elementCurent instanceof VBox elementIntrebare) {
                        if (!toateIntrebarileCompletate) {
                            break;
                        }
//                        Ii preluam copiii ( va fi un Label - Intrebarea propriu zisa si RadioButtons - Raspunsurile )
                        ObservableList<Node> elementeIntrebare = elementIntrebare.getChildren();
//                        Aici extragem text-ul intrebarii ( de ex.. Care dintre urmatorii algoritmi bla bla ... )
                        Label labelTitluIntrebare = (Label) elementeIntrebare.get(0);
                        String titluIntrebare = labelTitluIntrebare.getText();
                        titluIntrebare = titluIntrebare.substring(titluIntrebare.indexOf(".") + 2);

//                        Dupa asta incepem sa parcurgem raspunsurile intrebarii curente ( de la 1 ca pe 0 e cerinta )
                        for (int j = 1; j < elementeIntrebare.size(); j++) {
//                            Daca e RadioButton ( n-are ce sa fie altceva )
                            if (elementeIntrebare.get(j) instanceof RadioButton butonRadioCurent) {
                                if (butonRadioCurent.getToggleGroup().getSelectedToggle() == null) {
                                    toateIntrebarileCompletate = false;
                                    break;
                                }
//                                Si daca e selectat ( altfel returnam false )
                                if (butonRadioCurent.isSelected()) {
//                                if (butonRadioCurent.getToggleGroup().getSelectedToggle() != null) {
//                                    cautam in lista de intrebari intrebarea curenta la care suntem acum ( dupa textIntrebare )
                                    for (Intrebare intrebareCurenta : listaIntrebari) {
                                        String textIntrebare = intrebareCurenta.getTextIntrebare();
//                                        Daca am gasit-o
                                        if (textIntrebare.equals(titluIntrebare)) {
//                                            Verificam daca ce a selectat utilizatorul corespunde cu raspunsul intrebarii
                                            if (intrebareCurenta.getRaspunsCorect().equals(butonRadioCurent.getText())) {
                                                System.out.println("\tbutonRadioCurent.getText() = " + butonRadioCurent.getText());
                                                System.out.printf("Utilizatorul a raspuns corect la %s ( raspuns corect = %s )%n",
                                                        textIntrebare, intrebareCurenta.getRaspunsCorect());
                                                punctajTest += 10;
                                                elementIntrebare.setStyle(elementIntrebare.getStyle() + "; -fx-border-color: #99ff33; -fx-background-color: #f2ffe6");
                                            } else {
                                                elementIntrebare.setStyle(elementIntrebare.getStyle() + "; -fx-border-color: #ff0066; -fx-background-color: #ffe6f0");
                                            }
                                            for (Toggle butonCurent : butonRadioCurent.getToggleGroup().getToggles()) {
                                                ((RadioButton) butonCurent).setDisable(true);
                                            }
//                                            break;
                                        }
//                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                Label labelStatusTest = new Label();
                labelStatusTest.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: #99ff33");
                boolean aTrecutTestul = punctajTest >= (punctajTotal / 2);
                if (!aTrecutTestul || !toateIntrebarileCompletate) {
                    labelStatusTest.setStyle(labelStatusTest.getStyle() + "; -fx-text-fill: #ff0066");
                }
                String statusTest = !toateIntrebarileCompletate
                        ? "Toate intrebarile trebuie sa fie completate!"
                        : aTrecutTestul
                        ? "Ai trecut cu succes testul cu %s puncte!".formatted(punctajTest)
                        : "Din pacate ai picat testul, punctajul tau este %s".formatted(punctajTest);
                labelStatusTest.setText(statusTest);

                containerElement.getChildren().add(1, labelStatusTest);

                if (toateIntrebarileCompletate) {
                    Connection conexiuneBazaDeDate = new BazaDeDate().getConexiuneBazaDeDate();
                    try {
                        PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("insert into \"StatusTesteStudenti\" (\"idTest\", \"idCapitol\", " +
                                "\"idUtilizator\", \"statusTest\", \"punctajTest\") values (?, ?, ?, ?, ?);");
                        preparedStatement.setLong(1, idElement);
                        preparedStatement.setLong(2, idCapitol);
                        preparedStatement.setLong(3, utilizatorAutentificat.getIdUtilizator());
                        preparedStatement.setString(4, aTrecutTestul ? "TRECUT" : "PICAT");
                        preparedStatement.setInt(5, punctajTest);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
