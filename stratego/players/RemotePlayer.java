package stratego.players;

import stratego.game.Game;
import stratego.messages.IStrategoComms;

/**
 * @author Daniel Bonnin
 */
public class RemotePlayer {
    Game game;
    int gameWidth;
    int gameHeight;
    private IStrategoComms comms;

    public RemotePlayer(Game game)  {
        this.game = game;
        this.gameWidth = this.game.getBoard().getWidth();
        this.gameHeight = this.game.getBoard().getHeight();
    }
}
