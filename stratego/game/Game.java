package stratego.game;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import stratego.Stratego;
import stratego.messages.IStrategoComms;
import stratego.messages.Message;
import stratego.pieces.PieceType;
import stratego.ui.StrategoUI;
import stratego.board.*;
import stratego.pieces.Piece;
import javafx.scene.Cursor;
import stratego.players.LocalPlayer;
import stratego.players.RemotePlayer;

import java.beans.EventHandler;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.*;
import static stratego.game.GameState.*;
import static stratego.messages.IStrategoComms.hasIncoming;
import static stratego.messages.MsgType.MOVE;
import static stratego.messages.MsgType.SETUP_COMPLETE;
import static stratego.pieces.PieceType.FLAG;

/**
 * A Game of Stratego
 *
 * <p>Contains the board, players, pieces.</p>
 * <p>Coordinates the GamePlay phase of a Stratego game.</p>
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
    private IStrategoComms comms;
    private boolean isp1Turn;
    private TextArea statusText;

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
    public static final ObjectProperty<GameState> state = new SimpleObjectProperty<>(SETUP_NOT_PIECE_SELECTED);

    /**
     * @see BoardCoords
     */
    private BoardCoords selected;

    /**
     * Represents all possible moves from the selected board square
     */
    private List<Square> curPossMoves;

    private Button finishedButton;

    /**
     * Lister to notify on message from opponent
     */
    private ChangeListener<Boolean> commsListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue <? extends Boolean> obs, Boolean old, Boolean newValue) {
            if (obs.getValue()) {
                Message msg = comms.getIncomingMessage();
                switch (msg.getType()) {
                    case MOVE:
                        Platform.runLater(new Runnable() {
                            public void run() {
                                p2Move((Move)msg.getObj());
                            }
                        });
                        break;
                    case SETUP_COMPLETE:
                        Platform.runLater(new Runnable() {
                            public void run() {
                                piecePlacementP2((BoardData) msg.getObj());
                                if (getState() == AWAIT_P2_MOVE)
                                    setState(MOVE_NOT_ORIGIN_SELECTED);
                            }
                        });
                        break;
                    default:
                        break;
                }
                IStrategoComms.hasIncoming.set(false);
            }
        }
    };

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
        this.pieceButtons = pieceButtons;
        this.scene = scene;
        this.selected = new BoardCoords(0, 0);
        this.p1 = new LocalPlayer();
        this.p2 = new RemotePlayer(this);
        this.isp1Turn = true;
        // initialize boardPane with Square objects from this.board
        setupGridPane();

    }

    public void start(IStrategoComms comms, TextArea statusText, Button finishedButton) {
        this.comms = comms;
        this.statusText = statusText;

        // GUI button to indicate finished setup
        this.finishedButton = finishedButton;
        this.finishedButton.setDisable(true);
        setState(SETUP_NOT_PIECE_SELECTED);
        // notify commsListener when opponent has new message
        IStrategoComms.hasIncoming.addListener(this.commsListener);

        // initialize piece buttons according to player's inventory
        setupPieceButtons();

        // notify Game when new Move is available from UI
        StrategoUI.newClick.addListener(this);

        // finished button click handler
        this.finishedButton.setOnMouseClicked( e-> {
            // reference the ui elements to remove
            ButtonBar pieceButtonBar = (ButtonBar) this.scene.lookup("#pieceButtonBar");
            VBox finishedButtonVBox = (VBox) this.scene.lookup("#btn_finished_vbox");

            // the parent container of the ui
            VBox vbox = (VBox) this.scene.lookup("#vbox");

            // remove piece buttons and finished button
            vbox.getChildren().removeAll(pieceButtonBar, finishedButtonVBox);
            // send a message to remote player
            BoardData toSend = this.board.toBoardData();
            toSend.reverse();
            // send finished message to opponent
            Message finishedMessage = new Message(SETUP_COMPLETE, toSend.toJsonString());
            comms.sendMessage(finishedMessage);

            // initiate game play mode
            setState(AWAIT_P2_MOVE);
        });

        // initiate setup mode
        setState(SETUP_NOT_PIECE_SELECTED);
    }

    /**
     * piece picked up during setup
     */
    private void pickupClick(BoardCoords bc) {
        if (this.board.validPickup(bc)) {
            this.p1.incrementPiecesLeftInInventory();
            this.pieceInHand= this.board.pickupPiece(bc);
            setState(SETUP_PIECE_SELECTED);
            this.scene.getRoot().setCursor(Cursor.CROSSHAIR);
        }
        if (this.p1.getPiecesLeftInInventory() == 0)
            this.finishedButton.setDisable(false);
        else
            this.finishedButton.setDisable(true);
    }

    /**
     * piece placed during setup
     */
    private void placementClick(BoardCoords bc) {
        if (!this.board.tryPlacePiece(bc, this.pieceInHand)) {
            this.p1.getInventory().replace(this.pieceInHand);
            updatePieceButtons();
        } else
            this.p1.decrementPiecesLeftInInventory();
        this.scene.getRoot().setCursor(Cursor.HAND);
        setState(SETUP_NOT_PIECE_SELECTED);
        if (this.p1.getPiecesLeftInInventory() == 0) {
            this.finishedButton.setDisable(false);
        } else {
            this.finishedButton.setDisable(true);
        }
    }

    /**
     * origin click during game
     */
    private void originClick(BoardCoords bc) {
        System.out.println("origin click: " + bc.toString());
        if (this.board.validOrigin(bc)) {
            System.out.println("valid origin");
            this.pickedUpPiece = bc;
            this.board.showPossibleMoves(bc);
            setState(MOVE_ORIGIN_SELECTED);
        } else {
            System.out.println("invalid origin");
            System.out.println(this.board.toBoardData().toJsonString());
            setState(MOVE_NOT_ORIGIN_SELECTED);
        }
    }

    /**
     * piece destination click during game
     */
    private void destClick(BoardCoords dest) {
        Square destSquare = this.board.getSquare(dest.r, dest.c);
        boolean victory = false;
        if (!destSquare.isEmpty() && destSquare.getPiece().getPt() == FLAG)
            victory = true;
        Move proposed = new Move(this.pickedUpPiece, dest, victory, true);
        if (!this.board.move(proposed, true)) {
            System.out.println("Invalid move.");
            setState(MOVE_NOT_ORIGIN_SELECTED);
        } else {
            comms.sendMessage(new Message(MOVE, proposed.reversed().toJson()));
            if (victory)
                setState(GAME_WON);
            else
                setState(AWAIT_P2_MOVE);
        }
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
            if (this.p1.getPiecesLeftInInventory() == 0) {
                this.finishedButton.setDisable(false);
            } else {
                this.finishedButton.setDisable(true);
            }
        }
    }

    /**
     * Stub for placing p2 pieces during development
     */
    public void piecePlacementP2(BoardData p2Placement) {
        System.out.println("Before getting p2 placement");
        System.out.println(this.board.toBoardData().toJsonString());

        try {
            assert (this.board.getWidth() == 10 && this.board.getHeight() == 10);
        } catch (AssertionError e) {
            System.out.println("Board dimensions should be 10X10 for piecePlacementP2 stub method.");
        }
        PlayerPiece pp;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 10; j++){
                Square sq = this.board.getSquare(i, j);
                BoardCoords pos = new BoardCoords(i, j);
                Piece p;
                pp = p2Placement.get(pos);
                if (pp.getPt() != null) {
                    Class c = PIECETYPE_TO_PIECECLASS.get(pp.getPt());
                    try {
                        p = (Piece)c.getDeclaredConstructor().newInstance();
                        p.setP1(false);
                        sq.setPiece(p);
                    } catch (Exception e) {e.printStackTrace();}
                    System.out.println("playerpiece at " + pos.toString() + ": " + pp.toString());
                }
            }
        System.out.println("After getting p2 placement");
        System.out.println(this.board.toBoardData().toJsonString());
    }
    /**
     * Setup UI for placing pieces on the board
     */
    private void setupPieceButtons() {
        System.out.println("setup piece buttons");
        updatePieceButtons();

        // initialize piece buttons
        for (Button b : this.pieceButtons) {
            // User clicked on a piece button
            b.setOnMouseClicked(e-> {
                // Piece already selected. Put back old piece
                if (getState() == SETUP_PIECE_SELECTED) {
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
                System.out.println("pieces left in inventory: " + this.p1.getPiecesLeftInInventory());
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
     * Sets UI status text
     */
    private void setStatusText(String status) {
        this.statusText.setText(status);
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(GameState state) {
        System.out.println("new game state: " + state.toString());
        switch (state) {
            case SETUP_NOT_PIECE_SELECTED:
                setStatusText("Select a piece");
                break;
            case SETUP_PIECE_SELECTED:
                setStatusText("Place piece");
                break;
            case MOVE_NOT_ORIGIN_SELECTED:
                setStatusText("Select a piece");
                break;
            case MOVE_ORIGIN_SELECTED:
                setStatusText("Select destination");
                break;
            case AWAIT_P2_MOVE:
                setStatusText("Waiting for opponent to move");
                break;
            case GAME_LOST:
                setStatusText("You lost.");
                break;
            case GAME_WON:
                setStatusText("You won!");
            default:
                break;
        }
        Game.state.setValue(state);
    }

    /**
     * Gets state.
     *
     * @return GameState state
     */
    public GameState getState() {
        return Game.state.getValue();
    }

    /**
     * Gets board
     */
    public Board getBoard() { return this.board; }

    private void p2Move(Move move) {
        this.board.move(move, false);
        if (move.isFinal())
            setState(GAME_LOST);
        else
            setState(MOVE_NOT_ORIGIN_SELECTED);
    }

    /**
     * Process coordinates of a gameBoard click
     * @param bc grid coordinates of click
     */
    private void boardClick(BoardCoords bc) {
        switch(Game.state.getValue()) {
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
            case AWAIT_P2_MOVE:
                System.out.println("Waiting for opponent to move");
                break;
            default:
                throw (new IllegalStateException(this.getState().toString() + " is not a valid state."));
        }
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean newVal) {
        boardClick(StrategoUI.getCoords());
    }
}
