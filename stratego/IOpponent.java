package stratego;

/**
 * This interface represents the actions of a Stratego LocalPlayer
 * @author Daniel Bonnin
 */
public interface IOpponent {
    /**
     * Process a move and return next move
     * @return a new Move
     */
    Move getNextMove(Move m);

    /**
     * The initial board setup phase for this player
     * @return a json-formatted String of a valid p2 setup
     */
    String setup();
}
