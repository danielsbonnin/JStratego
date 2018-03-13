package stratego;

import javafx.scene.layout.GridPane;
import stratego.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.DEFAULT_BOARD_HEIGHT;
import static stratego.Constants.DEFAULT_BOARD_WIDTH;
import static stratego.SquareState.*;

public class Board {
    private List< List<Square> > board;
    int width;
    int height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new ArrayList< List<Square> >();
        for (int i = 0; i < this.height; i++) {
            ArrayList<Square> cur = new ArrayList<Square>();
            for (int j = 0; j < this.width; j++) {
                cur.add(new Square(EMPTY_LAND));
            }
            this.board.add(cur);
        }
    }

    public Square getSquare(int r, int c) {
        return this.board.get(r).get(c);
    }
    // Methods for updating GUI


    // Methods for moving and attacking

    public boolean place(int r, int c, Piece p) {
        Square dest = board.get(r).get(c);
        if (dest.getState() == WATER || !dest.isEmpty()) return false;
        dest.setPiece(p);
        return true;
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
            if (cur.getState() == WATER) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = r + 1; i < this.height && (i - r <= n); i++) {
            Square cur = board.get(i).get(c);
            if (cur.getState() == WATER) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = c - 1; i >= 0 && (c-i <= n); i--) {
            Square cur = board.get(r).get(i);
            if (cur.getState() == WATER) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = c + 1; i < this.width && (i-c <= n); i++) {
            Square cur = board.get(r).get(i);
            if (cur.getState() == WATER) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }
        return poss;
    }

    // methods for setting state

    public Square selectSquare(int r, int c) {
        Square selected = this.board.get(r).get(c);
        selected.setState(SELECTED_ORIGIN);
        return selected;
    }

    public void unselectSquare(int r, int c) {
        Square selected = this.board.get(r).get(c);
        selected.setState(selected.getLast());
    }
}