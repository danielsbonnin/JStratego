package stratego;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import stratego.pieces.Piece;

import static stratego.Constants.SQUARE_STYLES;
import static stratego.SquareState.EMPTY_LAND;
import static stratego.SquareState.POSSIBLE_MOVE;

/**
 * The type Square.
 */
public class Square extends Pane {
    private Piece piece = null;
    private boolean isEmpty = true;
    private boolean isSelected;
    private SquareState state;

    /**
     * The most recent state
     */
    private SquareState last;

    /**
     * Whether the square is highlighted.
     */
    private boolean isHighlighted;
    private Label labelText;

    /**
     * Instantiates a new Square.
     *
     * @param state the state
     */
    public Square(SquareState state) {
        this.last = state;
        this.setState(state);
        this.labelText = new Label("");
        this.getChildren().add(this.labelText);
        this.isHighlighted = false;

        // TODO: use constant cell size to center labelText
        this.labelText.setLayoutX(12);
        this.labelText.setLayoutY(12);
    }

    /**
     * Gets last.
     *
     * @return the last
     */
    SquareState getLast() { return this.last; }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    boolean isEmpty() { return this.isEmpty; }

    boolean getIsHighlighted() { return isHighlighted; }
    void highlight() {
        if (!this.isHighlighted)
            this.isHighlighted = true;
            this.last = this.state;
            setState(POSSIBLE_MOVE);
    }
    void unHighlight() {
        if (this.isHighlighted) {
            this.isHighlighted = false;
            this.setState(this.last);
        }
    }
    /**
     * Sets piece.
     *
     * @param p the p
     */
    void setPiece(Piece p) {
        this.piece = p;
        p.reveal();
        this.isEmpty = false;
        this.labelText.setText(p.getLabelText());
    }

    /**
     * Remove piece.
     *
     * @return the piece
     */
    Piece remove() {
        Piece p = this.piece;
        this.piece = null;
        this.isEmpty = true;
        setState(EMPTY_LAND);
        //TODO make constant
        this.labelText.setText("");
        return p;
    }

    /**
     * Sets sqstyle.
     *
     * @param sqStyle the sq style
     */
    void setSqstyle(String sqStyle) {
        this.setStyle(sqStyle);
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState ( SquareState state) {
        this.state = state;
        this.setSqstyle(SQUARE_STYLES.get(state));
    }

    /**
     * Reset state.
     */
    public void resetState() {
        this.state = this.last;
        setSqstyle(SQUARE_STYLES.get(this.state));
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public SquareState getState() { return this.state; }

    /**
     * Gets piece.
     *
     * @return the piece
     */
    Piece getPiece() {
        return this.piece;
    }
}
