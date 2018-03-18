package stratego.messages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @author Daniel Bonnin
 */
abstract class Message {
    MsgType type;
    String jsonObj;

    public Message (MsgType type, String jsonObj) {
        this.type = type;
        this.jsonObj = jsonObj;
    }

    /**
     * The message to transmit
     * @return
     */
    String message() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    abstract Object getObj();
}
