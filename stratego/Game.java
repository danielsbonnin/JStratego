package stratego;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import stratego.pieces.Flag;
import stratego.GameState.*;
import stratego.pieces.Piece;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.DEFAULT_PIECES;
import static stratego.Constants.PIECETYPESTRING_TO_PIECETYPE;
import static stratego.Constants.PIECETYPE_TO_PIECECLASS;
import static stratego.GameState.SETUP_NOT_PIECE_SELECTED;
import static stratego.GameState.SETUP_PIECE_SELECTED;
import static stratego.SquareState.*;

public class Game {
    private Board board;
    private GridPane boardPane;
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

    public Game(GridPane gp, List<Button> pieceButtons) {
        this.board = new Board();
        this.boardPane = gp;
        this.pieceButtons = new ArrayList<Button>(pieceButtons);
        this.originSelected = false;
        this.p1 = new Player(DEFAULT_PIECES);
        this.state = GameState.SETUP_NOT_PIECE_SELECTED;

        setupGridPane();
        setupPieceButtons();
        setupHandlers();
    }

    public void setupGridPane() {
        for (int i = 0; i < this.board.height; i++)
            for (int j = 0; j < this.board.width; j++) {
                Square target = this.board.getSquare(i, j);

                // Handler for initial placement of pieces
                target.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
                    if (this.state == SETUP_NOT_PIECE_SELECTED)
                        e.consume();
                    else if (!target.isEmpty()) {
                        this.p1.inventory.replace(this.pieceInHand.getPt());
                        System.out.println(this.pieceInHand.getLabelText() + " should be replaced.");
                        // Reset button in case this was the last piece of its type
                        setupPieceButtons();
                    } else {
                        target.setPiece(this.pieceInHand);
                    }
                    setState(SETUP_NOT_PIECE_SELECTED);
                });
                boardPane.add(target, j, i);
            }
    }

    private void setupPieceButtons() {
        for (Button b : this.pieceButtons) {
            PieceType pt = PIECETYPESTRING_TO_PIECETYPE.get(b.getId());
            int count = this.p1.inventory.getCount(pt);
            if (count > 0) {
                // TODO: make constants of these hardcoded styles
                b.setStyle("-fx-background-color: green;");
                b.setDisable(false);
            } else {
                // TODO: make constant
                b.setStyle("-fx-background-color: red;");
                b.setDisable(true);
            }

            b.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
                if (this.state == SETUP_PIECE_SELECTED) {
                    this.p1.inventory.replace(this.pieceInHand.getPt());
                    setupPieceButtons();
                }
                this.pieceInHand = PIECETYPE_TO_PIECECLASS.get(PIECETYPESTRING_TO_PIECETYPE.get(b.getId()));
                 if (this.p1.inventory.remove(PIECETYPESTRING_TO_PIECETYPE.get(b.getId())) == 0) {
                     // TODO: make constant
                     b.setStyle("-fx-background-color: red;");
                     b.setDisable(true);
                 }
                 setState(SETUP_PIECE_SELECTED);
            });
        }
    }

    public void setupHandlers() {
        System.out.println("setupHandlers(). state: " + this.state);
        switch (this.state) {
            case SETUP_PIECE_SELECTED:
                break;
            case SETUP_NOT_PIECE_SELECTED:
                break;
            case MOVE_ORIGIN_SELECTED:
                break;
            case MOVE_NOT_ORIGIN_SELECTED:
                this.boardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    if (!this.originSelected) {
                        this.currentlySelected =
                                this.board.selectSquare((int) Math.floor(e.getY() / 50), (int) Math.floor(e.getX() / 50));
                    } else {
                        this.currentlySelected.resetState();
                    }
                    this.originSelected = !this.originSelected;
                });
                break;
        }
    }

    void setState(GameState state) { this.state = state; }
}
