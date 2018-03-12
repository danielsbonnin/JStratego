package stratego;

import stratego.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected String handle;
    protected List<Piece> inventory;
    protected boolean hasTurn;

    public Player(List<Piece> pieces) {
        this.inventory = new ArrayList<Piece>();
        this.inventory.addAll(pieces);
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getHandle() {
        return this.handle;
    }
}
