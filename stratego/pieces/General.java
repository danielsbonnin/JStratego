package stratego.pieces;

import static stratego.pieces.PieceType.*;

import java.util.HashSet;

/**
 * The type General.
 */
public class General extends Piece{
    /**
     * Instantiates a new General.
     */
    public General() {
        super(1, GENERAL, "2");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { FLAG, SPY, SCOUT, MINER, SERGEANT, LIEUTENANT, CAPTAIN, MAJOR, COLONEL, GENERAL };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}

