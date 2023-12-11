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
                playerData.setPlayerData(playerNum, inData.getX(), inData.getY(), inData.getScore(), inData.getTimeAlive(), inData.xVelocity(),
                        inData.yVelocity(), inData.getIsAlive(), inData.isJumping(), inData.isWalking(),
                        inData.isGrounded(), inData.isClimbing(), inData.isClimbingSpecial(), inData.getDirection(),
                        inData.isCycle(), true);

                // System.out.println("Player " + playerNum + ": " + playerData.getPlayerData(playerNum).getX() + ", " + playerData.getPlayerData(playerNum).getY() + ", " + "score: " + playerData.getPlayerData(playerNum).getScore() + ", " + playerData.getPlayerData(playerNum).getIsAlive() + " Walking " + playerData.getPlayerData(playerNum).isWalking()); //debugging
                //System.out.println("Player " + playerNum + ": " + playerData.getPlayerData(playerNum).getX() + ", " + playerData.getPlayerData(playerNum).getY() + ", " + playerData.getPlayerData(playerNum).getIsAlive() + " Ready: " + playerData.getPlayerData(playerNum).isReady()); //debugging

                for (int idx = 0; idx < playerData.getNumPlayers(); idx++) {
                    outData = playerData.getPlayerData(idx);
                    out.writeUnshared(outData);
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}