package stratego.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Daniel Bonnin
 */
public class StrategoClient implements Runnable {
    private String ipAddr;
    private int portno;
    private boolean disconnect;

    public StrategoClient(String ipAddr, int portno) {
        this.ipAddr = ipAddr;
        this.portno = portno;
        this.disconnect = false;
        Thread t = new Thread(this);
        t.start();
    }

    public void connect(String ipAddr, int portno) {
        System.out.println("connecting to opponent: " + ipAddr + " " + portno);
        try {
            System.out.println("Client: connecting to server");
            Socket s = new Socket(this.ipAddr, this.portno);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            try {
                System.out.print(input.readLine());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            String response;
            while (!disconnect) {
                    out.println("a keepalive message from the client.");
                    try {
                        response = input.readLine();
                        System.out.println(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {}
            }
        } catch (IOException ioe) {
            System.out.println("Problem with client socket");
            ioe.printStackTrace();
        }
    }

    public void run() {
        connect(this.ipAddr, this.portno);
    }
}
