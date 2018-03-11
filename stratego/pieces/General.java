package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class General extends Piece{
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

