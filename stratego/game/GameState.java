package stratego.game;

/**
 * The enum Game state.
 */
public enum GameState {
    /**
     * Setup not piece selected game state.
     */
    SETUP_NOT_PIECE_SELECTED, /**
     * Setup piece selected game state.
     */
    SETUP_PIECE_SELECTED, /**
     * Play p 1 game state.
     */
    PLAY_P1, /**
     * Play p 2 game state.
     */
    PLAY_P2,
    /**
     * Move origin selected game state.
     */
    MOVE_ORIGIN_SELECTED, /**
     * Move not origin selected game state.
     */
    MOVE_NOT_ORIGIN_SELECTED,
    AWAIT_P2_MOVE
}
