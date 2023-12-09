import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private final List<Data> playerData;
    private final List<Integer> deathOrder;

    public PlayerData() {
        playerData = new ArrayList<>();
        deathOrder = new ArrayList<>();
    }

    public Data getPlayerData(int player) {
        return playerData.get(player);
    }

    public Data getDeathData(int player){
        return playerData.get(deathOrder.get(player));
    }

    public void addPlayer(double x, double y, int score, boolean isAlive) {
        playerData.add(new Data(x, y, score,  isAlive));
    }

    public void setPlayerData(int player, double x, double y, int score, double xVel, double yVel, boolean isAlive, boolean isJumping,
                              boolean isWalking, boolean isGrounded, boolean isClimbing, boolean isClimbingSpecial,
                              int direction, boolean isCycle) {
        playerData.get(player).setX(x);
        playerData.get(player).setY(y);
        playerData.get(player).setScore(score);
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

    public List<Integer> getDeathOrder(){
        return deathOrder;
    }

    public void setScore(int score){
        playerData.get(0).setScore(score);
    }

    public int getScore(){
        return playerData.get(0).getScore();
    }
}
