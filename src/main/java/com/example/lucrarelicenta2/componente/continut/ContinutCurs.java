package com.example.lucrarelicenta2.componente.continut;

import com.example.lucrarelicenta2.componente.cursuri.Curs;
import com.example.lucrarelicenta2.controllere.ControllerContinutCurs;
import com.example.lucrarelicenta2.modele.Utilizator;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class ContinutCurs {
  private final static Logger LOGGER = Logger.getLogger (ContinutCurs.class.getName ());
  private final Curs elementCurs;
  private ScrollPane containerCurs;

  public ContinutCurs (Curs elementCurs, Stage radacinaAplicatiei, Utilizator utilizatorAutentificat)
  {
    this.elementCurs = elementCurs;
    FXMLLoader fxmlLoader = new FXMLLoader (getClass ().getResource ("/curs-view.fxml"));
    try {
      fxmlLoader.setControllerFactory (aClass -> new ControllerContinutCurs (this.elementCurs, radacinaAplicatiei, utilizatorAutentificat));
      containerCurs = fxmlLoader.load ();
      VBox.setVgrow (containerCurs, Priority.ALWAYS);
      containerCurs.setMaxWidth (Region.USE_COMPUTED_SIZE);
      containerCurs.setMaxHeight (Region.USE_COMPUTED_SIZE);
    } catch (IOException e) {
      LOGGER.severe (e.getCause ().getMessage ());
    }
  }

  public ScrollPane getContainerCurs ()
  {
    return containerCurs;
  }
}
