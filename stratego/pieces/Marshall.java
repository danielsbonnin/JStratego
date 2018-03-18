package stratego.pieces;

import static stratego.pieces.PieceType.*;

import java.util.HashSet;

/**
 * The type Marshall.
 */
public class Marshall extends Piece{
    /**
     * Instantiates a new Marshall.
     */
    public Marshall() {
        super(1, MARSHALL, "1");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { FLAG, SCOUT, MINER, SERGEANT, LIEUTENANT, CAPTAIN, MAJOR, COLONEL, GENERAL, MARSHALL };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}
