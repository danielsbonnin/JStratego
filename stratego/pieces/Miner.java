package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

/**
 * The type Miner.
 */
public class Miner extends Piece{
    /**
     * Instantiates a new Miner.
     */
    public Miner() {
        super(1, MINER, "8");
        setKills();
    }

    void setKills() {

        PieceType[] myKills = {FLAG, BOMB, SPY, SCOUT, MINER};
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}

