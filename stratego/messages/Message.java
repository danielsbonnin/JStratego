package stratego.messages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import stratego.board.BoardData;
import stratego.game.Move;

import java.lang.reflect.Type;

/**
 * @author Daniel Bonnin
 */
public class Message {
    private MsgType type;
    private String jsonObj;

    public Message (MsgType type, String jsonObj) {
        this.type = type;
        this.jsonObj = jsonObj;
    }

    public static Message fromJson(String jsonMessage) {
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, Message.class);
    }
    /**
     * The message to transmit
     * @return
     */
    String message() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public MsgType getType() {
        return this.type;
    }

    public String getJsonObj() {
        return this.jsonObj;
    }

    public Object getObj() {
        Gson gson = new Gson();
        switch (this.type) {
            case MOVE:
                return gson.fromJson(this.jsonObj, Move.class);
            case START_SETUP:
                return true;
            case SETUP_COMPLETE:
                return new BoardData(this.getJsonObj());
            default:
                return null;
        }
    }
}
