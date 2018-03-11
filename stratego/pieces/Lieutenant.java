package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class Lieutenant extends Piece{
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

