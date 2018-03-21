package stratego.messages;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * StrategoServer utility to process new message from client
 * @author Daniel Bonnin
 */
public class FromClientHandler implements Runnable {
    private Socket socket;
    private Message newMessage;
    private String rawMessageString;
    public FromClientHandler(Socket newAccept) {
        this.socket = newAccept;
        System.out.println("fromclienthandler");
    }

    /**
     * initialize newMessage from json
     */
    private void parseMessage() {
        Gson gson = new Gson();
        this.newMessage = gson.fromJson(this.rawMessageString, Message.class);
        System.out.println("Received a " + this.newMessage.getType().toString() + " message.");
        switch (this.newMessage.getType()) {
            
        }
    }

    public void run() {
        try {
            PrintWriter out =
                    new PrintWriter(this.socket.getOutputStream(), true);
            out.println("Hello");
            BufferedReader input =
                new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.rawMessageString = input.readLine();
            out.println("recvd");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        parseMessage();
    }
}