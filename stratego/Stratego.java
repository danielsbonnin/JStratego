package stratego;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Stratego extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("stratego.fxml"));
        primaryStage.setTitle("JStratego");
        Scene scene = new Scene(root, 600, 800);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
