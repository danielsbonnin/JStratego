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

import java.util.ArrayList;
import java.util.List;

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

        primaryStage.setScene(scene);
        primaryStage.show();

        // Setup piece buttons
        List<Button> pieceButtons = new ArrayList<Button>();
        pieceButtons.add((Button)scene.lookup("#FLAG"));
        pieceButtons.add((Button)scene.lookup("#BOMB"));
        pieceButtons.add((Button)scene.lookup("#SPY"));
        pieceButtons.add((Button)scene.lookup("#SCOUT"));
        pieceButtons.add((Button)scene.lookup("#MINER"));
        pieceButtons.add((Button)scene.lookup("#SERGEANT"));
        pieceButtons.add((Button)scene.lookup("#LIEUTENANT"));
        pieceButtons.add((Button)scene.lookup("#CAPTAIN"));
        pieceButtons.add((Button)scene.lookup("#MAJOR"));
        pieceButtons.add((Button)scene.lookup("#COLONEL"));
        pieceButtons.add((Button)scene.lookup("#GENERAL"));
        pieceButtons.add((Button)scene.lookup("#MARSHALL"));
        Game game = new Game(gb, pieceButtons);
    }
}
