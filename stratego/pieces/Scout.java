package stratego.pieces;

import static stratego.pieces.PieceType.*;

import java.util.HashSet;

/**
 * The type Scout.
 */
public class Scout extends Piece{
    /**
     * Instantiates a new Scout.
     */
    public Scout() {
        super(10, SCOUT, "9");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = { SPY, SCOUT };
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}

