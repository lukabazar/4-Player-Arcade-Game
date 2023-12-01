import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Client class to handle getting multiplayer data
 * @author Samuel Dauk
 */
public class Client implements Runnable{
    private final Socket socket;
    private final Level level;
    private final Data thisData;
    private final Data otherData;
    private final OutputStream outStream;
    private final ObjectOutputStream out;
    private final InputStream inStream;
    private final ObjectInputStream in;

    /**
     * Constructor for client class
     * @param socket client socket
     * @param level current level
     * @param thisData player data
     * @param otherData multiplayer data
     */
    public Client(Socket socket, Level level, Data thisData, Data otherData) throws IOException {
        // for 4 players may need to set player num so knows when receiving data from server, knows which data is its own
        this.socket = socket;
        this.level = level;
        this.thisData = thisData;
        this.otherData = otherData;
        this.outStream = this.socket.getOutputStream();
        this.inStream = this.socket.getInputStream();
        this.out = new ObjectOutputStream(outStream);
        this.in = new ObjectInputStream(inStream);
    }

    @Override
    public void run() {
        // Gets player data from server, sends its own
        // will need loop where receives player data for each player separately
        boolean playing = true;

        while (playing) {
            try {
                Data tempData = (Data) in.readObject();

                otherData.setX(tempData.getX());
                otherData.setY(tempData.getY());
                otherData.setIsAlive(tempData.getIsAlive());
                System.out.println(otherData.getX());
    
                out.writeObject(thisData);
                
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
