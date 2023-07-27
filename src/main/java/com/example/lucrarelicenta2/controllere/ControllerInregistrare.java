package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.ferestre.FereastraAutentificare;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerInregistrare implements Initializable {
    @FXML
    private Label labelEroareEmail;
    @FXML
    private Label labelEroareParolaConf;
    @FXML
    private Label labelEroareParola;
    @FXML
    private Label labelEroareNumeUtilizator;
    @FXML
    private TextField fieldPrenume;
    @FXML
    private TextField fieldNumeUtilizator;
    @FXML
    private Label labelEroare;
    @FXML
    private Label labelAutentificare;
    @FXML
    private TextField fieldEmail;
    @FXML
    private TextField fieldNume;
    @FXML
    private PasswordField fieldParolaInreg;
    @FXML
    private PasswordField fieldConfParola;
    @FXML
    private Button butonInregistrare;
    @FXML
    private Stage fereastraRadacina;

    public ControllerInregistrare() {

    }

    public ControllerInregistrare(Stage fereastraRadacina) {
        this.fereastraRadacina = fereastraRadacina;
    }

    @SuppressWarnings({"Convert2Lambda", "Convert2Diamond"})
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelEroare.setVisible(false);
        labelEroareParola.setVisible(false);
        labelEroareParolaConf.setVisible(false);
        labelEroareNumeUtilizator.setVisible(false);
        labelEroareEmail.setVisible(false);

        labelAutentificare.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FereastraAutentificare fereastraAutentificare = new FereastraAutentificare(fereastraRadacina);
                fereastraRadacina.setTitle("Autentificare");
                fereastraRadacina.setScene(fereastraAutentificare.getScenaAutentificare());
            }
        });
        butonInregistrare.setOnMouseClicked(new EventHandler<MouseEvent>() {


            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean toateFieldurileSuntPline = toateFieldurileSuntCompletate();
                if (toateFieldurileSuntPline) {
                    boolean EmailValid = verificaEmail();
                    boolean numeUtilizatorValid = verificareNumeUtilizator();
                    boolean parolaValidata = verificareParola();
                    boolean paroleEgale=verificareParolaSimilare();
                    if (toateFieldurileSuntPline && EmailValid && numeUtilizatorValid && parolaValidata) {
//                        System.out.println("Putem crea cont");
                        labelEroare.setVisible(false);
                        labelEroareParola.setVisible(false);
                        labelEroareParolaConf.setVisible(false);
                        labelEroareNumeUtilizator.setVisible(false);
                        labelEroareEmail.setVisible(false);
                        BazaDeDate bazaDeDate = new BazaDeDate();
                        boolean UtilizatorCreatCuSucces = bazaDeDate.creareUtilizator(fieldNumeUtilizator.getText(), fieldNume.getText(), fieldPrenume.getText(), fieldParolaInreg.getText(), fieldEmail.getText());
                        if(UtilizatorCreatCuSucces){
                            fereastraRadacina.setScene(new FereastraAutentificare(fereastraRadacina).getScenaAutentificare());
                        }
                    } else {
                        labelEroare.setVisible(!toateFieldurileSuntPline);
                        labelEroareEmail.setVisible(!EmailValid);
                        labelEroareNumeUtilizator.setVisible(!numeUtilizatorValid);
                        labelEroareParola.setVisible(!parolaValidata);
                        labelEroareParolaConf.setVisible(!paroleEgale);
                    }

                } else {
                    labelEroare.setVisible(!toateFieldurileSuntPline);

                }


            }
        });

    }

    private boolean toateFieldurileSuntCompletate() {
//        System.out.println("fieldEmail.getText() = " + fieldEmail.getText());
        return !fieldEmail.getText().isEmpty() && !fieldNume.getText().isEmpty() && !fieldPrenume.getText().isEmpty()
                && !fieldNumeUtilizator.getText().isEmpty() && !fieldParolaInreg.getText().isEmpty() && !fieldConfParola.getText().isEmpty();
    }

    private boolean verificaEmail() {
        String expresiePtEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_-]+\\.[A-Za-z0-9+_-]+$";
        //            pattern-ul un model pe care trebuie sa il respecte expresia ca sa fie email
        Pattern patternEmail = Pattern.compile(expresiePtEmail);
        Matcher matcher = patternEmail.matcher(fieldEmail.getText());
        return matcher.matches();
    }

    private boolean verificareNumeUtilizator() {
        return fieldNumeUtilizator.getText().length() <= 20;

    }


    private boolean verificareParola() {
        boolean contineCaracterSpecial = false;
        boolean contineCifra = false;
        boolean contineMajuscula = false;
        String sirCaractereSpeciale = "!@#$%^&*(){}|[].,:;+=-_/\\";
        for (int indexCaracterCurent = 0; indexCaracterCurent < sirCaractereSpeciale.length(); indexCaracterCurent++) {
            // functia containes trebuie sa primeasca un sir asa ca convertim caracterul la sir
            String caracterCurentSir = String.valueOf(sirCaractereSpeciale.charAt(indexCaracterCurent));
            if (fieldParolaInreg.getText().contains(caracterCurentSir)) {
                contineCaracterSpecial = !contineCaracterSpecial;
                break;
            }
        }
        for (char caracterCurent : fieldParolaInreg.getText().toCharArray()) {
            if (Character.isUpperCase(caracterCurent)) {
                contineMajuscula = true;
            }
            if (Character.isDigit(caracterCurent)) {
                contineCifra = true;
            }
        }
        return fieldParolaInreg.getText().length() >= 5 && contineCaracterSpecial && contineMajuscula && contineCifra;
    }

    private boolean verificareParolaSimilare() {
        return fieldParolaInreg.getText().equals(fieldConfParola.getText());
    }

}
