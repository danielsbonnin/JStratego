package stratego.pieces;

import stratego.PieceType;

import java.util.HashSet;

import static stratego.PieceType.*;

public class Bomb extends Piece {
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
