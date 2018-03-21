package stratego.messages;

import stratego.board.BoardCoords;
import stratego.game.Move;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static stratego.messages.MsgType.MOVE;

/**
 * @author Daniel Bonnin
 */
public class ToServerHandler implements Runnable {
    private String responseJson;
    private Message message;
    private String ipAddr;
    private int portno;
    public ToServerHandler(Message message, String ipAddr, int portno) {
        this.message = message;
        this.ipAddr = ipAddr;
        this.portno = portno;
    }

    public void connect() {
        System.out.println("connecting to opponent: " + ipAddr + " " + portno);
        try {
            System.out.println("Client: connecting to server");
            Socket s = new Socket(this.ipAddr, this.portno);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("Client: waiting on server response");
            try {
                System.out.print(input.readLine());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            out.println(this.message.message());
            System.out.println("closing connection");
            s.close();
        } catch (IOException ioe) {
            System.out.println("Problem with client socket");
            ioe.printStackTrace();
        }
    }

    public void run() {
        connect();
    }
}
