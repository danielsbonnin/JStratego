package stratego;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import stratego.pieces.Piece;
import javafx.scene.Cursor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.*;
import static stratego.GameState.*;
import static stratego.SquareState.*;

public class Game {
    private Board board;
    private GridPane boardPane;
    private Scene scene;
    private List<Button> pieceButtons;
    private Player p1;
    private Player p2;
    private boolean finished = false;
    private boolean started = false;
    private Player currentTurn;
    private Piece pieceInHand;
    private Square currentlySelected;
    private boolean originSelected;
    private GameState state;
    private BoardCoords selected;
    private List<Square> curPossMoves;

    public Game(GridPane gp, List<Button> pieceButtons, Scene scene) {
        this.board = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, true);
        this.boardPane = gp;
        this.pieceButtons = new ArrayList<Button>(pieceButtons);
        this.originSelected = false;
        this.p1 = new Player(DEFAULT_PIECES);
        this.p2 = new Player(DEFAULT_PIECES);
        this.state = GameState.SETUP_NOT_PIECE_SELECTED;
        this.scene = scene;
        this.selected = new BoardCoords(0, 0);
        this.curPossMoves = new ArrayList<Square>();

        setupGridPane();
        setupPieceButtons();
    }

    private void updatePieceButtons() {
        for (Button b : this.pieceButtons) {
            // Type of piece represented by this button
            PieceType pt = PIECETYPESTRING_TO_PIECETYPE.get(b.getId());

            // Count of this button's piece type in inventory
            int count = this.p1.inventory.getCount(pt);

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
     * Setup UI for placing pieces on the board
     */
    public void piecePlacementLoop() {
        updatePieceButtons();

        // GUI button to indicate finished setup
        Button finishedButton = (Button) this.scene.lookup("#btn_setup_finished");

        // finished button click handler
        finishedButton.setOnMouseClicked( e-> {
            ButtonBar pieceButtonBar = (ButtonBar) this.scene.lookup("#pieceButtonBar");
            VBox finishedButtonVBox = (VBox) this.scene.lookup("#btn_finished_vbox");

            VBox vbox = (VBox) this.scene.lookup("#vbox");
            vbox.getChildren().removeAll(pieceButtonBar, finishedButtonVBox);

            // Start game loop
            gameLoop();
        });

        // restrict board squares valid for placement
        int placementRowStIdx = this.board.height - ((this.board.height / 2) - 1);

        // initialize piece buttons
        for (Button b : this.pieceButtons) {
            // User clicked on a piece button
            b.setOnMouseClicked(e-> {
                // Piece already selected. Put back old piece
                if (this.state == SETUP_PIECE_SELECTED) {
                    this.p1.inventory.replace(this.pieceInHand.getPt());
                    this.setState(SETUP_NOT_PIECE_SELECTED);
                    updatePieceButtons();
                }

                // instantiate new Piece of selected type
                this.pieceInHand = PIECETYPE_TO_PIECECLASS.get(PIECETYPESTRING_TO_PIECETYPE.get(b.getId()));

                // decrement inventory count for this type
                if (this.p1.inventory.remove(PIECETYPESTRING_TO_PIECETYPE.get(b.getId())) == 0) {
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
        for (int i = placementRowStIdx; i < this.board.height; i++) {
            for (int j = 0; j < this.board.width; j++) {
                Square sq = this.board.getSquare(i, j);

                // click handler
                sq.setOnMouseClicked(e -> {
                    // No piece selected; ignore
                    if (this.state == SETUP_NOT_PIECE_SELECTED) {
                        if (!sq.isEmpty() && !(sq.getState() == WATER)) {
                            this.pieceInHand = sq.remove();
                            setState(SETUP_PIECE_SELECTED);
                            this.scene.getRoot().setCursor(Cursor.CROSSHAIR);
                        }
                        return;
                    }
                    else if (sq.getState() == WATER) {
                        return;
                    } else if (!sq.isEmpty()) {   // square already contains a piece
                        this.p1.inventory.replace(sq.getPiece().getPt());
                        sq.remove();
                        // Reset button in case replaced piece was last of its type
                        updatePieceButtons();
                    } else {}
                    sq.setPiece(this.pieceInHand);

                    setState(SETUP_NOT_PIECE_SELECTED);
                    this.scene.getRoot().setCursor(Cursor.HAND);
                });
            }
        }
    }

    public void showPossMoves() {
        for (Square p : this.curPossMoves) {
            p.setState(POSSIBLE_MOVE);
        }
    }

    /**
     * Game play
     */
    public void gameLoop() {
        System.out.println("Entering Game Loop");
        this.setState(MOVE_NOT_ORIGIN_SELECTED);

        // remove square click handlers
        for (int i = 0; i < this.board.height; i++) {
            for (int j = 0; j < this.board.width; j++) {
               Square sq = this.board.getSquare(i, j);
                sq.setOnMouseClicked(null);
            }
        }
        boardPane.setOnMousePressed(e->{
            int row = ((int) (e.getY())) / 50;
            int col = ((int) (e.getX())) / 50;
            if (this.state == MOVE_NOT_ORIGIN_SELECTED) {
                this.curPossMoves.addAll(this.board.getMoves(row, col));
                if (this.curPossMoves.isEmpty())
                    return;
                showPossMoves();
                setState(MOVE_ORIGIN_SELECTED);
                this.selected = new BoardCoords(row, col);
            } else if (this.state == MOVE_ORIGIN_SELECTED) {
                if (this.board.move(selected.r, selected.c, row, col)) {
                    System.out.println("Move chosen: (" + selected.c + ", " + selected.r + ") to " +
                                        "(" + col + ", " + row + ")");
                    setState(MOVE_NOT_ORIGIN_SELECTED);
                    while (!this.curPossMoves.isEmpty())
                        this.curPossMoves.remove(0).resetState();
                }
            }
        });
    }
    public void setupGridPane() {
        for (int i = 0; i < this.board.height; i++)
            for (int j = 0; j < this.board.width; j++) {
                Square sqTarget = this.board.getSquare(i, j);
                boardPane.add(sqTarget, j, i);
            }
    }

    private void setupPieceButtons() {
    }

    void setState(GameState state) { this.state = state; }
}
