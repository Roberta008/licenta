package com.example.lucrarelicenta2.componente.continut.containere;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ContainerTema {
    private VBox vBoxTema;

    public ContainerTema() {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-tema.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/elemente-curs/element-proiect.fxml"));
        try {
            vBoxTema = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public VBox getvBoxTema() {
        return vBoxTema;
    }
}
