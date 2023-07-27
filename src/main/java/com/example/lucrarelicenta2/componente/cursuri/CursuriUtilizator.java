package com.example.lucrarelicenta2.componente.cursuri;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.modele.Utilizator;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CursuriUtilizator {
    private final List<Curs> listaCursuri;
    private List<VBox> listaCursuriVbox;
    private FlowPane flowPane;
    public CursuriUtilizator (Utilizator utilizatorAutentificat) {
        this.flowPane = new FlowPane();
        BazaDeDate bazaDeDate = new BazaDeDate();
        listaCursuri = bazaDeDate.preluareListaCursuri(utilizatorAutentificat);
        if (listaCursuri.size() > 0) {
            listaCursuriVbox = new ArrayList<>();
            listaCursuri.forEach(elementCurent -> {
                VBox containerCurs = elementCurent.getContainerCurs();
                listaCursuriVbox.add(containerCurs);
            });
            flowPane.getChildren().addAll(listaCursuriVbox);
        } else {
            flowPane.getChildren().add(new Label("Deocamdata nu exista cursuri la care sa fii inscris!"));
        }
    }

    public List<Curs> getListaCursuri() {
        return listaCursuri;
    }

    public List<VBox> getListaCursuriVbox() {
        return listaCursuriVbox;
    }
}
