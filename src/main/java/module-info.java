module com.example.lucrarelicenta {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    exports com.example.lucrarelicenta2.modele;

    opens com.example.lucrarelicenta2 to javafx.fxml;
    exports com.example.lucrarelicenta2;
    exports com.example.lucrarelicenta2.ferestre;
    opens com.example.lucrarelicenta2.ferestre to javafx.fxml;
    exports com.example.lucrarelicenta2.controllere;
    opens com.example.lucrarelicenta2.controllere to javafx.fxml;
    exports com.example.lucrarelicenta2.componente.continut;
    opens com.example.lucrarelicenta2.componente.continut to javafx.fxml;
    exports com.example.lucrarelicenta2.componente.cursuri;
    opens com.example.lucrarelicenta2.componente.cursuri to javafx.fxml;
    exports com.example.lucrarelicenta2.componente.continut.containere;
    opens com.example.lucrarelicenta2.componente.continut.containere to javafx.fxml;
    exports com.example.lucrarelicenta2.componente.continut.profesor;
    opens com.example.lucrarelicenta2.componente.continut.profesor to javafx.fxml;
    exports com.example.lucrarelicenta2.controllere.profesor;
    opens com.example.lucrarelicenta2.controllere.profesor to javafx.fxml;
    exports com.example.lucrarelicenta2.elemente.profesor;
    exports com.example.lucrarelicenta2.elemente;
}