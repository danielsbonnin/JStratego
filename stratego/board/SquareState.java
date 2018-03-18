package stratego.board;

/**
 * The enum Square state.
 */
public enum SquareState {
    /**
     * Selected origin square state.
     */
    SELECTED_ORIGIN, /**
     * Possible move square state.
     */
    POSSIBLE_MOVE, /**
     * Occupied p 1 square state.
     */
    OCCUPIED_P1, /**
     * Occupied p 2 square state.
     */
    OCCUPIED_P2, /**
     * Empty land square state.
     */
    EMPTY_LAND, /**
     * Water square state.
     */
    WATER
}
