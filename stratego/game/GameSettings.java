package stratego.game;

import com.google.gson.Gson;
import stratego.board.BoardCoords;
import stratego.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

import static stratego.Constants.DEFAULT_PIECES;
import static stratego.Constants.DEFAULT_WATER_SQUARES;

/**
 * @author Daniel Bonnin
 */
public class GameSettings {
    private int boardHeight;
    private int boardWidth;
    private List<PieceType> pieces;
    private List<BoardCoords> waterSquares;

    public GameSettings() {
        this.boardHeight = 10;
        this.boardWidth = 10;
        this.pieces = new ArrayList<>(DEFAULT_PIECES);
        this.waterSquares = new ArrayList<>(DEFAULT_WATER_SQUARES);
    }

    public int getBoardHeight() {
        return this.boardHeight;
    }

    public int getBoardWidth() {
        return this.boardWidth;
    }

    public List<PieceType> getPieces() {
        return this.pieces;
    }

    public List<BoardCoords> getWaterSquares() {
        return this.waterSquares;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
