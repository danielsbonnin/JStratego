package stratego;

import stratego.pieces.Piece;

public class Square {
    private Piece piece = null;

    boolean isEmpty() { return (this.piece == null); }

    void setPiece(Piece p) { this.piece = p; }

    void remove() { this.piece = null; }

    Piece getPiece() { return this.piece; }


}
