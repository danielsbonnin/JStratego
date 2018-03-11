package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class Major extends Piece{
    public Major() {
        super(1, MAJOR, "4");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { FLAG, SPY, SCOUT, MINER, SERGEANT, LIEUTENANT, CAPTAIN, MAJOR };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}

