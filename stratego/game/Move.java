package stratego.game;

import com.google.gson.Gson;
import stratego.board.BoardCoords;

/**
 * @author Daniel Bonnin
 */
public class Move {
    private BoardCoords origin;
    private BoardCoords destination;
    private boolean isFinal;
    private boolean isP1;

    public Move(BoardCoords origin, BoardCoords destination, boolean isFinal, boolean isP1) {
        this.origin = origin;
        this.destination = destination;
        this.isFinal = isFinal;
        this.isP1 = isP1;
    }

    public BoardCoords getOrigin() {
        return this.origin;
    }

    public void setOrigin(BoardCoords origin) {
        this.origin = origin;
    }

    public BoardCoords getDestination() {
        return this.destination;
    }

    public void setDestination(BoardCoords destination) {
        this.destination = destination;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
