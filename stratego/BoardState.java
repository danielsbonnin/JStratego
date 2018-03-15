package stratego;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Represent state of the Stratego gameboard
 * @author Daniel Bonnin
 */
public class BoardState {
    private List< List<PlayerPiece> > state;
    private int height;
    private int width;

    /**
     * Initialize an empty board
     * @param height
     * @param width
     */
    public BoardState(int height, int width) {
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

    public BoardState(List< List<PlayerPiece> > newBoardState) {
        this.width = newBoardState.size();
        if (this.width > 0) {
            this.height = newBoardState.get(0).size();
        } else {
            this.height = 0;
        }
        this.state = new ArrayList<>(newBoardState);
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
     * @param newState 2d List of PieceType
     */
    public void setAll(List< List<PieceType>> newState) {
        // Ensure new board's dimensions match existing
        try {
            assert(newState.size() == this.height && newState.get(0).size() == this.width);
        } catch (AssertionError e) {
            System.out.println("Dimensions of new board state do not match existing.");
        }
        this.state = new ArrayList<>(state);
    }

    /**
     * Parse board from json String and set
     * @see Gson
     * @param newState json formatted String
     */
    public void fromJson(String newState) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken< List< List<PieceType> > >(){}.getType();
        List< List<PieceType>> newBoard = gson.fromJson(newState, collectionType);
        setAll(newBoard);
    }

    /**
     * Board as json object
     */
    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this.state);
    }
}
