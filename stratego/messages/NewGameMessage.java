package stratego.messages;

import com.google.gson.Gson;
import stratego.game.GameSettings;

import static stratego.messages.MsgType.NEW_GAME;

/**
 * @author Daniel Bonnin
 */
public class NewGameMessage extends Message {
    public NewGameMessage(GameSettings gs){
        super(NEW_GAME, gs.toJson());
    }

    public GameSettings getObj() {
        Gson gson = new Gson();
        return gson.fromJson(this.jsonObj, GameSettings.class);
    }
}
