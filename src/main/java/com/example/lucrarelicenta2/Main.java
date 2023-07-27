package com.example.lucrarelicenta2;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.ferestre.FereastraPrincipala;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage scenaRadacina) throws SQLException, IOException {
        FereastraPrincipala fereastraPrincipala = new FereastraPrincipala(scenaRadacina, null);
        scenaRadacina.setScene(fereastraPrincipala.getMainScene());
        scenaRadacina.show();
//        FileChooser fileChooser = new FileChooser();
//        File selectedFile = fileChooser.showOpenDialog(scenaRadacina);
//        FileInputStream fileInputStream = new FileInputStream(selectedFile);
//        byte[] fileContentBytes = fileInputStream.readAllBytes();
//
//        Connection conexiuneBazaDeDate = new BazaDeDate().getConexiuneBazaDeDate();
//        PreparedStatement preparedStatement = conexiuneBazaDeDate.prepareStatement("insert into \"FisiereTeme\" " +
//                "(\"idTema\", \"idUtilizator\", \"numeFisier\", \"continutFisier\") values (?, ?, ?, ?);");
//        preparedStatement.setInt(1, 2);
//        preparedStatement.setInt(2, 1);
//        preparedStatement.setString(3, selectedFile.getName());
//        preparedStatement.setBytes(4, fileContentBytes);
//        preparedStatement.executeUpdate();
    }

    public static void main(String[] args) {
        launch();
    }
}