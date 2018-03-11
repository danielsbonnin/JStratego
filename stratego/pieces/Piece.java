package stratego.pieces;

import stratego.PieceType;

import java.util.HashSet;

public abstract class Piece {
    // list common attributes of all stratego.pieces
    private int range;
    private PieceType pt;
    protected HashSet<PieceType> kills;
    private String pLabel;
    private boolean revealed = false;
    private boolean p1;

    public Piece(int range, PieceType pt, String pLabel) {
        this.range = range;
        this.pt = pt;
        this.pLabel = pLabel;
        setKills();
    }
    // list common methods among all stratego.pieces
    public int getRange() { return range; }
    public PieceType getPt() { return pt; }
    public HashSet<PieceType> getKills() { return kills; }
    public void reveal() { this.revealed = true; }
    public void hide() { this.revealed = false;}

    // Each piece has unique set of kills
    abstract void setKills();
}
