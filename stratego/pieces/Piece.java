package stratego.pieces;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import stratego.PieceType;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import static javafx.scene.text.TextAlignment.CENTER;

public abstract class Piece {
    // list common attributes of all stratego.pieces
    private int range;
    private PieceType pt;
    protected HashSet<PieceType> kills;
    private boolean revealed = false;
    private boolean p1;
    private String pLabel;

    public Piece(int range, PieceType pt, String labelText) {
        this.range = range;
        this.pt = pt;
        this.pLabel = labelText;
        setKills();
    }
    // list common methods among all stratego.pieces
    public int getRange() { return range; }
    public PieceType getPt() { return pt; }
    public HashSet<PieceType> getKills() { return kills; }
    public void reveal() {
        this.revealed = true;
    }
    public void hide() {
        this.revealed = false;
    }
    public String getLabelText() { return this.pLabel; }

    // Each piece has unique set of kills
    abstract void setKills();
}
