package stratego;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The type Piece inventory.
 */
public class PieceInventory {
    /**
     * The Piece counts.
     */
    Map<PieceType, Integer> pieceCounts;

    /**
     * Instantiates a new Piece inventory.
     */
    public PieceInventory() {
        this.pieceCounts = new EnumMap<>(PieceType.class);
        for (PieceType pt : PieceType.values())
            pieceCounts.put(pt, 0);
    }

    /**
     * Instantiates a new Piece inventory.
     *
     * @param pieces the pieces
     */
    public PieceInventory(List<PieceType> pieces) {
        this();
        setPieceCounts(pieces);
    }

    /**
     * Sets piece counts.
     *
     * @param pieces the pieces
     */
    public void setPieceCounts(List<PieceType> pieces) {
        for (PieceType p : pieces) {
            this.pieceCounts.put(p, this.pieceCounts.get(p) + 1);
        }
    }

    /**
     * Gets count.
     *
     * @param pt the pt
     * @return the count
     */
    public int getCount(PieceType pt) {
        return this.pieceCounts.get(pt);
    }

    /**
     * Remove int.
     *
     * @param pt the pt
     * @return the int
     */
    public int remove(PieceType pt) {
        int cur = this.pieceCounts.get(pt);
        this.pieceCounts.put(pt, --cur);
        return cur;
    }

    /**
     * Replace int.
     *
     * @param pt the pt
     * @return the int
     */
    public int replace(PieceType pt) {
        int cur = this.pieceCounts.get(pt);
        this.pieceCounts.put(pt, ++cur);
        return cur;
    }
}
