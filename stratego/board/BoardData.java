package stratego.board;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import stratego.pieces.PieceType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Represent state of the Stratego gameboard
 * @author Daniel Bonnin
 */
public class BoardData {
    private List< List<PlayerPiece> > state;
    private int height;
    private int width;

    /**
     * Initialize an empty board
     * @param height
     * @param width
     */
    public BoardData(int height, int width) {
        this.state = new ArrayList< List<PlayerPiece>>();
        this.height = height;
        this.width = width;
        for (int i = 0; i < this.height; i++) {
            ArrayList<PlayerPiece> row = new ArrayList<PlayerPiece>();
            for (int j = 0; j < this.width; j++) {
                row.add(null);
            }
            this.state.add(row);
        }
    }

    public BoardData(List< List<PlayerPiece> > newBoardState) {
        this.width = newBoardState.size();
        if (this.width > 0) {
            this.height = newBoardState.get(0).size();
        } else {
            this.height = 0;
        }
        this.state = new ArrayList<>(newBoardState);
    }

    public BoardData(String jsonState) {
        this(fromJson(jsonState));
    }

    int getHeight() { return this.height; }
    int getWidth() { return this.width; }
    void setHeight(int height) { this.height = height; }
    void setWidth (int width) { this.width = width; }

    /**
     * Get a piece on the board
     * @param bc BoardType coordinates of a piece on this board
     * @return PieceType piece at bc
     */
    public PlayerPiece get(BoardCoords bc) {
        return this.state.get(bc.r).get(bc.c);
    }

    /**
     * Set a piece on the board
     * @param bc BoardType coordinates of a piece on this board
     * @param newPiece PieceType type of piece
     */
    public void set(BoardCoords bc, PlayerPiece newPiece) {
        this.state.get(bc.r).set(bc.c, newPiece);
    }

    /**
     * Sets all pieces on the board
     * @param newState 2d List of PlayerPiece
     */
    public void setAll(List< List<PlayerPiece>> newState) {
        // Ensure new board's dimensions match existing
        try {
            assert(newState.size() == this.height && newState.get(0).size() == this.width);
        } catch (AssertionError e) {
            System.out.println("Dimensions of new board state do not match existing.");
        }
        this.state = new ArrayList<>();
        this.state.addAll(newState);
    }

    /**
     * Parse board state from json String
     * @see Gson
     * @param newState json formatted String
     */
    public static List< List<PlayerPiece> > fromJson(String newState) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken< List< List<PlayerPiece> > >(){}.getType();
        List< List<PlayerPiece>> newBoard =
                gson.fromJson(newState, collectionType);
        return newBoard;
    }

    /**
     * Reverse the board pieces to represent opponent's view
     */
    public void reverse() {
        for (int i = 0; i < (getHeight() / 2); i++)
            for (int j = 0; j < (getWidth()/2); j++) {
                BoardCoords top = new BoardCoords(i, j);
                BoardCoords bottom = new BoardCoords(getHeight() - i - 1, getWidth()-j - 1);
                PlayerPiece newTop = get(bottom);
                PlayerPiece newBottom = get(top);
                newBottom.setIsP1(!newBottom.getIsP1());
                newTop.setIsP1(!newTop.getIsP1());
                set(top, newTop);
                set(bottom, newBottom);
            }
    }

    /**
     * Board as json object
     */
    public String toJsonString() {
        Gson gson = new Gson();
        Type ptList = new TypeToken<List<List<PlayerPiece>>>() {}.getType();
        return gson.toJson(this.state, ptList);
    }
}
