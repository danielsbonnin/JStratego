package stratego.players;

import org.codehaus.groovy.control.messages.Message;
import stratego.game.Game;
import stratego.game.Move;
import stratego.board.BoardCoords;
import stratego.messages.MoveMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static stratego.messages.MsgType.MOVE;

/**
 * @author Daniel Bonnin
 */
public class RemotePlayer implements IOpponent, Runnable{
    Game game;
    String ipAddr;
    int gameWidth;
    int gameHeight;
    int portno;

    public RemotePlayer(Game game)  {
        this.game = game;
        this.ipAddr = "localhost";
        this.portno = 9031;
        this.gameWidth = this.game.getBoard().getWidth();
        this.gameHeight = this.game.getBoard().getHeight();
        //new Thread(this).start();
    }


    public void run() {
        System.out.println("connecting to opponent");
        String serverAddress = "localhost";
        while (true) {
            try {
                for (int i = 0; i < 5; i++) {
                    try {Thread.sleep(1000);} catch(Exception e) {}
                    Socket s = new Socket(serverAddress, 9031);
                    BufferedReader input =
                            new BufferedReader(new InputStreamReader(s.getInputStream()));
                    System.out.println("On P2's computer: " + input.readLine());
                }
            } catch (IOException e) {
                System.out.println("Failed to connect");
                try {
                    Thread.sleep(3000);
                } catch (Exception exception) {
                }
                System.out.println("Trying Again");
            }
        }
    }
    public String setup() {
        return "catdog";
    }

    /**
     * Create a mirror image of a Move for opponent's board
     * @param m the original move
     * @return the flipped move
     */
    public Move flipMove(Move m) {
        BoardCoords oldOrigin = m.getOrigin();
        BoardCoords oldDestination = m.getDestination();
        BoardCoords flippedOrigin =
                new BoardCoords(this.gameHeight - oldOrigin.r, this.gameWidth - oldOrigin.c);
        BoardCoords flippedDestination =
                new BoardCoords(this.gameHeight - oldDestination.r, this.gameWidth - oldDestination.c);
        return new Move(flippedOrigin, flippedDestination, false, false);
    }

    public void trySend(Message m) {

    }
    public Move getNextMove(Move m) {
        Move flipped = flipMove(m);
        MoveMessage toSend = new MoveMessage(MOVE, flipped);
        return flipped;
    }
}
