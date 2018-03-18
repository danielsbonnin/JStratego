package stratego.pieces;

import java.util.HashSet;
import static stratego.pieces.PieceType.*;

/**
 * The type Captain.
 */
public class Captain extends Piece{
    /**
     * Instantiates a new Captain.
     */
    public Captain() {
        super(1, CAPTAIN, "5");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { FLAG, SPY, SCOUT, MINER, SERGEANT, LIEUTENANT, CAPTAIN };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}
