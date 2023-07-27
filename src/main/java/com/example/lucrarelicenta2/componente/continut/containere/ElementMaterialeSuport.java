package com.example.lucrarelicenta2.componente.continut.containere;

import com.example.lucrarelicenta2.controllere.ControllerElementMaterialeSuport;
import com.example.lucrarelicenta2.elemente.FisierMaterialeSuport;
import com.example.lucrarelicenta2.modele.Utilizator;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ElementMaterialeSuport {
    private final long idElement;
    private final long idCapitol;
    private final String titluElement;
    private final String mesajElement;
    private final List<FisierMaterialeSuport> listaFisiereMaterialeSuport;
    private VBox containerProiect;

    public ElementMaterialeSuport(long idElement, long idCapitol, String titluElement, String mesajElement,
            List<FisierMaterialeSuport> listaFisiereMaterialeSuport, Stage radacinaAplicatiei, Utilizator utilizatorAutentificat) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.mesajElement = mesajElement;
        this.listaFisiereMaterialeSuport = listaFisiereMaterialeSuport;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-materiale-suport.fxml"));
        try {
            fxmlLoader.setControllerFactory(aClass -> new ControllerElementMaterialeSuport(idElement, idCapitol, titluElement, mesajElement,
                    listaFisiereMaterialeSuport, radacinaAplicatiei, utilizatorAutentificat));
            containerProiect = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox getContainerProiect() {
        return containerProiect;
    }
}
