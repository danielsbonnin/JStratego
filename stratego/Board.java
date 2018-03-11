package stratego;

import stratego.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.DEFAULT_BOARD_HEIGHT;
import static stratego.Constants.DEFAULT_BOARD_WIDTH;

public class Board {
    private List< List<Square> > board;
    int width;
    int height;

    public Board() {
        this.width = DEFAULT_BOARD_WIDTH;
        this.height = DEFAULT_BOARD_HEIGHT;
        this.board = new ArrayList< List<Square> >();
        for (int i = 0; i < this.height; i++) {
            this.board.add(new ArrayList<Square>(this.width));
        }
    }

    public boolean move(int startRow, int startCol, int endRow, int endCol) {
        Square st = board.get(startRow).get(startCol);
        Piece stPiece = st.getPiece();
        Square end = board.get(endRow).get(endCol);
        List<Square> moves = getMoves(startRow, startCol);
        if (!moves.contains(end)) return false;
        st.remove();
        if (!end.isEmpty()) {
            PieceType eType = end.getPiece().getPt();
            if (!st.getPiece().getKills().contains(eType)) return true;
            end.remove();
        }
        end.setPiece(stPiece);
        return true;
    }

    public List<Square> getMoves(int r, int c) {
        Square sq = board.get(r).get(c);
        ArrayList<Square> poss = new ArrayList<Square>();
        if (sq.isEmpty()) return poss;
        int n = sq.getPiece().getRange();

        for (int i = r - 1; i >= 0 && (r-i <= n); i--) {
            Square cur = board.get(i).get(c);
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = r + 1; i < this.height && (i - r <= n); i++) {
            Square cur = board.get(i).get(c);
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = c - 1; i >= 0 && (c-i <= n); i--) {
            Square cur = board.get(r).get(i);
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = c + 1; i < this.width && (i-c <= n); i++) {
            Square cur = board.get(r).get(i);
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }
        return poss;
    }
}