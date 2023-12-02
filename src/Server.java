import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class
 */
public class Server {
    public static void main(String[] args) {
        int port = 8000;
        int playerNum = 0;
        PlayerData playerData = new PlayerData();
        
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client);

                playerData.addPlayer(10, 10, true); // set to default spawn location based on player number
                
                ClientHandler handler = new ClientHandler(client, playerData, playerNum);
                Thread thread = new Thread(handler);
                thread.start();

                playerNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
