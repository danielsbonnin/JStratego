package stratego.pieces;

import stratego.PieceType;
import static stratego.PieceType.*;

import java.util.HashSet;

public class Flag extends Piece{
    // list attributes unique to Flag

    public Flag(int range, PieceType pt, String pLabel) {
        super(0, FLAG, "Flag");
    }

    void setKills() {
        this.kills = new HashSet<PieceType>();
    }
}
