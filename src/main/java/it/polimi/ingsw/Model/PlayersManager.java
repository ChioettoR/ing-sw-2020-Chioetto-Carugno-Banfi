package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Collections;

public class PlayersManager {

    private ArrayList<Player> players;
    private IDManager idManager;
    private int currentPlayerIndex;
    private int nextPlayerIndex;
    private static PlayersManager playersManager;
    private Worker currentWorker;
    private ArrayList<Action> currentActionOrder = new ArrayList<Action>();
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
     * If the player is valid then adds the player to the players list and assigns him a uniqueID.
     * The position in the list will determine the round order of the players
     * @param player The logged player you want to add to the game
     */
    public void addPlayer(Player player) {
        if(player==null)
            System.out.println("You're adding a null player");

        else if(validatePlayer(player)) {
            if(getPlayersNumber()==0) {
                currentPlayerIndex = 0;
                nextPlayerIndex = 0;
            }
            player.setID(idManager.pickID());
            players.add(player);
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
     * Verifies if the player you want to add has an original name
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
     * Removes the current player from the game and sets current player int to -1
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
        int currentPlayerIndexCopy;

        if(currentPlayerIndex!=-1)
            players.get(currentPlayerIndex).resetActionsValues();

        currentPlayerIndex = nextPlayerIndex;
        int nextPlayerIndexCopy = nextPlayerIndex;
        nextPlayerIndex = increaseIndex(nextPlayerIndex);
        currentActionOrder = players.get(currentPlayerIndex).getCard().getActionOrder();
        return players.get(currentPlayerIndex);
    }

    /**
     * Increases the index of a circular List
     * @param index The current index
     * @return The increased index
     */
    private int increaseIndex(int index) {
        index++;
        if(index>=getPlayersNumber())
            index=0;
        return index;
    }

    /**
     * Deletes a worker from the game
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

    /**
     * Deletes the selected worker from the game
     */
    public void deleteCurrentWorker() {
        deleteWorker(getCurrentWorker());
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public ArrayList<Action> getActionOrder() {
        return currentActionOrder;
    }

    /**
     * @return List of all players except the current one
     */
    public ArrayList<Player> getNextPlayers() {
        ArrayList<Player> playersCopy = new ArrayList<Player>(getPlayers());
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
     * Searches a player with the given card
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

