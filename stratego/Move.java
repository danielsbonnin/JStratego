package stratego;

import com.google.gson.Gson;
import stratego.board.BoardCoords;

/**
 * @author Daniel Bonnin
 */
public class Move {
    BoardCoords origin;
    BoardCoords destination;
    boolean isFinal;
    boolean isP1;

    public Move(BoardCoords origin, BoardCoords destination, boolean isFinal, boolean isP1) {
        this.origin = origin;
        this.destination = destination;
        this.isFinal = isFinal;
        this.isP1 = isP1;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
