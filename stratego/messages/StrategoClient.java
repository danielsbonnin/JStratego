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
public class StrategoClient {
    private String ipAddr;
    private int portno;
    public static boolean disconnect;

    public StrategoClient(String ipAddr, int portno) {
        this.ipAddr = ipAddr;
        this.portno = portno;
        StrategoClient.disconnect = false;
    }

    public void sendMessage(Message message) {
        ToServerHandler tsh = new ToServerHandler(message, this.ipAddr, this.portno);
        Thread t = new Thread(tsh);
        t.start();
    }
}
