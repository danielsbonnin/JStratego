package stratego;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import static stratego.SquareState.*;

public class Game {
    private Board board;
    private GridPane gp;
    private Player p1;
    private Player p2;
    private boolean finished = false;
    private boolean started;
    private Player currentTurn;
    private Square currentlySelected;
    private boolean originSelected;

    public Game(GridPane gp) {
        this.board = new Board(gp);
        this.gp = gp;
        this.originSelected = false;
        setupHandlers();
    }

    public void setupHandlers() {
        this.gp.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> {
            if (!this.originSelected) {
                this.currentlySelected =
                        this.board.selectSquare((int) Math.floor(e.getY() / 50), (int) Math.floor(e.getX() / 50));
            } else {
                this.currentlySelected.resetState();
            }
            this.originSelected = !this.originSelected;
        });
    }
}
