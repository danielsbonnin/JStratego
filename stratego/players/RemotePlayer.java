package stratego.players;

import stratego.Game;
import stratego.Move;
import stratego.board.BoardCoords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Daniel Bonnin
 */
public class RemotePlayer implements IOpponent, Runnable{
    Game g;
    public RemotePlayer(Game g)  {
        this.g = g;
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

    public Move getNextMove(Move m) {
        return new Move(new BoardCoords(1, 1), new BoardCoords(2, 2), false, false);
    }
}
