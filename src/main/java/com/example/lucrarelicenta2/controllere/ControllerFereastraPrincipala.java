package com.example.lucrarelicenta2.controllere;

import com.example.lucrarelicenta2.componente.continut.ContinutCurs;
import com.example.lucrarelicenta2.componente.cursuri.CursuriUtilizator;
import com.example.lucrarelicenta2.ferestre.FereastraAutentificare;
import com.example.lucrarelicenta2.ferestre.FereastraPrincipala;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class ControllerFereastraPrincipala implements Initializable {
    private final Stage radacinaAplicatiei;
    @FXML
    private TextField fieldCautareCursuri;
    @FXML
    private VBox containerDinamic;
    @FXML
    private HBox baraDeCautare;
    @FXML
    private Label labelOptiuneCursuri;
    @FXML
    private Label labelOptiuneAutentificat2, labelOptiuneAutentificat3;
    @FXML
    private Label labelAutentificare;
    @FXML
    private FlowPane containerCursuri;
    private Label labelAvertizare;
    private final Utilizator utilizatorAutentificat;

    public ControllerFereastraPrincipala(Stage radacinaAplicatiei, Utilizator utilizatorAutentificat) {
        this.radacinaAplicatiei = radacinaAplicatiei;
        this.utilizatorAutentificat = utilizatorAutentificat;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Afisam elementele ( Cursurile Mele )
        afisareCursuri();

//        Setam imagini labelurilor
        setareImagini();
//        Daca utilizatorul e autentificat
        if (utilizatorAutentificat != null) {
//            Preluam lista cu copii a containerului dinamic ( un Hbox si un FlowPane - deocamdata )
            ObservableList<Node> copiiContainerDinamic = containerDinamic.getChildren();
//            Preia FlowPane-ul
            Node flowPaneCursuri = copiiContainerDinamic
                    .stream()
                    .filter(elementCurent -> elementCurent.getId().equals("containerCursuri"))
                    .findFirst()
                    .orElse(null);

//            Daca suntem intr-un curs ( fereastra cursului ) si apasam pe Cursurile Mele afisam bara de cautare, scoatem fereastra
//            cursului din lista copiilor containerului dinamic si adaugam inapoi containerul cu cursuri!
            labelOptiuneCursuri.setOnMouseClicked(mouseEvent -> {
                assert containerCursuri != null;
                baraDeCautare.setVisible(true);
                copiiContainerDinamic.remove(copiiContainerDinamic.size() - 1);
                copiiContainerDinamic.add(flowPaneCursuri);
            });

            containerCursuri.setAlignment(Pos.TOP_LEFT);
            containerCursuri.getChildren().remove(labelAvertizare);
            CursuriUtilizator obiectCursuriUtilizator = new CursuriUtilizator(utilizatorAutentificat);

//            Lista cu cursurile propriu-zise
            List<VBox> listaElementeCursPrincipale = obiectCursuriUtilizator.getListaCursuriVbox();
            if (listaElementeCursPrincipale != null) {
                List<VBox> listaElementeCursFiltrate = new ArrayList<>();
                for (int i = 0; i < listaElementeCursPrincipale.size(); i++) {
                    int finalI = i;
                    listaElementeCursPrincipale.get(i).setOnMouseClicked(cursCurent -> {
                        baraDeCautare.setVisible(!baraDeCautare.isVisible());
//                    Stergem FlowPane-ul
                        copiiContainerDinamic.remove(copiiContainerDinamic.size() - 1);
//                    Container Curs este un Container care contine TOT CONTINUTUL CURSULUI
                        copiiContainerDinamic.add(new ContinutCurs(obiectCursuriUtilizator.getListaCursuri().get(finalI),
                                radacinaAplicatiei, utilizatorAutentificat).getContainerCurs());
                    });
                }
                fieldCautareCursuri.textProperty().addListener((observableValue, valoareVecheTitluCurs, valoareNouaTitluCurs) -> {
                    if (valoareNouaTitluCurs.isEmpty()) {
                        containerCursuri.getChildren().clear();
                        containerCursuri.getChildren().addAll(listaElementeCursPrincipale);
                    } else {
                        listaElementeCursFiltrate.clear();
                        listaElementeCursPrincipale.forEach(elementCurs -> {
                            elementCurs.getChildren().forEach(copilElementCurs -> {
                                String idCopilElementCurs = copilElementCurs.getId();
                                if (idCopilElementCurs != null && idCopilElementCurs.equals("containerInformatiiCurs")) {
                                    ((VBox) copilElementCurs).getChildren().forEach(copilCurent -> {
                                        String idCopilCurent = copilCurent.getId();
                                        if (idCopilCurent != null && idCopilCurent.equals("labelTitluCurs")) {
                                            String titluCursCurent = ((TextFlow) copilCurent).getChildren().get(0).toString();
                                            if (titluCursCurent.contains(valoareNouaTitluCurs)) {
                                                if (!listaElementeCursFiltrate.contains(elementCurs)) {
                                                    listaElementeCursFiltrate.add(elementCurs);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        });
                        containerCursuri.getChildren().clear();
                        containerCursuri.getChildren().addAll(listaElementeCursFiltrate);
                    }
                });
                containerCursuri.getChildren().addAll(listaElementeCursPrincipale);
            } else {
                containerCursuri.setAlignment(Pos.CENTER);
                containerCursuri.getChildren().add(new Label("Deocamdata niciun profesor nu te-a inscris la un curs!"));
            }
        } else {
            labelAvertizare = new Label("Pentru a vedea cursurile trebuie sa te autentifici!");
            labelAvertizare.setStyle("-fx-text-fill: #cc33ff; -fx-font-size: 15; -fx-font-weight: bold; -fx-font-style: italic");
            containerCursuri.setAlignment(Pos.CENTER);
            containerCursuri.getChildren().add(labelAvertizare);
        }

        adaugaFunctionalitateLabelAutentificare();
    }

    private void adaugaFunctionalitateLabelAutentificare() {
        labelAutentificare.setText(utilizatorAutentificat == null ? "Autentificare" : "Delogare");
        labelAutentificare.setOnMouseClicked(mouseEvent -> {
            radacinaAplicatiei.setScene(utilizatorAutentificat == null
                    ? new FereastraAutentificare(radacinaAplicatiei).getScenaAutentificare()
                    : new FereastraPrincipala(radacinaAplicatiei, null).getMainScene());
        });
    }

    private void afisareCursuri() {
        labelOptiuneCursuri.setVisible(utilizatorAutentificat != null);
        baraDeCautare.setVisible(utilizatorAutentificat != null);
    }

    private void setareImagini() {
        FontIcon iconAutentificare1 = new FontIcon("bi-book");
        iconAutentificare1.setIconColor(Color.WHITE);
        iconAutentificare1.setIconSize(20);
        labelOptiuneCursuri.setGraphic(iconAutentificare1);
        FontIcon iconAutentificare = new FontIcon(utilizatorAutentificat == null ? "bi-person-plus" : "bi-person-dash");
        iconAutentificare.setIconColor(Color.WHITE);
        iconAutentificare.setIconSize(20);
        labelAutentificare.setGraphic(iconAutentificare);
    }
}
