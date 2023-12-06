import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Server class
 */
public class Server {
    private static Map<Integer, List<Integer>> standings = new HashMap<>();
    private int[] placement = {0, 1, 2, 3};
    public static void main(String[] args) {
        int port = 8000;
        int playerNum = 0;
        PlayerData playerData = new PlayerData();
        
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client);

                playerData.addPlayer(0, 0, 0,  true); // set to default spawn location based on player number
                
                ClientHandler handler = new ClientHandler(client, playerData, playerNum);
                Thread thread = new Thread(handler);
                thread.start();

                playerNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Update standings based off of death order
     */
    private static void updateStandings(int[] placement, PlayerData playerData){
        /*
         * 1st -> 2,000
         * 2nd -> 750 
         * 3rd -> 100
         */
        
        // pull death order & assign placement points 
        // First place points 
        
    }
}
