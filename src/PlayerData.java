import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class PlayerData {
    private final List<Data> playerData;
    private boolean haveDied = false;

    private final int FIRST_BONUS = 5000;
    private final int SECOND_BONUS = 2000;
    private final int THIRD_BONUS = 500;

    public PlayerData() {
        playerData = new ArrayList<>();
    }

    public Data getPlayerData(int player) {
        return playerData.get(player);
    }

    public void addPlayer(double x, double y, int score, boolean isAlive) {
        playerData.add(new Data(x, y, score,  isAlive));
    }

    public void setPlayerData(int player, double x, double y, int score, long timeAlive, double xVel, double yVel, boolean isAlive, boolean isJumping,
                              boolean isWalking, boolean isGrounded, boolean isClimbing, boolean isClimbingSpecial,
                              int direction, boolean isCycle, boolean isReady) {

        // Only update key values when player is alive 
        if(isAlive){
            playerData.get(player).setX(x);
            playerData.get(player).setY(y);
            playerData.get(player).setScore(score);
            playerData.get(player).setTimeAlive(timeAlive);
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

        // calculate final standing scores after all players are dead 
        sortDeathOrder();

        return true;
        // System.out.println("--- All players are dead ---");
        // System.out.println("----------------------------");
        // if(!playerData.get(0).getIsAlive() && !playerData.get(1).getIsAlive() && !playerData.get(2).getIsAlive() && !playerData.get(3).getIsAlive()){
        //     return true;
        // }else{
        //     return false;
        // }
    }

    private void sortDeathOrder() {
        long[] timing = new long[3];
        int[] order = new int[3];
        for (int i = 0; i < 3; i++) {
            order[i] = i;
            timing[i] = playerData.get(i).getTimeAlive();
        }
    
        // Sort in reverse order
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2 - i; j++) {
                if (timing[j] < timing[j + 1]) {
                    long tempTime = timing[j];
                    timing[j] = timing[j + 1];
                    timing[j + 1] = tempTime;
    
                    int tempId = order[j];
                    order[j] = order[j + 1];
                    order[j + 1] = tempId;
                }
            }
        }
    
        // give bonus points 
        setScore(order[0], getScore(order[0]) + FIRST_BONUS);
        setScore(order[1], getScore(order[1]) + SECOND_BONUS);
        setScore(order[2], getScore(order[2]) + THIRD_BONUS);

        // Print the things 
        System.out.println("----------------------------");
        System.out.println("First Place: " + order[0]);
        System.out.println("Time Alive: " + getPlayerData(order[0]).getTimeAlive());
        System.out.println("---");
        System.out.println("Second Place: " + order[1]);
        System.out.println("Time Alive: " + getPlayerData(order[1]).getTimeAlive());
        System.out.println("---");
        System.out.println("Third Place: " + order[2]);
        System.out.println("Time Alive: " + getPlayerData(order[2]).getTimeAlive());
        System.out.println("----------------------------");
    }
    
}
