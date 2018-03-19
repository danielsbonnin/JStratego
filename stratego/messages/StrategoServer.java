package stratego.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Daniel Bonnin
 */
public class StrategoServer implements Runnable{
    int portno;
    boolean connected;
    boolean disconnect;
    public StrategoServer(int portno) {
        this.connected = false;
        this.disconnect = false;
        this.portno = portno;  // portno is set by user
        Thread t = new Thread(this);
        t.start();
    }

    public void listenLoop() throws IOException {
        System.out.println("Server is listening on " + this.portno);
        ServerSocket listener = new ServerSocket(this.portno);
        try {
            while (!disconnect) {
                Socket socket = listener.accept();
                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                    out.println("We are connected.");
                    while (!disconnect) {
                        String message = in.readLine();
                        out.println("msg received");
                    }
                } catch (Exception e) {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }

    public void run() {
        try {listenLoop();} catch (IOException e) {}
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = true;
    }

    public void setPortno(int portno) {
        this.portno = portno;
    }
}
