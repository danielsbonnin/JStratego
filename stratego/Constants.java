package stratego;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
