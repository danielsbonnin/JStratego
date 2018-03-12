package stratego;

import javafx.scene.layout.Pane;
import stratego.pieces.Piece;

import static stratego.Constants.SQUARE_STYLES;
import static stratego.SquareState.*;

public class Square extends Pane {
    private Piece piece = null;
    private boolean isSelected;
    private SquareState state = WATER;
    private SquareState last = EMPTY_LAND;

    public Square(SquareState state) {
        this.setState(state);
    }
    SquareState getLast() { return this.last; }
    boolean isEmpty() { return (this.piece == null); }

    void setPiece(Piece p) { this.piece = p; }

    void remove() { this.piece = null; }

    void setSqstyle(String sqStyle) {
        this.setStyle(sqStyle);
    }

    public void setState ( SquareState state) {
        if (this.state != state) {
            this.last = this.state;
            setSqstyle(SQUARE_STYLES.get(state));
        }
        this.state = state;
    }

    public void resetState() {
        this.state = this.last;
        setSqstyle(SQUARE_STYLES.get(this.state));
    }

    public SquareState getState() { return this.state; }

    Piece getPiece() { return this.piece; }


}
