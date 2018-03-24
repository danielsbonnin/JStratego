package stratego.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static stratego.messages.Constants.MS_BETWEEN_KEEPALIVES;
import static stratego.messages.MsgType.KEEPALIVE;
import static stratego.messages.MsgType.NOT_READY;

/**
 * @author Daniel Bonnin
 */
public class ToServerHandler implements Runnable {
    private Message message;
    private String ipAddr;
    private int portno;
    private boolean hasPreviouslyConnected;
    private StrategoClient sc;
    private Message keepaliveMessage;
    private static int keepaliveMS = MS_BETWEEN_KEEPALIVES;
    public ToServerHandler(String ipAddr, int portno, StrategoClient sc) {
        this.message = message;
        this.keepaliveMessage = new Message(KEEPALIVE, "[]");
        this.ipAddr = ipAddr;
        this.portno = portno;
        this.sc = sc;
        this.hasPreviouslyConnected = false;
    }

    private void poll() {
        while (!StrategoClient.getDisconnect()) {
            Message msg;
            if (StrategoClient.hasOutgoingMessage()) {
                System.out.println("tsh: sc has outgoing msg");
                msg = sc.getOutgoingMessage();
                StrategoClient.setHasOutgoingMessage(false);
            } else
                msg = this.keepaliveMessage;
            try {
                Socket socket = new Socket(this.ipAddr, this.portno);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println(msg.message());
                String response = in.readLine();
                Message serverMessage = Message.fromJson(response);
                this.sc.setIncomingMessage(serverMessage);
                if (serverMessage.getType() == NOT_READY)
                    StrategoClient.setHasIncomingMessage(false);
                else
                    StrategoClient.setHasIncomingMessage(true);
                socket.close();
                try {Thread.sleep(ToServerHandler.keepaliveMS);} catch (InterruptedException e) {}
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            if (!this.hasPreviouslyConnected) {
                this.hasPreviouslyConnected = true;
                IStrategoComms.isConnected.set(true);
            }
        }
    }

    public void run() {
        poll();
    }
}
