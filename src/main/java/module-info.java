module com.example.interfazjuegosolitario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    exports interfaz;
    opens solitaire to javafx.fxml;
    opens com.example.interfazjuegosolitario to javafx.fxml;
    exports com.example.interfazjuegosolitario;
    exports solitaire;
    opens interfaz to javafx.graphics;
}