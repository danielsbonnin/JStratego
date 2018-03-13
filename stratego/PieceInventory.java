package stratego;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PieceInventory {
    Map<PieceType, Integer> pieceCounts;
    public PieceInventory() {
        this.pieceCounts = new EnumMap<>(PieceType.class);
        for (PieceType pt : PieceType.values())
            pieceCounts.put(pt, 0);
    }

    public PieceInventory(List<PieceType> pieces) {
        this();
        setPieceCounts(pieces);
    }

    public void setPieceCounts(List<PieceType> pieces) {
        for (PieceType p : pieces) {
            this.pieceCounts.put(p, this.pieceCounts.get(p) + 1);
        }
    }

    public int getCount(PieceType pt) {
        return this.pieceCounts.get(pt);
    }

    public int remove(PieceType pt) {
        int cur = this.pieceCounts.get(pt);
        this.pieceCounts.put(pt, --cur);
        return cur;
    }

    public int replace(PieceType pt) {
        int cur = this.pieceCounts.get(pt);
        this.pieceCounts.put(pt, ++cur);
        return cur;
    }
}
