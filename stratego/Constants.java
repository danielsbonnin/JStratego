package stratego;

import java.util.*;

import static stratego.PieceType.*;

public class Constants {

    // disable instantiation
    private Constants() { }
    public static final int DEFAULT_BOARD_WIDTH = 10;
    public static final int DEFAULT_BOARD_HEIGHT = 10;
    public static final int DEFAULT_PIECES_COUNT = 40;
    private static PieceType default_pieces [] = {
            FLAG,
            BOMB, BOMB, BOMB, BOMB, BOMB, BOMB,
            SPY,
            SCOUT, SCOUT, SCOUT, SCOUT, SCOUT, SCOUT, SCOUT, SCOUT,
            MINER, MINER, MINER, MINER, MINER,
            SERGEANT, SERGEANT, SERGEANT, SERGEANT,
            LIEUTENANT, LIEUTENANT, LIEUTENANT, LIEUTENANT,
            CAPTAIN, CAPTAIN, CAPTAIN, CAPTAIN,
            MAJOR, MAJOR, MAJOR,
            COLONEL, COLONEL,
            GENERAL,
            MARSHALL
    };
    public static final Map<SquareState, String> SQUARE_STYLES;
    static {
        Map<SquareState, String> sqStyles = new EnumMap<>(SquareState.class);

        sqStyles.put(SquareState.WATER,
                "-fx-background-color: black, lightblue; -fx-background-insets: 0, 0 1 1 0;");
        sqStyles.put(SquareState.EMPTY_LAND,
                "-fx-background-color: black, lightgreen; -fx-background-insets: 0, 0 1 1 0;");
        sqStyles.put(SquareState.SELECTED_ORIGIN,
                "-fx-background-color: black, grey; -fx-background-insets: 0, 0 1 1 0;");
        sqStyles.put(SquareState.OCCUPIED_P1,
                "-fx-background-color: black, grey; -fx-background-insets: 0, 0 1 1 0;");
        sqStyles.put(SquareState.OCCUPIED_P2,
                "-fx-background-color: black, grey; -fx-background-insets: 0, 0 1 1 0;");
        sqStyles.put(SquareState.POSSIBLE_MOVE,
                "-fx-background-color: black, grey; -fx-background-insets: 0, 0 1 1 0;");

        if (!sqStyles.keySet().equals(EnumSet.allOf(SquareState.class))) {
            throw new RuntimeException(
                    "SQUARE_STYLES is missing entries for 1 or more SquareState constants."
            );
        }

        SQUARE_STYLES = Collections.unmodifiableMap(sqStyles);
    }
    public static final List<PieceType> DEFAULT_PIECES = Collections.unmodifiableList(
            new ArrayList<PieceType>() {
                {
                    for (int i = 0; i < DEFAULT_PIECES_COUNT; i++) {
                        add(default_pieces[i]);
                    }
                }
            }
    );
}
