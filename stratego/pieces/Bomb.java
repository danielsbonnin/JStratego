package stratego.pieces;

import java.util.HashSet;

import static stratego.pieces.PieceType.*;

/**
 * The type Bomb.
 */
public class Bomb extends Piece {
    /**
     * Instantiates a new Bomb.
     */
    public Bomb() {
        super(0, BOMB, "B");
        setKills();
    }

    void setKills() {
        PieceType[] myKills = {SPY, SCOUT, SERGEANT, LIEUTENANT, CAPTAIN, MAJOR, COLONEL, GENERAL, MARSHALL};
        this.kills = new HashSet<PieceType>();
        for (PieceType p : myKills)
            this.kills.add(p);
    }
}
