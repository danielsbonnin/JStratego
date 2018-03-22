package stratego.messages;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Daniel Bonnin
 */
public interface IStrategoComms {
    public static BooleanProperty isConnected = new SimpleBooleanProperty(false);
    public static BooleanProperty hasIncoming = new SimpleBooleanProperty(false);
    public boolean isConnected();
    public void sendMessage(Message m);
    public Message getIncomingMessage();
    public boolean hasIncomingMessage();
}
