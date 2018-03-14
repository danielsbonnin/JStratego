package stratego;

import stratego.pieces.*;

import java.util.*;

import static stratego.PieceType.*;

/**
 * useful constants for use with stratego classes
 */
public class Constants {
    // disable instantiation
    private Constants() { }

    /**
     * The constant DEFAULT_BOARD_WIDTH.
     */
    public static final int DEFAULT_BOARD_WIDTH = 10;
    /**
     * The constant DEFAULT_BOARD_HEIGHT.
     */
    public static final int DEFAULT_BOARD_HEIGHT = 10;
    /**
     * The constant DEFAULT_PIECES_COUNT.
     */
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

    /**
     * Map of enum SquareState to css style
     *
     * @usage String squareStyle = SQUARE_STYLES.get(SquareState.WATER);
     */
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
                "-fx-background-color: black, lightblue; -fx-background-insets: 0, 0 1 1 0;");
        sqStyles.put(SquareState.OCCUPIED_P2,
                "-fx-background-color: black, lightred; -fx-background-insets: 0, 0 1 1 0;");
        sqStyles.put(SquareState.POSSIBLE_MOVE,
                "-fx-background-color: black, orange; -fx-background-insets: 0, 0 1 1 0;");

        // enforce that each SquareStyle and only SquareStyle keys exist
        if (!sqStyles.keySet().equals(EnumSet.allOf(SquareState.class))) {
            throw new RuntimeException(
                    "SQUARE_STYLES is missing entries for 1 or more SquareState constants."
            );
        }
        SQUARE_STYLES = Collections.unmodifiableMap(sqStyles);
    }

    /**
     * Piece set for standard 10 X 10 40 piece Stratego game
     */
    public static final List<PieceType> DEFAULT_PIECES = Collections.unmodifiableList(
            new ArrayList<PieceType>() {
                {
                    for (int i = 0; i < DEFAULT_PIECES_COUNT; i++) {
                        add(default_pieces[i]);
                    }
                }
            }
    );

    /**
     * Map of PieceType to Piece instance
     *
     * @usage Piece piece = PIECETYPE_TO_PIECECLASS.get(MINER);
     */
    public static final Map<PieceType, Piece> PIECETYPE_TO_PIECECLASS;
    static {
        Map<PieceType, Piece> pieceClasses = new EnumMap<>(PieceType.class);
        pieceClasses.put(FLAG, new Flag());
        pieceClasses.put(BOMB, new Bomb());
        pieceClasses.put(SPY, new Spy());
        pieceClasses.put(SCOUT, new Scout());
        pieceClasses.put(MINER, new Miner());
        pieceClasses.put(SERGEANT, new Sergeant());
        pieceClasses.put(LIEUTENANT, new Lieutenant());
        pieceClasses.put(CAPTAIN, new Captain());
        pieceClasses.put(MAJOR, new Major());
        pieceClasses.put(COLONEL, new Colonel());
        pieceClasses.put(GENERAL, new General());
        pieceClasses.put(MARSHALL, new Marshall());
        PIECETYPE_TO_PIECECLASS = Collections.unmodifiableMap(pieceClasses);
    }

    /**
     * Map of String to PieceType
     *
     * @usage PieceType pt = PIECETYPESTRING_TO_PIECETYPE.get("MINER");
     */
    public static final Map<String, PieceType> PIECETYPESTRING_TO_PIECETYPE;
    static {
        Map<String, PieceType> pieceStrings = new HashMap<String, PieceType>();
        pieceStrings.put("FLAG", FLAG);
        pieceStrings.put("BOMB", BOMB);
        pieceStrings.put("SPY", SPY);
        pieceStrings.put("SCOUT", SCOUT);
        pieceStrings.put("MINER", MINER);
        pieceStrings.put("SERGEANT", SERGEANT);
        pieceStrings.put("LIEUTENANT", LIEUTENANT);
        pieceStrings.put("CAPTAIN", CAPTAIN);
        pieceStrings.put("MAJOR", MAJOR);
        pieceStrings.put("COLONEL", COLONEL);
        pieceStrings.put("GENERAL", GENERAL);
        pieceStrings.put("MARSHALL", MARSHALL);
        PIECETYPESTRING_TO_PIECETYPE = Collections.unmodifiableMap(pieceStrings);
    }

    /**
     * water Square coordinates for standard 10 X 10 40 piece Stratego game
     */
    private static BoardCoords [] defaultWaterSquares = {
            new BoardCoords(4, 2),
            new BoardCoords(4, 3),
            new BoardCoords(4, 6),
            new BoardCoords(4, 7),
            new BoardCoords(5, 2),
            new BoardCoords(5, 3),
            new BoardCoords(5, 6),
            new BoardCoords(5, 7)

    };

    /**
     * The constant DEFAULT_WATER_SQUARES.
     */
    public static final List <BoardCoords> DEFAULT_WATER_SQUARES = Collections.unmodifiableList(
            new ArrayList<BoardCoords>() {
                {
                    for (int i = 0; i < 8; i++)
                        add(defaultWaterSquares[i]);
                }
            }
    );
}