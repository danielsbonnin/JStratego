package stratego;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Main application of JStratego.
 *
 * @author Daniel Bonnin
 * @see javafx.application
 * @since 9.0
 */
public class Stratego extends Application {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("stratego.fxml"));
        // Setup window
        primaryStage.setTitle("JStratego");

        // TODO: make constants
        primaryStage.setMinHeight(650);
        primaryStage.setMinWidth(550);
        primaryStage.setMaxHeight(650);
        primaryStage.setMaxWidth(550);
        Scene scene = new Scene(root, 550, 650);

        // ui element representing the game board
        GridPane gb = (GridPane) scene.lookup("#gameBoard");

        // Cursor icon
        scene.getRoot().setCursor(Cursor.HAND);

        // display window and initialize ui elements for access
        primaryStage.setScene(scene);
        primaryStage.show();

        // Retrieve Button instances from hardcoded ui elements
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

        // instantiate Game object
        Game game = new Game(gb, pieceButtons, scene);
    }
}
