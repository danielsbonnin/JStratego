package stratego.messages;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import stratego.board.BoardCoords;
import stratego.game.Move;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static stratego.messages.MsgType.KEEPALIVE;
import static stratego.messages.MsgType.MOVE;

/**
 * @author Daniel Bonnin
 */
public class StrategoClient implements IStrategoComms {
    private String ipAddr;
    private int portno;
    private static final AtomicBoolean disconnect = new AtomicBoolean(false);
    private static final AtomicBoolean hasOutgoingMessage = new AtomicBoolean(false);
    private static final AtomicBoolean hasIncomingMessage = new AtomicBoolean(false);
    private static Message incomingMessage;
    private static Message outgoingMessage;
    //public static BooleanProperty isConnected = new SimpleBooleanProperty(false);

    public StrategoClient(String ipAddr, int portno) {
        System.out.println("StrategoClient instantiated");
        this.ipAddr = ipAddr;
        this.portno = portno;
        StrategoClient.disconnect.set(false);
        Thread toServerHandler = new Thread(new ToServerHandler(this.ipAddr, this.portno, this));
        toServerHandler.start();
        //try {toServerHandler.join();} catch (InterruptedException e) {e.printStackTrace();}
    }

    public boolean isConnected() {
        return this.isConnected.get();
    }

    public void setIsConnected(boolean isConnected) {
        IStrategoComms.isConnected.set(isConnected);
    }

    public void sendMessage(Message message) {
        StrategoClient.outgoingMessage = message;
        StrategoClient.setHasOutgoingMessage(true);
        System.out.println("Send message method");
    }

    public boolean hasIncomingMessage() {
        return StrategoClient.hasIncomingMessage.get();
    }

    public Message getIncomingMessage() {
        return StrategoClient.incomingMessage;
    }

    public void setIncomingMessage(Message incomingMessage) {
        StrategoClient.incomingMessage = incomingMessage;
    }

    public static void setHasIncomingMessage(boolean hasIncomingMessage) {
        StrategoClient.hasIncomingMessage.set(hasIncomingMessage);
        IStrategoComms.hasIncoming.set(hasIncomingMessage);
    }

    public static boolean hasOutgoingMessage() {
        return StrategoClient.hasOutgoingMessage.get();
    }

    public static void setHasOutgoingMessage(boolean hasOutgoingMessage) {
        StrategoClient.hasOutgoingMessage.set(hasOutgoingMessage);
    }

    public static Message getOutgoingMessage() {
        return StrategoClient.outgoingMessage;
    }
    public static boolean getDisconnect() {
        return StrategoClient.disconnect.get();
    }

    public static void setDisconnect(boolean disconnect) {
        StrategoClient.disconnect.set(disconnect);
    }
}
