import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private final List<Data> playerData;

    public PlayerData() {
        playerData = new ArrayList<>();
    }

    public Data getPlayerData(int player) {
        return playerData.get(player);
    }

    public void addPlayer(double x, double y, boolean isAlive) {
        playerData.add(new Data(x, y, isAlive));
    }

    public void setPlayerData(int player, double x, double y, boolean isAlive) {
        playerData.get(player).setX(x);
        playerData.get(player).setY(y);
        playerData.get(player).setIsAlive(isAlive);
    }

    public int getNumPlayers() {
        return playerData.size();
    }
}
