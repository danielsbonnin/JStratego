package stratego;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import stratego.board.BoardCoords;
import stratego.game.Game;
import stratego.game.Move;
import stratego.messages.*;
import stratego.players.IOpponent;
import stratego.players.LocalPlayer;
import stratego.players.RemotePlayer;
import stratego.ui.StrategoUI;

import java.util.ArrayList;
import java.util.List;

import static stratego.messages.MsgType.MOVE;

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
    IStrategoComms opponent;

    @FXML
    public void clientClicked() {
        this.serverAddrField.setDisable(false);
    }

    @FXML
    public void serverClicked() {
        this.serverAddrField.setDisable(true);
    }

    @FXML public void connectionTypeClicked() {
        if (this.clientRadio.isSelected()) {
            this.serverAddrField.setDisable(false);
        }
    }

    public void changeStatusText(String newText) {
        this.statusText.setText(newText);
    }
    @FXML public void connectButtonClicked() {
        if (this.serverRadio.isSelected()) {
            this.connectButton.setDisable(true);
            System.out.println("Server to start");
            this.opponent = new StrategoServer(Integer.parseInt(this.serverPortField.getText()));
            changeStatusText("Awaiting Connection");
        } else {
            this.opponent = new StrategoClient(this.serverAddrField.getText(), Integer.parseInt(this.serverPortField.getText()));
            changeStatusText("Attempting to Connect");
        }
        IStrategoComms.isConnected.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old, Boolean thenew) {
                System.out.println("isConnected Changed");
                changeStatusText(thenew ? "Connected" : "Not Connected");
            }
        });
    }

    @FXML public void isServerToggleGroupToggled() {
        if (this.clientRadio.isSelected());
    }

    @FXML
    public TextArea statusText;

    @FXML private ToggleGroup isServerToggleGroup;

    @FXML private RadioButton serverRadio;

    @FXML private RadioButton clientRadio;

    @FXML private TextField serverAddrField;

    @FXML private TextField serverPortField;

    @FXML private Button connectButton;

    @FXML private Pane serverInput;

    @FXML
    void initialize() {
        changeStatusText("Setup Connection");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        Game game = new Game(gb, pieceButtons, scene, this.opponent);

        StrategoUI ui = new StrategoUI(gb, pieceButtons, 10, 10);
    }
}
