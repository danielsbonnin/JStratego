package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class Water extends Piece {
    // list attributes unique to Flag

    public Water(int range, PieceType pt, String pLabel) {
        super(0, WATER, "Water");
    }

    void setKills() {
        this.kills = new HashSet<PieceType>();
    }
}

