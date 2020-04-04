package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class PlayersManager {

    private ArrayList<Player> players;
    private IDManager idManager;
    private int currentPlayerIndex;
    private int nextPlayerIndex;
    private static PlayersManager playersManager;
    private Worker currentWorker;
    //private ActionOrder currentActionOrder;
    WinManager winManager = new WinManager();

    private PlayersManager() {
        idManager = new IDManager();
        players = new ArrayList<Player>();
        playersManager = this;
    }

    public static PlayersManager getPlayersManager() {
        if(playersManager==null)
            playersManager = new PlayersManager();
        return  playersManager;
    }

    /**
     * If the player is valid then add the player to the players list and assign him a uniqueID.
     * The position in the list will determine the round order of the players
     * @param player The logged player you want to add to the game
     */
    public void addPlayer(Player player) {
        if(player==null)
            System.out.println("You're adding a null player");

        else if(validatePlayer(player)) {
            if(getPlayersNumber()==0)
                currentPlayerIndex=0;
            player.setID(idManager.pickID());
            players.add(player);
            if(getPlayersNumber()==2)
                nextPlayerIndex=1;
        }
    }

    public Player getPlayerWithID(int ID) {
        for(Player p : getPlayers()) {
            if(p.getID()==ID)
                return p;
        }
        System.out.println("There are no players with that ID");
        return null;
    }

    /**
     * Verify if the player you want to add has an original name
     * @param player The player to validate
     * @return The result of the validation
     */
    private boolean validatePlayer(Player player) {
        for(Player p : players) {
            if(p.getName()==null) {
                System.out.println("You're adding a player with a null name");
                return false;
            }

            if(p.getName().equals(player.getName())) {
                System.out.println("You're adding a player with an existing name");
                return false;
            }
        }
        return true;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorker(Worker worker) {
        this.currentWorker = worker;
    }

    /**
     * Remove the current player from the game
     */
    public void deletePlayer(Player player) {
        if(player ==null)
            System.out.println("The currentPlayer to delete is null");

        else if(!players.contains(player))
            System.out.println("The currentPlayer to delete is not in the list of players");

        else {
            players.remove(player);
            nextPlayerIndex = increaseIndex(currentPlayerIndex - 1);
            currentPlayerIndex = -1;
            if(getPlayersNumber() == 1){
                winManager.win(getPlayers().get(0));
            }
        }
    }

    public void deleteCurrentPlayer() {
        deletePlayer(getCurrentPlayer());
    }

    /**
     * The current round has ended
     * @return The next player on the list
     */
    public Player getNextPlayerAndStartRound() {
        if(getPlayersNumber()==0) {
            System.out.println("There are no players left in the game");
            return null;
        }
        currentPlayerIndex = nextPlayerIndex;
        int nextPlayerIndexCopy = nextPlayerIndex;
        nextPlayerIndex = increaseIndex(nextPlayerIndex);
        return players.get(nextPlayerIndexCopy);
    }

    private int increaseIndex(int index) {
        index++;
        if(index>=getPlayersNumber())
            index=0;
        return index;
    }

    /**
     * Delete a worker from the game
     * @param worker The worker you want to delete from the game
     */
    public void deleteWorker(Worker worker) {
        for(Player p : players) {
            if(p.getID()==worker.getPlayerID()) {
                Tile workerPosition = worker.getPosition();
                if(workerPosition==null) {
                    System.out.println("The worker is not in the grid");
                    return;
                }
                worker.getPosition().setEmpty(true);
                p.deleteWorker(worker);
                if(p.getWorkers().size() == 0) {
                    deletePlayer(p);
                }
                return;
            }
        }
        System.out.println("Can't find a player which controls this worker");
    }

    public void deleteCurrentWorker() {
        deleteWorker(getCurrentWorker());
    }

    public int getPlayersNumber() {
        return players.size();
    }

/*
    public void setActionOrder(ActionOrder actionOrder){
        currentActionOrder = actionOrder;
    }

    public ActionOrder getActionOrder() {
        return currentActionOrder;
    }
*/

    /**
     * @return List of all players except the current one
     */
    public ArrayList<Player> getNextPlayers() {
        ArrayList<Player> playersCopy = getPlayers();
        playersCopy.remove(currentPlayerIndex);
        return playersCopy;
    }

    /**
     * @return List of all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        if(currentPlayerIndex==-1) {
            System.out.println("Enable to retrieve the current player. It has been deleted from the game");
            return null;
        }
        return players.get(currentPlayerIndex);
    }

    /**
     * Search player with the given card
     * @param card The card that the wanted player holds
     * @return The searched player
     */
    public Player getPlayerWithCard(Card card) {
        for (Player p : players) {
            if (p.getCard() == card)
                return p;
        }
        System.out.println("There are no players with that card");
        return null;
    }
}

