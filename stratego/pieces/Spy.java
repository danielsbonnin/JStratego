package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class Spy extends Piece{
    public Spy() {
        super(1, SPY, "Spy");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { FLAG, SPY, MARSHALL };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}

