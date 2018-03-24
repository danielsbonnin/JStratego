package stratego.players;

import stratego.game.Game;
import stratego.pieces.PieceType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static stratego.Constants.DEFAULT_PIECES;

/**
 * The type LocalPlayer.
 */
public class LocalPlayer {
    /**
     * The Handle.
     */
    protected String handle;
    /**
     * The Inventory.
     */
    protected PieceInventory inventory;

    private int piecesLeftInInventory;
    private boolean isP1;

    private Game g;
    /**
     * Instantiates a new LocalPlayer.
     *
     * @param pieces the pieces
     */
    public LocalPlayer(List<PieceType> pieces, Game g) {
        this.g = g;
        this.isP1 = true;
        this.piecesLeftInInventory = pieces.size();
        setupInventory(pieces);
    }

    /**
     * Instantiate a new LocalPlayer
     *
     * @param pieces
     * @param isP1
     */
    public LocalPlayer(List<PieceType> pieces, boolean isP1, Game g) {
        this.g = g;
        this.isP1 = isP1;
        setupInventory(pieces);
        this.piecesLeftInInventory = pieces.size();
        //new Thread(this).start();
    }


    /**
     * Instantiates a new LocalPlayer.
     */
    public LocalPlayer() {
        setupInventory(DEFAULT_PIECES);
    }

    private void setupInventory(List<PieceType> pieces) {
        this.inventory = new PieceInventory(pieces);
        this.piecesLeftInInventory = pieces.size();
    }

    /**
     * Sets handle.
     *
     * @param handle the handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    public int getPiecesLeftInInventory() {
        return this.piecesLeftInInventory;
    }

    public void decrementPiecesLeftInInventory() {
        this.piecesLeftInInventory--;
    }

    public void incrementPiecesLeftInInventory() {
        this.piecesLeftInInventory++;
    }

    /**
     * Gets handle.
     *
     * @return the handle
     */
    public String getHandle() {
        return this.handle;
    }
    public PieceInventory getInventory() {
        return this.inventory;
    }
}
