package stratego.players;

import stratego.game.Move;
import stratego.messages.Message;

/**
 * This interface represents the actions of a Stratego Player
 * @author Daniel Bonnin
 */
public interface IOpponent {
    /**
     * Process a Message and return next Message
     * @return a new Message
     */
    Message getNextMessage(Message m);
}
