package stratego.messages;

import static stratego.messages.MsgType.*;

/**
 * @author Daniel Bonnin
 */
public class StartSetupMessage extends Message {
    public StartSetupMessage() {
        super(START_SETUP, "");
    }

    String message() {
        return "";
    }
    Object getObj(){return new Object();}
}
