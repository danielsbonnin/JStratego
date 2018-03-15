package stratego;

import java.util.List;

import static stratego.Constants.DEFAULT_PIECES;

/**
 * The type Player.
 */
public class Player {
    /**
     * The Handle.
     */
    protected String handle;
    /**
     * The Inventory.
     */
    protected PieceInventory inventory;

    private boolean isP1;

    /**
     * Instantiates a new Player.
     *
     * @param pieces the pieces
     */
    public Player(List<PieceType> pieces) {
        this.isP1 = true;
        setupInventory(pieces);
    }

    /**
     * Instantiate a new Player
     *
     * @param pieces
     * @param isP1
     */
    public Player(List<PieceType> pieces, boolean isP1) {
        this.isP1 = isP1;
        setupInventory(pieces);
    }

    /**
     * Instantiates a new Player.
     */
    public Player() {
        setupInventory(DEFAULT_PIECES);
    }

    private void setupInventory(List<PieceType> pieces) {
        this.inventory = new PieceInventory(pieces);
    }

    /**
     * Sets handle.
     *
     * @param handle the handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Gets handle.
     *
     * @return the handle
     */
    public String getHandle() {
        return this.handle;
    }
}
