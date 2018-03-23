package stratego.messages;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static stratego.messages.MsgType.KEEPALIVE;
import static stratego.messages.MsgType.NOT_READY;

/**
 * StrategoServer utility to process new message from client
 * @author Daniel Bonnin
 */
public class FromClientHandler implements Runnable {
    private Socket socket;
    private StrategoServer ss;
    private boolean hasPreviouslyConnected;
    public static Message notReadyMessage = new Message(NOT_READY, "[]");
    public FromClientHandler(StrategoServer ss) {
        this.ss = ss;
        this.hasPreviouslyConnected = false;
    }

    public void run() {
        System.out.println("Waiting on client");
        while (!StrategoServer.getDisconnect()) {
            try {
                this.socket = ss.serverSocket.accept();
                if (!this.hasPreviouslyConnected) {
                    this.ss.setIsConnected(true);
                    this.hasPreviouslyConnected = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Message msg;
                if (StrategoServer.getHasOutgoingMessage()) {
                    msg = ss.getOutgoingMessage();
                    StrategoServer.setHasOutgoingMessage(false);
                } else
                    msg = FromClientHandler.notReadyMessage;

                PrintWriter out =
                        new PrintWriter(this.socket.getOutputStream(), true);
                BufferedReader input =
                        new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                Message clientMessage = Message.fromJson(input.readLine());
                if (clientMessage.getType() != KEEPALIVE) {
                    ss.setHasIncomingMessage(true);
                    ss.setIncomingMessage(clientMessage);
                }
                out.println(msg.message());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}