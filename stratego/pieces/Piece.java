package stratego.pieces;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import stratego.PieceType;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import static javafx.scene.text.TextAlignment.CENTER;

/**
 * The type Piece.
 */
public abstract class Piece {
    // list common attributes of all stratego.pieces
    private int range;
    private PieceType pt;
    /**
     * The Kills.
     */
    protected HashSet<PieceType> kills;
    private boolean revealed = false;
    private boolean p1;
    private String pLabel;

    /**
     * Instantiates a new Piece.
     *
     * @param range     the range
     * @param pt        the pt
     * @param labelText the label text
     */
    public Piece(int range, PieceType pt, String labelText) {
        this.range = range;
        this.pt = pt;
        this.pLabel = labelText;
        setKills();
    }

    /**
     * Gets range.
     *
     * @return the range
     */
// list common methods among all stratego.pieces
    public int getRange() { return range; }

    /**
     * Gets pt.
     *
     * @return the pt
     */
    public PieceType getPt() { return pt; }

    /**
     * Gets kills.
     *
     * @return the kills
     */
    public HashSet<PieceType> getKills() { return kills; }

    /**
     * Reveal.
     */
    public void reveal() {
        this.revealed = true;
    }

    /**
     * Hide.
     */
    public void hide() {
        this.revealed = false;
    }

    /**
     * Gets label text.
     *
     * @return the label text
     */
    public String getLabelText() { return this.pLabel; }

    /**
     * Sets kills.
     */
// Each piece has unique set of kills
    abstract void setKills();
}
