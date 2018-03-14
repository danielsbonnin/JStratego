package stratego;

import stratego.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.DEFAULT_WATER_SQUARES;
import static stratego.SquareState.*;

/**
 * The type Board.
 */
public class Board {
    private List< List<Square> > board;
    /**
     * The Width.
     */
    int width;
    /**
     * The Height.
     */
    int height;

    /**
     * Instantiates a new Board.
     *
     * @param width               the width
     * @param height              the height
     * @param defaultWaterSquares the default water squares
     */
    public Board(int width, int height, boolean defaultWaterSquares) {
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

        // Set default water squares
        if (defaultWaterSquares) {
            for (BoardCoords bc : DEFAULT_WATER_SQUARES)
                getSquare(bc.r, bc.c).setState(WATER);
        }
    }

    /**
     * Gets square.
     *
     * @param r the r
     * @param c the c
     * @return the square
     */
    public Square getSquare(int r, int c) {
        return this.board.get(r).get(c);
    }
    // Methods for updating GUI

    // Methods for moving and attacking

    /**
     * Place boolean.
     *
     * @param r the r
     * @param c the c
     * @param p the p
     * @return the boolean
     */
    public boolean place(int r, int c, Piece p, boolean player1) {
        Square dest = board.get(r).get(c);
        SquareState destState = dest.getState();
        if (
                destState == WATER
                || (player1 ? destState == OCCUPIED_P1 : destState == OCCUPIED_P2)
                || !dest.isEmpty()
        ) {
            return false;
        }
        dest.setPiece(p);
        dest.setState(player1 ? OCCUPIED_P1 : OCCUPIED_P2);
        return true;
    }

    /**
     * Move boolean.
     *
     * @param startRow the start row
     * @param startCol the start col
     * @param endRow   the end row
     * @param endCol   the end col
     * @return the boolean
     */
    public boolean move(int startRow, int startCol, int endRow, int endCol) {
        Square st = getSquare(startRow, startCol);
        Piece stPiece = st.getPiece();
        Square end = getSquare(endRow, endCol);
        List<Square> moves = getMoves(startRow, startCol);
        if (!moves.contains(end)) return false;
        st.remove();
        if (!end.isEmpty()) {  // attack move
            PieceType eType = end.getPiece().getPt();

            // attacker lost, attacking piece is discarded
            if (!stPiece.getKills().contains(eType)) {
                return true;
            }
            end.remove();
        }
        end.setPiece(stPiece);
        return true;
    }

    /**
     * Gets moves.
     *
     * @param r the r
     * @param c the c
     * @return the moves
     */
    public List<Square> getMoves(int r, int c) {
        Square sq = board.get(r).get(c);
        SquareState sqState = sq.getState();
        ArrayList<Square> poss = new ArrayList<Square>();
        if (sq.isEmpty()) return poss;
        int n = sq.getPiece().getRange();
        for (int i = r - 1; i >= 0 && (r-i <= n); i--) {
            Square cur = board.get(i).get(c);
            if (cur.getState() == WATER) break;

            // this player's piece
            if (cur.getState() == sqState) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = r + 1; i < this.height && (i - r <= n); i++) {
            Square cur = board.get(i).get(c);
            if (cur.getState() == WATER) break;

            // this player's piece
            if (cur.getState() == sqState) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = c - 1; i >= 0 && (c-i <= n); i--) {
            Square cur = board.get(r).get(i);
            if (cur.getState() == WATER) break;

            // this player's piece
            if (cur.getState() == sqState) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }

        for (int i = c + 1; i < this.width && (i-c <= n); i++) {
            Square cur = board.get(r).get(i);
            if (cur.getState() == WATER) break;

            // this player's piece
            if (cur.getState() == sqState) break;
            poss.add(cur);
            if (!cur.isEmpty()) break;
        }
        return poss;
    }

    // methods for setting state

    /**
     * Select square square.
     *
     * @param r the r
     * @param c the c
     * @return the square
     */
    public Square selectSquare(int r, int c) {
        Square selected = this.board.get(r).get(c);
        selected.setState(SELECTED_ORIGIN);
        return selected;
    }

    /**
     * Unselect square.
     *
     * @param r the r
     * @param c the c
     */
    public void unselectSquare(int r, int c) {
        Square selected = this.board.get(r).get(c);
        selected.setState(selected.getLast());
    }
}