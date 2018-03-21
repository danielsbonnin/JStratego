package stratego.messages;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Daniel Bonnin
 */
public class StrategoServer {
    private int portno;
    private ServerSocket serverSocket;
    private boolean disconnect;
    public StrategoServer(int portno) {
        System.out.println("Server initializing");
        this.portno = portno;  // portno is set by user
        this.disconnect = false;
        try {
            this.serverSocket = new ServerSocket(this.portno);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {listen();}catch(IOException e) {}
    }

    public void listen() throws IOException {
        System.out.println("Server is listening on " + this.portno);
        while (!this.disconnect) {
            Socket socket = serverSocket.accept();
            System.out.println("client connected");
            Thread t = new Thread(new FromClientHandler(socket));
            t.start();
        }
        this.serverSocket.close();
    }

    public void run() {
        try {listen();} catch (IOException e) {}
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = true;
    }

    public void setPortno(int portno) {
        this.portno = portno;
    }
}
