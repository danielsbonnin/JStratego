package stratego;

import stratego.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.DEFAULT_PIECES;
import static stratego.Constants.PIECETYPE_TO_PIECECLASS;
import static stratego.PieceType.*;

public class Player {
    protected String handle;
    protected PieceInventory inventory;
    protected boolean hasTurn;

    public Player(List<PieceType> pieces) {
        setupInventory(pieces);
    }

    public Player() {
        setupInventory(DEFAULT_PIECES);
    }

    private void setupInventory(List<PieceType> pieces) {
        this.inventory = new PieceInventory(pieces);
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getHandle() {
        return this.handle;
    }
}
