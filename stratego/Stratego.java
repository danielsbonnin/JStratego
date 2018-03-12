package stratego;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Stratego extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("stratego.fxml"));
        primaryStage.setTitle("JStratego");
        primaryStage.setMinHeight(650);
        primaryStage.setMinWidth(550);
        primaryStage.setMaxHeight(650);
        primaryStage.setMaxWidth(550);
        Scene scene = new Scene(root, 550, 650);
        GridPane gb = (GridPane) scene.lookup("#gameBoard");

        Game game = new Game(gb);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
