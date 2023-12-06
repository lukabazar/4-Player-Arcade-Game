import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client implements Runnable {
    private final String hostname;
    private final int port;
    private final PlayerData playerData;

    private int playerNum;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean playing;

    public Client(String hostname, int port, PlayerData playerData) {
        this.hostname = hostname;
        this.port = port;
        this.playerData = playerData;

        playing = true;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void stopClient() {
        playing = false;
    }

    @Override
    public void run() {
        Data inData;
        Data outData;

        try (Socket socket = new Socket(hostname, port)) {
            socket.setSoTimeout(250);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            playerNum = in.readInt();
            System.out.println("Player: " + playerNum);

            while (playing) {
                outData = playerData.getPlayerData(playerNum);
                out.writeUnshared(outData);
                out.flush();

                for (int idx = 0; idx < playerData.getNumPlayers(); idx++) {
                    try {
                        inData = (Data) in.readUnshared();

                        if (idx != playerNum) {
                            playerData.setPlayerData(idx, inData.getX(), inData.getY(), inData.getScore(), inData.getIsAlive());
                            System.out.println("Player " + idx + ": " + inData.getX() + ", " + inData.getY() + ", " + inData.getIsAlive() + ", " + inData.getScore());
                        }

                    } catch (SocketTimeoutException e) {
                        idx = 4;
                    }
                }
            }

            System.out.println("Disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
