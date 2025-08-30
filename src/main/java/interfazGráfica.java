import javafx.application.Application;
import javafx.stage.Stage;

public class interfazGráfica extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
      primaryStage.setTitle("Solitario");
      primaryStage.show();
    }
}
