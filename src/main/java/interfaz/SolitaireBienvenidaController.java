package interfaz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SolitaireBienvenidaController {

    @FXML
    private Button btnJugar;

    @FXML
    private Button btnSalir;

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleJugarAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/tableroSolitario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnJugar.getScene().getWindow();
            stage.setScene(new Scene(root));
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