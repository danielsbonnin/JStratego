package stratego.pieces;

import static stratego.pieces.PieceType.*;

import java.util.HashSet;

/**
 * The type Lieutenant.
 */
public class Lieutenant extends Piece{
    /**
     * Instantiates a new Lieutenant.
     */
    public Lieutenant() {
        super(1, LIEUTENANT, "6");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { FLAG, SPY, SCOUT, MINER, SERGEANT, LIEUTENANT };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}

