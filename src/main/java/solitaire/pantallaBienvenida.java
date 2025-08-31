package solitaire;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class pantallaBienvenida extends Application {

    @Override
    public void start(Stage stage) {

        // Botón jugar
        Button btnJugar = new Button("Jugar");
        btnJugar.setFont(Font.font("Arial", 65));
        btnJugar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 10;");

        // Acción del botón (ejemplo)
        btnJugar.setOnAction(e -> {
            System.out.println("Inicia el juego...");
            // futura implementación del juego
        });


        VBox vbox = new VBox(20, btnJugar);
        vbox.setAlignment(Pos.CENTER);


        BackgroundImage fondo = new BackgroundImage(
                new Image("C:\\Users\\GF76\\IdeaProjects\\interfazJuegoSolitario\\src\\main\\java\\fondoBienvenida.png", 1920, 1080, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        vbox.setBackground(new Background(fondo));

        Scene scene = new Scene(vbox);

        stage.setTitle("Solitario - Bienvenida");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
