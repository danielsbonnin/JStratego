package stratego.pieces;

import static stratego.pieces.PieceType.*;

import java.util.HashSet;

/**
 * The type Flag.
 */
public class Flag extends Piece {
    /**
     * Instantiates a new Flag.
     */
// list attributes unique to Flag
    public Flag() {
        super(0, FLAG, "Flag");
        setKills();
    }

    void setKills() {
        this.kills = new HashSet<PieceType>();
    }
}
