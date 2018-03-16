package stratego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static stratego.Constants.DEFAULT_PIECES;

/**
 * The type LocalPlayer.
 */
public class LocalPlayer implements Runnable{
    /**
     * The Handle.
     */
    protected String handle;
    /**
     * The Inventory.
     */
    protected PieceInventory inventory;

    private boolean isP1;

    private Game g;
    /**
     * Instantiates a new LocalPlayer.
     *
     * @param pieces the pieces
     */
    public LocalPlayer(List<PieceType> pieces, Game g) {
        this.g = g;
        this.isP1 = true;
        setupInventory(pieces);
    }

    /**
     * Instantiate a new LocalPlayer
     *
     * @param pieces
     * @param isP1
     */
    public LocalPlayer(List<PieceType> pieces, boolean isP1, Game g) {
        this.g = g;
        this.isP1 = isP1;
        setupInventory(pieces);
        //new Thread(this).start();
    }

    private void connect() throws IOException {
        ServerSocket listener = new ServerSocket(9031);
        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                    out.println("Hi, I'm Player 1.");
                } catch (Exception e) {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
    /**
     * Instantiate a socket server to receive opponent moves
     */
    public void run() {
        try {connect();} catch (Exception exc) {};
    }
    /**
     * Instantiates a new LocalPlayer.
     */
    public LocalPlayer() {
        setupInventory(DEFAULT_PIECES);
    }

    private void setupInventory(List<PieceType> pieces) {
        this.inventory = new PieceInventory(pieces);
    }

    /**
     * Sets handle.
     *
     * @param handle the handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Gets handle.
     *
     * @return the handle
     */
    public String getHandle() {
        return this.handle;
    }
}
