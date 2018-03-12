package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class Miner extends Piece{
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

