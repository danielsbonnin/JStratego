package stratego.board;

import stratego.game.Move;
import stratego.pieces.Piece;
import stratego.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.DEFAULT_WATER_SQUARES;
import static stratego.Constants.PIECETYPE_TO_PIECECLASS;
import static stratego.board.SquareState.*;

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
     * The highest row allowed for placing pieces during setup
     */
    int placementRowStIdx;


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

        // restrict board squares valid for placement
        this.placementRowStIdx = this.height - ((this.height / 2) - 1);

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
     * Whether square is valid origin for pickup
     */
    public boolean validPickup(BoardCoords bc) {
        Square pickupSq = getSquare(bc.r, bc.c);
        // not empty and piece is player1
        return (!pickupSq.isEmpty() && pickupSq.getPiece().getP1());
    }

    /**
     * remove piece at coordinates if valid for pickup
     * @param bc board coordinates to pick up
     * @return
     */
    public PieceType pickupPiece(BoardCoords bc) {
        PieceType pt = getSquare(bc.r, bc.c).getPiece().getPt();
        getSquare(bc.r, bc.c).remove();
        return pt;
    }

    /**
     * Whether square is valid destination for placement during setup
     */
    public boolean validPlacement(BoardCoords bc) {
        Square placementSq = getSquare(bc.r, bc.c);
        return (bc.r >= this.placementRowStIdx && placementSq.isEmpty());
    }

    /**
     * Try to place a piece on the board during setup
     * @param bc the board coordinates
     * @param pt the type of piece
     */
    public boolean tryPlacePiece(BoardCoords bc, PieceType pt) {
        if (!validPlacement(bc)) return false;
        Piece toPlace = PIECETYPE_TO_PIECECLASS.get(pt);
        toPlace.setP1(true);
        Square placeSq = getSquare(bc.r, bc.c);
        placeSq.setPiece(toPlace);
        return true;
    }

    /**
     * Whether square is valid origin for move
     */
    public boolean validOrigin(BoardCoords bc) {
        Square originSq = getSquare(bc.r, bc.c);
        return (!originSq.isEmpty() && originSq.getPiece().getP1() && originSq.getPiece().getRange() > 0);
    }

    /**
     * Place boolean.
     *
     * @param r the r
     * @param c the c
     * @param p the p
     * @return the boolean
     * @deprecated
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
     * is valid Move
     *
     * @pre assumes player 1
     * @param move the proposed move
     * @return boolean valid move
     */
    private boolean isValidMove(Move move) {
        Square end = getSquare(move.getDestination().r, move.getDestination().c);

        // the set of possible moves starting at origin
        List<Square> moves = getMoves(move.getOrigin());

        // the list of valid moves contains the proposed destination
        return(moves.contains(end));
    }

    /**
     * validate and execute a move
     * @param move proposed move
     * @param isP1 whether move by player 1
     * @return
     */
    public boolean move(Move move, boolean isP1) {
        if (isP1) {  // player 2 moves assumed valid

            // unhighlight possible move squares
            hidePossibleMoves(move.getOrigin());
            
            if (!isValidMove(move))  return false;  // p1 move is not valid
        }

        Square stSq = getSquare(move.getOrigin().r, move.getOrigin().c);
        Piece stPiece = stSq.getPiece();
        Square end = getSquare(move.getDestination().r, move.getDestination().c);

        if (!end.isEmpty()) {  // attack move
            PieceType eType = end.getPiece().getPt();

            // attacker lost, attacking piece is discarded
            if (!stPiece.getKills().contains(eType)) {
                // pass

            } else { // attacker won, attacked piece is discarded
                end.remove();
                end.setPiece(stPiece);
            }
        } else { // move to empty land
            end.setPiece(stPiece);
        }
        // remove piece from start square.
        stSq.remove();

        return true;
    }

    /**
     * Get possible moves from origin coordinates.
     *
     * @param origin coords at proposed move origin
     * @return the moves
     */
    public List<Square> getMoves(BoardCoords origin) {
        int r = origin.r;
        int c = origin.c;
        Square sq = getSquare(r, c);
        ArrayList<Square> poss = new ArrayList<Square>();
        if (sq.isEmpty()) return poss;
        Piece stPiece = sq.getPiece();
        int n = stPiece.getRange();
        for (int i = r - 1; i >= 0 && (r-i <= n); i--) {
            Square cur = getSquare(i, c);

            if (cur.getState() == WATER) break;

            // cur contains a piece
            if (!cur.isEmpty()) {
                Piece curPiece = cur.getPiece();
                if (curPiece.getP1() == stPiece.getP1())  // friendly piece
                    break;
                poss.add(cur);  // opponent piece
                break;
            } else {
                poss.add(cur);
            }
        }

        for (int i = r + 1; i < this.height && (i - r <= n); i++) {
            Square cur = getSquare(i, c);

            if (cur.getState() == WATER) break;

            // cur contains a piece
            if (!cur.isEmpty()) {
                Piece curPiece = cur.getPiece();
                if (curPiece.getP1() == stPiece.getP1())  // friendly piece
                    break;
                poss.add(cur);  // opponent piece
                break;
            } else {
                poss.add(cur);
            }
        }

        for (int i = c - 1; i >= 0 && (c-i <= n); i--) {
            Square cur = getSquare(r, i);

            if (cur.getState() == WATER) break;

            // cur contains a piece
            if (!cur.isEmpty()) {
                Piece curPiece = cur.getPiece();
                if (curPiece.getP1() == stPiece.getP1())  // friendly piece
                    break;
                poss.add(cur);  // opponent piece
                break;
            } else {  // square is empty
                poss.add(cur);
            }
        }

        for (int i = c + 1; i < this.width && (i-c <= n); i++) {
            Square cur = getSquare(r, i);

            if (cur.getState() == WATER) break;

            // cur contains a piece
            if (!cur.isEmpty()) {
                Piece curPiece = cur.getPiece();
                if (curPiece.getP1() == stPiece.getP1())  // friendly piece
                    break;
                poss.add(cur);  // opponent piece
                break;
            } else {  // square is empty
                poss.add(cur);
            }
        }
        return poss;
    }

    /**
     * Highlight squares of possible moves
     * @param origin starting square
     */
    public void showPossibleMoves(BoardCoords origin) {
        List<Square> poss = getMoves(origin);
        for (Square m : poss) {
            m.highlight();
        }
    }

    /**
     * unHighlight squares of possible moves
     * @param origin starting square coordinates of highlighted poss moves
     */
    public void hidePossibleMoves(BoardCoords origin) {
        List<Square> poss = getMoves(origin);
        for (Square m : poss) {
            m.unHighlight();
        }
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

    public BoardData toBoardData() {
        BoardData curState = new BoardData(this.height, this.width);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                BoardCoords bc = new BoardCoords(i, j);
                Square curSquare = getSquare(i, j);
                Piece p = null;
                PieceType pt = null;
                boolean player1 = false;
                if (!curSquare.isEmpty()) {
                    p = curSquare.getPiece();
                    pt = p.getPt();
                    player1 = p.getP1();
                }
                PlayerPiece pp = new PlayerPiece(pt, player1);
                curState.set(bc, pp);
            }
        }
        return curState;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
}