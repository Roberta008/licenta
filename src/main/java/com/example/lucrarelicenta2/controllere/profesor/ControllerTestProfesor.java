package com.example.lucrarelicenta2.controllere.profesor;

import com.example.lucrarelicenta2.BazaDeDate.BazaDeDate;
import com.example.lucrarelicenta2.modele.Intrebare;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ControllerTestProfesor implements Initializable {
    private final long idElement;
    private final long idCapitol;
    private final String titluElement;
    private final List<Intrebare> listaIntrebari;
    public VBox containerElement;
    public Label labelTitluElement;
    public Button butonAdaugaIntrebare;

    public ControllerTestProfesor(long idElement, long idCapitol, String titluElement, List<Intrebare> listaIntrebari) {
        this.idElement = idElement;
        this.idCapitol = idCapitol;
        this.titluElement = titluElement;
        this.listaIntrebari = listaIntrebari;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection conexiuneBD = new BazaDeDate().getConexiuneBazaDeDate();
        labelTitluElement.setText(labelTitluElement.getText() + titluElement);
        final String[] titluInitial = {labelTitluElement.getText()};
        labelTitluElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    labelTitluElement.setText("");
                    labelTitluElement.requestFocus();
                }
            }
        });
        labelTitluElement.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    labelTitluElement.getParent().requestFocus();
                    keyEvent.consume();
                    try {
                        PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"Teste\" SET \"titluElement\" = ? " +
                                "WHERE \"idCapitol\" = ? AND \"idElement\" = ?;");
                        preparedStatement.setString(1, labelTitluElement.getText());
                        preparedStatement.setLong(2, idCapitol);
                        preparedStatement.setLong(3, idElement);
                        int executeUpdate = preparedStatement.executeUpdate();
                        if (executeUpdate != 1) {
                            labelTitluElement.setText(titluInitial[0]);
                        } else {
                            titluInitial[0] = labelTitluElement.getText();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    String textDescriereCurs = labelTitluElement.getText();
                    if (!textDescriereCurs.isEmpty()) {
                        labelTitluElement.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                    }
                } else {
                    String upperCaseText = keyEvent.getText();
                    if (keyEvent.isShiftDown()) {
                        upperCaseText = upperCaseText.toUpperCase();
                    }
                    labelTitluElement.setText(labelTitluElement.getText() + upperCaseText);
                    keyEvent.consume();
                }
            }
        });
        labelTitluElement.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelTitluElement.setStyle("-fx-font-size: 36; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-text-fill: #d966ff; -fx-cursor: hand");
            }
        });
        labelTitluElement.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labelTitluElement.setStyle("-fx-font-size: 36; -fx-font-style: italic; -fx-font-weight: bold; " +
                        "-fx-text-fill: #cc33ff; -fx-cursor: hand");
            }
        });

        for (int i = 0; i < listaIntrebari.size(); i++) {
            Intrebare intrebareCurenta = listaIntrebari.get(i);
            Label labelIntrebare = new Label(i + 1 + ". " + intrebareCurenta.getTextIntrebare());
            String[] textIntrebareInitial = {labelIntrebare.getText()};
            int finalI = i;
            labelIntrebare.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
//                        labelIntrebare.setText(finalI + 1 + ". ");
                        labelIntrebare.setText(finalI + 1 + ". ");
                        labelIntrebare.requestFocus();
                    }
                }
            });
            labelIntrebare.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        labelIntrebare.getParent().requestFocus();
                        keyEvent.consume();
                        try {
                            PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"IntrebariTeste\" " +
                                    "SET \"textIntrebare\" = ? WHERE \"idIntrebare\" = ?;");
                            preparedStatement.setString(1, labelIntrebare.getText().substring(labelIntrebare.getText().indexOf(".") + 1).strip());
                            preparedStatement.setLong(2, intrebareCurenta.getIdIntrebare());
                            int executeUpdate = preparedStatement.executeUpdate();
                            if (executeUpdate != 1) {
                                labelIntrebare.setText(textIntrebareInitial[0]);
                            } else {
                                textIntrebareInitial[0] = labelIntrebare.getText();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                        String textDescriereCurs = labelIntrebare.getText();
                        if (!textDescriereCurs.isEmpty()) {
                            labelIntrebare.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                        }
                    } else {
                        String upperCaseText = keyEvent.getText();
                        if (keyEvent.isShiftDown()) {
                            upperCaseText = upperCaseText.toUpperCase();
                        }
                        labelIntrebare.setText(labelIntrebare.getText() + upperCaseText);
                        keyEvent.consume();
                    }
                }
            });
            labelIntrebare.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 16; -fx-text-fill: #cc33ff; -fx-cursor: hand");
            labelIntrebare.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    labelIntrebare.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 16; -fx-text-fill: #d966ff; -fx-cursor: hand");

                }
            });
            labelIntrebare.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    labelIntrebare.setStyle("-fx-font-weight: bold; -fx-font-style: italic; -fx-font-size: 16; -fx-text-fill: #cc33ff; -fx-cursor: hand");
                }
            });
            VBox containerIntrebare = new VBox(labelIntrebare);
            VBox.setMargin(containerIntrebare, new Insets(10.0, 15.0, 0.0, 15.0));
            containerIntrebare.setStyle("-fx-background-color: #f7e6ff; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1); " +
                    "-fx-padding: 15; -fx-pref-width: 100; -fx-font-size: 18; -fx-background-radius: 25");
            List<String> listaRaspunsuriIntrebare = intrebareCurenta.getListaRaspunsuriIntrebare();
            ToggleGroup radioToggleGroup = new ToggleGroup();
            for (String raspunsCurent : listaRaspunsuriIntrebare) {
                RadioButton radioButtonRaspuns = new RadioButton(raspunsCurent);
                radioButtonRaspuns.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        radioButtonRaspuns.setSelected(false);
                    }
                });
