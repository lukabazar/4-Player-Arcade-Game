import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private final List<Data> playerData;
    private boolean haveDied = false;

    public PlayerData() {
        playerData = new ArrayList<>();
    }

    public Data getPlayerData(int player) {
        return playerData.get(player);
    }

    public void addPlayer(double x, double y, int score, boolean isAlive) {
        playerData.add(new Data(x, y, score,  isAlive));
    }

    public void setPlayerData(int player, double x, double y, int score, double xVel, double yVel, boolean isAlive, boolean isJumping,
                              boolean isWalking, boolean isGrounded, boolean isClimbing, boolean isClimbingSpecial,
                              int direction, boolean isCycle, boolean isReady) {

        // Only update key values when player is alive 
        if(isAlive){
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
            playerData.get(player).setReady(isReady);

        }else if(!haveDied){
            haveDied = true;    // make sure we only add to deathOrder once

            System.out.println("adding player " + player + " to death order. in playerData.setPlayerData()");
            putInDeathOrder(player);
        }
    }

    public int getNumPlayers() {
        return playerData.size();
    }

    public void setScore(int player, int score){
        playerData.get(player).setScore(score);
    }

    private void putInDeathOrder(int player) {
        // Only add to the deathOrder list once per player
        for(int i=0; i<3; i++){
            
            System.out.println("Adding Player: " + player + "to the death list for player: " + i);

            playerData.get(i).addDeathOrder(player);    // add to each player's daeth order
        }
    }


    public int getScore(int player){
        return playerData.get(player).getScore();
    }

    public boolean allPlayersDead(){
        // System.out.println("----------------------------");
        for(int i = 0; i < getNumPlayers(); i++){
            if(playerData.get(i).getIsAlive()){
                // System.out.println("Player: " + i + " is alive");
                return false;
            }
        }
        return true;
        // System.out.println("--- All players are dead ---");
        // System.out.println("----------------------------");
        // if(!playerData.get(0).getIsAlive() && !playerData.get(1).getIsAlive() && !playerData.get(2).getIsAlive() && !playerData.get(3).getIsAlive()){
        //     return true;
        // }else{
        //     return false;
        // }
    }
}
