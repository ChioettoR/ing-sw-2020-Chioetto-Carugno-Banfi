package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Player {
    private int ID = -1;
    private String name;
    private Card card;
    private ArrayList<Worker> workers;

    public Player(String name) {
        workers = new ArrayList<Worker>();
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {this.ID = ID;}

    public String getName(){
        return name;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Player resets values of his Action classes to the original one at the end of his round
     * Sets can move up to true
     */
    public void resetActionsValues() {
        getCard().getMoveActions().forEach(moveAction -> moveAction.setCantMoveUp(false));
        getCard().getBuildActions().forEach(buildAction -> buildAction.setActionLock(false));
    }

    /**
     * Removes a worker from the game
     * @param workerToDelete The Worker you want to delete from the game
     */
    public void deleteWorker(Worker workerToDelete){
        if(workerToDelete==null)
            System.out.println("The worker you want to delete is null");

        else if(workerToDelete.getPlayerID()!=ID)
            System.out.println("The worker you want to delete is not controlled by this player");

        else if(!workers.contains(workerToDelete))
            System.out.println("The worker you want to delete is not in this list");

        else
            workers.remove(workerToDelete);
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    /**
     * Adds a new worker to the list of workers of this player
     * @param worker The worker this player will control
     */
    public void setWorker(Worker worker) {
        for(Worker w : workers) {
            if (workers.contains(worker)) {
                System.out.println("You're adding a worker already in the workers list");
                return;
            }
        }
        worker.setPlayerID(ID);
        workers.add(worker);
    }
}

