package stratego.messages;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static stratego.messages.MsgType.NOT_READY;
import static stratego.messages.MsgType.START_SETUP;


/**
 * @author Daniel Bonnin
 */
public class StrategoServer implements IStrategoComms{
    private int portno;
    public ServerSocket serverSocket;
    private static final AtomicBoolean disconnect = new AtomicBoolean(false);
    private static final AtomicBoolean hasOutgoingMessage = new AtomicBoolean(false);
    private static final AtomicBoolean hasIncomingMessage = new AtomicBoolean(false);
    //public static BooleanProperty isConnected = new SimpleBooleanProperty(false);
    private Message incomingMessage;
    private Message outgoingMessage;
    public StrategoServer(int portno) {
        this.portno = portno;  // portno is set by user
        try {
            this.serverSocket = new ServerSocket(this.portno);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(new FromClientHandler(this));
        t.start();
    }

    public void listen() throws IOException {
        while (!StrategoServer.disconnect.get()) {
            Socket socket = serverSocket.accept();
            System.out.println("server got a connection attempt");
        }
        this.serverSocket.close();
    }

    public boolean isConnected() {
        return this.isConnected.get();
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected.set(isConnected);
    }
    public static void setDisconnect(boolean disconnect) {
        StrategoServer.disconnect.set(true);
    }

    public static boolean getDisconnect() {
        return StrategoServer.disconnect.get();
    }

    public void setPortno(int portno) {
        this.portno = portno;
    }

    public void setIncomingMessage(Message incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public Message getIncomingMessage() {
        return this.incomingMessage;
    }

    public void setHasIncomingMessage(boolean hasIncomingMessage) {
        StrategoServer.hasIncomingMessage.set(hasIncomingMessage);
        IStrategoComms.hasIncoming.set(hasIncomingMessage);
    }

    public boolean hasIncomingMessage() {
        return StrategoServer.hasIncomingMessage.get();
    }

    public void sendMessage(Message m) {
        this.outgoingMessage = m;
        StrategoServer.hasOutgoingMessage.set(true);
    }

    public static void setHasOutgoingMessage(boolean hasOutgoingMessage) {
        StrategoServer.hasOutgoingMessage.set(hasOutgoingMessage);
    }

    public static boolean getHasOutgoingMessage() {
        return StrategoServer.hasOutgoingMessage.get();
    }

    public Message getOutgoingMessage() {
        return this.outgoingMessage;
    }
}
