package stratego.board;

import stratego.pieces.Piece;
import stratego.pieces.PieceType;

/**
 * Stratego PieceType and whether owner is LocalPlayer 1
 * @author Daniel Bonnin
 */
public class PlayerPiece {
    private PieceType pt;
    private boolean isP1;
    public PlayerPiece(PieceType pt, boolean isP1) {
        this.pt = pt;
        this.isP1 = isP1;
    }

    /**
     * Construct PlayerPiece from Piece members
     * @param p a Piece
     */
    public PlayerPiece(Piece p) {
        this.isP1 = p.getP1();
        this.pt = p.getPt();
    }

    public void setPt(PieceType pt) { this.pt = pt; }
    public PieceType getPt() { return this.pt; }

    public void setIsP1(boolean isP1) { this.isP1 = isP1; }
    public boolean getIsP1() { return this.isP1; }
    public String toString() {
        return "P1: " + this.isP1 + " pt: " + this.pt.toString();
    }
}
