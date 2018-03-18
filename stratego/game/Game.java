package stratego.game;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import stratego.pieces.PieceType;
import stratego.ui.StrategoUI;
import stratego.board.*;
import stratego.pieces.Piece;
import javafx.scene.Cursor;
import stratego.players.LocalPlayer;
import stratego.players.RemotePlayer;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.*;
import static stratego.game.GameState.*;

/**
 * A Game of Stratego
 *
 * <p>Contains the board, players, pieces.</p>
 * <p>Coordinates the Setup, GamePlay phases of a game.</p>
 *
 * @author Daniel Bonnin
 * //TODO separate game logic from ui
 */
public class Game implements ChangeListener<Boolean>{
    /**
     * A 2d Array of Square objects
     * @see Square Square
     * */
    private Board board;

    /** @see javafx.scene.layout.GridPane the UI element*/
    private GridPane boardPane;

    /** @see javafx.scene */
    private Scene scene;

    /**
     * button for each piece type for setting up board
     * @see javafx.scene.control.Button
     **/
    private List<Button> pieceButtons;

    /**
     * @see LocalPlayer
     */
    private LocalPlayer p1;
    private RemotePlayer p2;
    private boolean isp1Turn;

    /**
     * PieceType currently "held" by player
     * @see stratego.pieces.Piece
     */
    private PieceType pieceInHand;


    /**
     * coordinates of picked-up piece
     */
    private BoardCoords pickedUpPiece;

    /**
     * @see GameState
     */
    private GameState state;

    /**
     * @see BoardCoords
     */
    private BoardCoords selected;

    /**
     * Represents all possible moves from the selected board square
     */
    private List<Square> curPossMoves;

    /**
     * Constructor produces a Game containing a board of default size
     * and Players with default pieces
     *
     * @param gp           UI element representing the game board
     * @param pieceButtons Button for each piece type
     * @param scene        the javafx.scene.Scene used for this game's UI
     * @see javafx.scene.Scene
     */
    public Game(GridPane gp, List<Button> pieceButtons, Scene scene) {
        this.board = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, true);
        this.boardPane = gp;
        this.pieceButtons = new ArrayList<Button>(pieceButtons);
        this.p1 = new LocalPlayer(DEFAULT_PIECES, true, this);

        this.p2 = new RemotePlayer(this);
        this.state = GameState.SETUP_NOT_PIECE_SELECTED;
        this.scene = scene;
        this.selected = new BoardCoords(0, 0);

        //this.curPossMoves = new ArrayList<Square>();
        this.isp1Turn = true;

        // initialize boardPane with Square objects from this.board
        setupGridPane();

        // initialize piece buttons according to player's inventory
        setupPieceButtons();

        // notify Game when new Move is available from UI
        StrategoUI.newClick.addListener(this);

        // GUI button to indicate finished setup
        Button finishedButton = (Button) this.scene.lookup("#btn_setup_finished");

        // finished button click handler
        finishedButton.setOnMouseClicked( e-> {
            // reference the ui elements to remove
            ButtonBar pieceButtonBar = (ButtonBar) this.scene.lookup("#pieceButtonBar");
            VBox finishedButtonVBox = (VBox) this.scene.lookup("#btn_finished_vbox");

            // the parent container of the ui
            VBox vbox = (VBox) this.scene.lookup("#vbox");

            // remove piece buttons and finished button
            vbox.getChildren().removeAll(pieceButtonBar, finishedButtonVBox);

            // initiate game play mode
            setState(MOVE_NOT_ORIGIN_SELECTED);
        });

