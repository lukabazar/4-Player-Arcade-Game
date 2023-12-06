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

    public void setPlayerData(int player, double x, double y, double xVel, double yVel, boolean isAlive, boolean isJumping,
                              boolean isWalking, boolean isGrounded, boolean isClimbing, boolean isClimbingSpecial,
                              int direction, boolean isCycle) {
        playerData.get(player).setX(x);
        playerData.get(player).setY(y);
        playerData.get(player).setXVelocity(xVel);
        playerData.get(player).setYVelocity(yVel);
        playerData.get(player).setIsAlive(isAlive);
        playerData.get(player).setJumping(isJumping);
        playerData.get(player).setWalking(isWalking);
        playerData.get(player).setGrounded(isGrounded);
        playerData.get(player).setClimbing(isClimbing);
        playerData.get(player).setClimbingSpecial(isClimbingSpecial);
        playerData.get(player).setCycle(isCycle);
        playerData.get(player).setDirection(direction);
    }

    public int getNumPlayers() {
        return playerData.size();
    }
}
