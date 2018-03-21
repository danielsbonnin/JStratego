package stratego.messages;

import com.google.gson.Gson;
import stratego.game.Move;

import java.lang.reflect.Type;

/**
 * @author Daniel Bonnin
 */
public class MoveMessage extends Message {
    public MoveMessage(MsgType type, Move move) {
        super(type, move.toJson());
    }

    public MoveMessage(MsgType type, String jsonMove) {
        super(type, jsonMove);
    }
}