        // initiate setup mode
        setState(SETUP_NOT_PIECE_SELECTED);
    }

    /**
     * piece picked up during setup
     */
    private void pickupClick(BoardCoords bc) {
        if (this.board.validPickup(bc)) {
            this.pieceInHand= this.board.pickupPiece(bc);
            setState(SETUP_PIECE_SELECTED);
            this.scene.getRoot().setCursor(Cursor.CROSSHAIR);
        }
    }

    /**
     * piece placed during setup
     */
    private void placementClick(BoardCoords bc) {
        if (!this.board.tryPlacePiece(bc, this.pieceInHand)) {
        }
        this.scene.getRoot().setCursor(Cursor.HAND);
        setState(SETUP_NOT_PIECE_SELECTED);
    }

    /**
     * origin click during game
     */
    private void originClick(BoardCoords bc) {
        if (this.board.validOrigin(bc)) {
            this.pickedUpPiece = bc;
            this.board.showPossibleMoves(bc);
            setState(MOVE_ORIGIN_SELECTED);
        } else {
            setState(MOVE_NOT_ORIGIN_SELECTED);
        }
    }

    /**
     * piece destination click during game
     */
    private void destClick(BoardCoords dest) {
        Move proposed = new Move(this.pickedUpPiece, dest, false, true);
        if (!this.board.move(proposed, this.isp1Turn)) {
            System.out.println("Invalid move.");
        }
        setState(MOVE_NOT_ORIGIN_SELECTED);
    }

    /**
     * Set style of piece buttons according to quantity in p1's inventory
     * @see LocalPlayer#inventory
     */
    private void updatePieceButtons() {
        // loop pieceButtons
        for (Button b : this.pieceButtons) {
            // PieceType represented by this button
            PieceType pt = PIECETYPESTRING_TO_PIECETYPE.get(b.getId());

            // Count of this button's PieceType in inventory
            int count = this.p1.getInventory().getCount(pt);

            if (count > 0) {        // at least 1 piece of this type
                // TODO: make constants of these hardcoded styles
                b.setStyle("-fx-background-color: green;");
                b.setDisable(false);
            } else {                // 0 pieces of this type
                // TODO: make constant
                b.setStyle("-fx-background-color: red;");
                b.setDisable(true); // disable this button
            }
        }
    }

    /**
     * Stub for placing p2 pieces during development
     */
    public void piecePlacementP2() {
        //
        try {
            assert (this.board.getWidth() == 10 && this.board.getHeight() == 10);
        } catch (AssertionError e) {
            System.out.println("Board dimensions should be 10X10 for piecePlacementP2 stub method.");
        }
        for (int i = 0; i < 40; i++) {
            int row = i / 10;
            int col = i % 10;
            Square sq = this.board.getSquare(row, col);
            Piece p = PIECETYPE_TO_PIECECLASS.get(DEFAULT_PIECES.get(i));
            p.setP1(false);
            sq.setPiece(p);
        }
    }
    /**
     * Setup UI for placing pieces on the board
     */
    private void setupPieceButtons() {
        updatePieceButtons();

        // setup test p2 pieces
        piecePlacementP2();
        // GUI button to indicate finished setup
        Button finishedButton = (Button) this.scene.lookup("#btn_setup_finished");

        // initialize piece buttons
        for (Button b : this.pieceButtons) {
            // User clicked on a piece button
            b.setOnMouseClicked(e-> {
                // Piece already selected. Put back old piece
                if (this.state == SETUP_PIECE_SELECTED) {
                    this.p1.getInventory().replace(this.pieceInHand);
                    this.setState(SETUP_NOT_PIECE_SELECTED);
                    updatePieceButtons();
                }
                this.pieceInHand = PIECETYPESTRING_TO_PIECETYPE.get(b.getId());
                // decrement inventory count for this type
                if (this.p1.getInventory().remove(this.pieceInHand) == 0) {
                    // last piece of this type. disable button
                    // TODO: make constant
                    b.setStyle("-fx-background-color: red;");
                    b.setDisable(true);
                }

                // set cursor to crosshair
                this.scene.getRoot().setCursor(Cursor.CROSSHAIR);
                setState(SETUP_PIECE_SELECTED);
            });
        }
    }

    /**
     * Sets grid pane.
     */
    public void setupGridPane() {
        for (int i = 0; i < this.board.getHeight(); i++)
            for (int j = 0; j < this.board.getWidth(); j++) {
                Square sqTarget = this.board.getSquare(i, j);
                boardPane.add(sqTarget, j, i);
            }
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(GameState state) {
        System.out.println("new game state: " + state.toString());
        this.state = state; }

    /**
     * Gets state.
     *
     * @return GameState state
     */
    public GameState getState() { return this.state; }

    /**
     * Gets board
     */
    public Board getBoard() { return this.board; }

    /**
     * Process coordinates of a gameBoard click
     * @param bc grid coordinates of click
     */
    private void boardClick(BoardCoords bc) {
        switch(this.state) {
            case SETUP_NOT_PIECE_SELECTED:
                pickupClick(bc);
                break;
            case SETUP_PIECE_SELECTED:
                placementClick(bc);
                break;
            case MOVE_NOT_ORIGIN_SELECTED:
                originClick(bc);
                break;
            case MOVE_ORIGIN_SELECTED:
                destClick(bc);
                break;
            default:
                throw (new IllegalStateException(this.getState().toString() + " is not a valid state."));
        }
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean newVal) {
        System.out.println("new move reported: " + StrategoUI.getCoords());
        boardClick(StrategoUI.getCoords());
    }
}
