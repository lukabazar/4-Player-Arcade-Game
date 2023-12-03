import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handler class
 */
class ClientHandler implements Runnable {
    private final Socket socket;
    private final PlayerData playerData;
    private final int playerNum;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    
    public ClientHandler(Socket socket, PlayerData playerData, int playerNum) throws IOException {
        this.socket = socket;
        this.playerData = playerData;
        this.playerNum = playerNum;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            Data inData;
            Data outData;

            out.writeInt(playerNum);
            out.flush();

            while (true) {
                inData = (Data) in.readUnshared();
                playerData.setPlayerData(playerNum, inData.getX(), inData.getY(), inData.getIsAlive());
                System.out.println("Player " + playerNum + ": " + playerData.getPlayerData(playerNum).getX() + ", " + playerData.getPlayerData(playerNum).getY() + ", " + playerData.getPlayerData(playerNum).getIsAlive()); //debugging

                for (int idx = 0; idx < playerData.getNumPlayers(); idx++) {
                    outData = playerData.getPlayerData(idx);
                    out.writeUnshared(outData);
                    out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}