//                radioButtonRaspuns.setDisable(testulAMaiFostCompletat);
                radioButtonRaspuns.setToggleGroup(radioToggleGroup);
                radioButtonRaspuns.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getClickCount() == 2) {
                            radioButtonRaspuns.setText("");
                            radioButtonRaspuns.requestFocus();
                        }
                    }
                });
                radioButtonRaspuns.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        radioButtonRaspuns.setStyle("-fx-font-style: italic; -fx-cursor: hand; -fx-text-fill: #737373");
                    }
                });
                radioButtonRaspuns.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        radioButtonRaspuns.setStyle("-fx-font-style: italic; -fx-cursor: hand");
                    }
                });
                String[] raspunsInitial = {raspunsCurent};
                radioButtonRaspuns.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.ENTER) {
                            radioButtonRaspuns.getParent().requestFocus();
                            keyEvent.consume();
                            try {
                                PreparedStatement preparedStatement = conexiuneBD.prepareStatement("UPDATE \"Raspunsuri\" " +
                                        "SET \"textRaspuns\" = ? WHERE \"idIntrebare\" = ? AND \"textRaspuns\" = ?;");
                                preparedStatement.setString(1, radioButtonRaspuns.getText());
                                preparedStatement.setLong(2, intrebareCurenta.getIdIntrebare());
                                preparedStatement.setString(3, raspunsInitial[0]);
                                int executeUpdate = preparedStatement.executeUpdate();
                                if (executeUpdate != 1) {
                                    radioButtonRaspuns.setText(raspunsInitial[0]);
                                } else {
                                    raspunsInitial[0] = radioButtonRaspuns.getText();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                            String textDescriereCurs = radioButtonRaspuns.getText();
                            if (!textDescriereCurs.isEmpty()) {
                                radioButtonRaspuns.setText(textDescriereCurs.substring(0, textDescriereCurs.length() - 1));
                            }
                        } else {
                            String upperCaseText = keyEvent.getText();
                            if (keyEvent.isShiftDown()) {
                                upperCaseText = upperCaseText.toUpperCase();
                            }
                            radioButtonRaspuns.setText(radioButtonRaspuns.getText() + upperCaseText);
                            keyEvent.consume();
                        }
                    }
                });
                VBox.setMargin(radioButtonRaspuns, new Insets(5.0, 0.0, 0.0, 20.0));
                radioButtonRaspuns.setStyle("-fx-font-style: italic; -fx-cursor: hand");
                containerIntrebare.getChildren().add(radioButtonRaspuns);
            }
            containerElement.getChildren().add(containerIntrebare);
        }

        butonAdaugaIntrebare = new Button("Adauga intrebare");
        butonAdaugaIntrebare.setStyle("-fx-background-color: #f7e6ff; -fx-background-radius: 25; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0.2, 0, 1); -fx-border-color: #cc33ff; " +
                "-fx-border-radius: 25; -fx-border-width: 2; -fx-font-size: 18; -fx-font-style: italic; -fx-font-weight: bold; " +
                "-fx-text-fill: #cc33ff; -fx-padding: 5 50");
        VBox.setMargin(butonAdaugaIntrebare, new Insets(15.0, 0.0, 0.0, 0.0));
    }
}
