import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class to handle getting multiplayer data
 * @author Samuel Dauk
 */
public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final Socket otherPlayer;
    private final Level level;
    private final Data otherData;
    private final Data thisData;
    private final OutputStream outStream;
    private final ObjectOutputStream out;
    private final InputStream inStream;
    private final ObjectInputStream in;

    public Server(ServerSocket serverSocket, Socket otherPlayer, Level level, Data otherData, Data thisData) throws IOException {
        this.serverSocket = serverSocket;
        this.otherPlayer = otherPlayer;
        this.level = level;
        this.otherData = otherData;
        this.thisData = thisData;
        this.outStream = this.otherPlayer.getOutputStream();
        this.inStream = this.otherPlayer.getInputStream();
        this.out = new ObjectOutputStream(outStream);
        this.in = new ObjectInputStream(inStream);
    }

    @Override
    public void run() {
        // Receives player data from clients and sends back
        boolean playing = true;

        while(playing) {
            // will need to implement a loop that goes through each socket and gets the data for all 4 players
            // then will send each client data for other players
            try {
                otherPlayer.setKeepAlive(true);
                out.writeObject(thisData);

                Data tempData = (Data) in.readObject();

                otherData.setX(tempData.getX());
                otherData.setY(tempData.getY());
                otherData.setIsAlive(tempData.getIsAlive());
                System.out.println(otherData.getX());

                if (!otherData.getIsAlive() && !thisData.getIsAlive()) {
                    playing = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                playing = false;
            }
        }
    }
}
