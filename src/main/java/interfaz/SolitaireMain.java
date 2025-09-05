package interfaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SolitaireMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/bienvenida-solitario.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Juego Solitario");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);


        primaryStage.centerOnScreen();
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("interfaz/trebol.png")));
        } catch (Exception e) {
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}