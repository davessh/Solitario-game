package interfaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SolitaireMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/solitaire/bienvenida-solitario.fxml"));
        Parent root = loader.load();

        // Configurar la escena
        Scene scene = new Scene(root);
        primaryStage.setTitle("Solitaire - Bienvenida");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Opcional: si no quieres que sea redimensionable
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}