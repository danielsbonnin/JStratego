package stratego;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import stratego.board.*;
import stratego.pieces.Piece;
import javafx.scene.Cursor;
import stratego.players.LocalPlayer;
import stratego.players.RemotePlayer;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.*;
import static stratego.GameState.*;
import static stratego.board.SquareState.*;

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
     * piece currently "held" by player while setting up board
     * @see stratego.pieces.Piece
     */
    private Piece pieceInHand;

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
     * @see Board#getMoves(int, int)
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

        this.curPossMoves = new ArrayList<Square>();
        this.isp1Turn = true;

        // initialize boardPane with Square objects from this.board
        setupGridPane();

        // initialize piece buttons according to player's inventory
        updatePieceButtons();

        // player sets pieces on the board
        piecePlacementLoop();

        // game play
        //gameLoop();
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
    public void piecePlacementLoop() {
        updatePieceButtons();

        // setup test p2 pieces
        piecePlacementP2();
        // GUI button to indicate finished setup
        Button finishedButton = (Button) this.scene.lookup("#btn_setup_finished");

        // finished button click handler
        finishedButton.setOnMouseClicked( e-> {
            System.out.println("finishedButton clicked");
            ButtonBar pieceButtonBar = (ButtonBar) this.scene.lookup("#pieceButtonBar");
            VBox finishedButtonVBox = (VBox) this.scene.lookup("#btn_finished_vbox");

            VBox vbox = (VBox) this.scene.lookup("#vbox");
            vbox.getChildren().removeAll(pieceButtonBar, finishedButtonVBox);

            // Start game loop
            gameLoop();
        });

        // restrict board squares valid for placement
        int placementRowStIdx = this.board.getHeight() - ((this.board.getHeight() / 2) - 1);

        // initialize piece buttons
        for (Button b : this.pieceButtons) {
            // User clicked on a piece button
            b.setOnMouseClicked(e-> {
                // Piece already selected. Put back old piece
                if (this.state == SETUP_PIECE_SELECTED) {
                    this.p1.getInventory().replace(this.pieceInHand.getPt());
                    this.setState(SETUP_NOT_PIECE_SELECTED);
                    updatePieceButtons();
                }

                // instantiate new Piece of selected type
                this.pieceInHand = PIECETYPE_TO_PIECECLASS.get(PIECETYPESTRING_TO_PIECETYPE.get(b.getId()));
                this.pieceInHand.setP1(this.isp1Turn);

                // decrement inventory count for this type
                if (this.p1.getInventory().remove(PIECETYPESTRING_TO_PIECETYPE.get(b.getId())) == 0) {
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

        // initialize square click handlers for placement
        for (int i = placementRowStIdx; i < this.board.getHeight(); i++) {
            for (int j = 0; j < this.board.getHeight(); j++) {
                Square sq = this.board.getSquare(i, j);

                // click handler for grid squares
                sq.setOnMouseClicked(e -> {
                    // no piece selected
                    if (this.state == SETUP_NOT_PIECE_SELECTED) {
                        // if piece here, pick it up
                        if (!sq.isEmpty() && !(sq.getState() == WATER && sq.getPiece().getP1() == isp1Turn)) {
                            this.pieceInHand = sq.remove();

                            setState(SETUP_PIECE_SELECTED);
                            this.scene.getRoot().setCursor(Cursor.CROSSHAIR);
                        }
                        return;
                    }
                    // piece is currently selected
                    else if (sq.getState() == WATER) {  // WATER square clicked
                        return;                         // do nothing
                    } else if (!sq.isEmpty()) {  // square already contains a piece
                        // opponent's piece
                        if (sq.getPiece().getP1() != isp1Turn) {
                            return;
                        }
                        // put back piece in hand. pick up piece on board
                        if (isp1Turn) {
                            this.p1.getInventory().replace(sq.getPiece().getPt());
                        } else {
                            // TODO: REMOVE the p2 stuff
                            //this.p2.inventory.replace(sq.getPiece().getPt());
                        }

                        sq.remove();

                        // Reset button in case replaced piece was last of its type
                        updatePieceButtons();
                    } else {
                        setState(SETUP_NOT_PIECE_SELECTED);
                        this.scene.getRoot().setCursor(Cursor.HAND);
                    }
                    sq.setPiece(this.pieceInHand);
                });
            }
        }
    }

    /**
     * Game play
     */
    public void gameLoop() throws IllegalStateException{
        this.setState(MOVE_NOT_ORIGIN_SELECTED);
        // remove square click handlers
        for (int i = 0; i < this.board.getHeight(); i++) {
            for (int j = 0; j < this.board.getWidth(); j++) {
               Square sq = this.board.getSquare(i, j);
                sq.setOnMouseClicked(null);
            }
        }

        // Set click handler for boardPane
        // *Square instances do not know their board coordinates
        //  so we calculate the grid relative to board bounds
        this.boardPane.setOnMousePressed(e->{
            // calculate board coordinates
            int row = ((int) (e.getY())) / 50;
            int col = ((int) (e.getX())) / 50;
            BoardCoords bc = new BoardCoords(row, col);
            Square selectedSquare = this.board.getSquare(row, col);

            if (this.state == MOVE_NOT_ORIGIN_SELECTED) {
                /** Get moves for piece at this Square
                 * @see Board#getMoves(int, int)
                 */
                // coordinates at the clicked square
                this.selected = new BoardCoords(row, col);

                // player clicked empty square
                if (selectedSquare.isEmpty()) {
                    return;
                }

                Piece selectedPiece = selectedSquare.getPiece();

                // make sure this is the current player's piece
                if (isp1Turn != selectedPiece.getP1()) return;

                // update UI to reflect curPossMoves
                this.board.showPossibleMoves(this.selected);

                setState(MOVE_ORIGIN_SELECTED);
            } else if (this.state == MOVE_ORIGIN_SELECTED) {
                if (this.board.move(selected, bc)) {  // valid move
                    setState(MOVE_NOT_ORIGIN_SELECTED);
                    isp1Turn = !isp1Turn;  // change player
                } else {                              // invalid move
                    // pass
                }
            } else {
                throw (new IllegalStateException("Invalid game state for game loop"));
            }
        });
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
    void setState(GameState state) { this.state = state; }

    /**
     * Gets state.
     *
     * @return GameState state
     */
    GameState getState() { return this.state; }

    @Override
    public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean newVal) {
        System.out.println("new move reported: " + StrategoUI.getCoords());
    }
}
