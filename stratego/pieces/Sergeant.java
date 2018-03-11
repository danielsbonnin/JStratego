package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class Sergeant extends Piece{
    public Sergeant() {
        super(1, SERGEANT, "7");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { FLAG, SPY, SCOUT, MINER, SERGEANT };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}

