package stratego;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stratego.pieces.Piece;

import static javafx.scene.text.TextAlignment.CENTER;
import static stratego.Constants.SQUARE_STYLES;
import static stratego.SquareState.*;

public class Square extends Pane {
    private Piece piece = null;
    private boolean isEmpty = true;
    private boolean isSelected;
    private SquareState state = WATER;
    private SquareState last = EMPTY_LAND;
    private Label labelText;

    public Square(SquareState state) {
        this.setState(state);
        this.labelText = new Label("");
        this.getChildren().add(this.labelText);

        // TODO: use constant cell size to center labelText
        this.labelText.setLayoutX(12);
        this.labelText.setLayoutY(12);
    }
    SquareState getLast() { return this.last; }
    boolean isEmpty() { return this.isEmpty; }

    void setPiece(Piece p) {
        this.piece = p;
        p.reveal();
        this.isEmpty = false;
        this.labelText.setText(p.getLabelText());
    }

    Piece remove() {
        Piece p = this.piece;
        this.piece = null;
        this.isEmpty = true;
        //TODO make constant
        this.labelText.setText("");
        return p;
    }

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
    Piece getPiece() {
        return this.piece;
    }
}
