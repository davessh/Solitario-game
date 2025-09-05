package interfaz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class SolitaireBienvenidaController {

    @FXML
    private Button btnJugar;

    @FXML
    private Button btnSalir;

    @FXML
    private void initialize() {
        // Agregar efectos hover a los botones
        setupButtonHoverEffects();
    }

    private void setupButtonHoverEffects() {
        // Efecto hover para botón Jugar
        btnJugar.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), btnJugar);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
            btnJugar.setStyle(btnJugar.getStyle() +
                    "-fx-background-color: linear-gradient(to bottom, #5CBF60, #4CAF50);");
        });

        btnJugar.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), btnJugar);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            btnJugar.setStyle(btnJugar.getStyle().replace(
                    "-fx-background-color: linear-gradient(to bottom, #5CBF60, #4CAF50);",
                    "-fx-background-color: linear-gradient(to bottom, #4CAF50, #45a049);"));
        });

        // Efecto hover para botón Salir
        btnSalir.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), btnSalir);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
            btnSalir.setStyle(btnSalir.getStyle() +
                    "-fx-background-color: linear-gradient(to bottom, #f55a4e, #f44336);");
        });

        btnSalir.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), btnSalir);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            btnSalir.setStyle(btnSalir.getStyle().replace(
                    "-fx-background-color: linear-gradient(to bottom, #f55a4e, #f44336);",
                    "-fx-background-color: linear-gradient(to bottom, #f44336, #d32f2f);"));
        });
    }

    @FXML
    private void handleJugarAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/tableroSolitario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnJugar.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Solitaire Klondike");

            // Hacer que la ventana sea redimensionable y maximizada
            stage.setResizable(true);
            stage.setMaximized(true);

            // Establecer tamaños mínimos
            stage.setMinWidth(1000);
            stage.setMinHeight(700);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalirAction() {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }
}