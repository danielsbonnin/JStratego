package stratego;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import stratego.game.Game;
import stratego.messages.StrategoClient;
import stratego.messages.StrategoServer;
import stratego.players.IOpponent;
import stratego.players.LocalPlayer;
import stratego.players.RemotePlayer;
import stratego.ui.StrategoUI;

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

    LocalPlayer p1;
    RemotePlayer p2;
    StrategoServer ss;
    StrategoClient sc;
    @FXML
    public void clientClicked() {
        System.out.println("client clicked");
        this.serverAddrField.setDisable(false);
    }

    @FXML
    public void serverClicked() {
        System.out.println("server clicked");
        this.serverAddrField.setDisable(true);
    }

    @FXML public void connectionTypeClicked() {
        if (this.clientRadio.isSelected()) {
            this.serverAddrField.setDisable(false);
        }
    }

    @FXML public void connectButtonClicked() {
        if (this.serverRadio.isSelected()) {
            this.connectButton.setDisable(true);
            ss = new StrategoServer(Integer.parseInt(this.serverPortField.getText()));
        }
        else
            sc = new StrategoClient(this.serverAddrField.getText(), Integer.parseInt(this.serverPortField.getText()));
    }

    @FXML public void isServerToggleGroupToggled() {
        if (this.clientRadio.isSelected());
    }

    @FXML
    private TextArea statusText;

    @FXML private ToggleGroup isServerToggleGroup;

    @FXML private RadioButton serverRadio;

    @FXML private RadioButton clientRadio;

    @FXML private TextField serverAddrField;

    @FXML private TextField serverPortField;

    @FXML private Button connectButton;

    @FXML private Pane serverInput;
    @FXML
    void initialize() {
        this.statusText.setText("Setup Connection");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("stratego.fxml"));
        // Setup window

        primaryStage.setTitle("JStratego");
        Scene scene = new Scene(root, 700, 650);

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
        //Game game = new Game(gb, pieceButtons, scene);

        StrategoUI ui = new StrategoUI(gb, pieceButtons, 10, 10);
    }
}
