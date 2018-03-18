package stratego.pieces;

import static stratego.pieces.PieceType.*;

import java.util.HashSet;

/**
 * The type Spy.
 */
public class Spy extends Piece{
    /**
     * Instantiates a new Spy.
     */
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

