package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.LoseEvent;
import it.polimi.ingsw.Observer.Server.MessageObservable;

import java.io.IOException;
import java.util.ArrayList;

public class PlayersManager extends MessageObservable {

    private final ArrayList<Player> players = new ArrayList<>();
    private IDManager idManager = new IDManager();
    private int currentPlayerIndex;
    private int nextPlayerIndex;
    private static PlayersManager playersManager;
    private Worker currentWorker;
    private ArrayList<Action> currentActionOrder = new ArrayList<>();
    private int playerWinnerID = -1;

    private PlayersManager() {
        playersManager = this;
    }

    public static PlayersManager getPlayersManager() {
        if(playersManager==null)
            playersManager = new PlayersManager();
        return  playersManager;
    }

    public void reset() {
        idManager = new IDManager();
        players.clear();
        playerWinnerID = -1;
        currentPlayerIndex = 0;
        nextPlayerIndex = 0;
        currentWorker = null;
        currentActionOrder.clear();
    }

    public void setNextPlayerIndex(int nextPlayerIndex) {
        this.nextPlayerIndex = nextPlayerIndex;
    }

    public int getPlayerWinnerID() {
        return playerWinnerID;
    }

    public void setPlayerWinnerID(int playerWinnerID) {
        this.playerWinnerID = playerWinnerID;
    }

    public void addPlayer(Player player) {
        if(getPlayersNumber()==0) {
            currentPlayerIndex = 0;
            nextPlayerIndex = 0;
        }
        player.setID(idManager.pickID());
        players.add(player);
    }

    public Player getPlayerWithID(int ID) {
        for(Player p : getPlayers()) {
            if(p.getID()==ID)
                return p;
        }
        System.out.println("There are no players with that ID");
        return null;
    }

    public Worker getWorkerWithID(int playerID, int localID) {
        for(Player p : getPlayers()) {
            if(p.getID()==playerID) {
                for(Worker w : p.getWorkers()) {
                    if(w.getLocalID()==localID) {
                        return w;
                    }
                }
            }
        }
        System.out.println("There are no workers or players with that ID");
        return null;
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
        if(player == null)
            System.out.println("The currentPlayer to delete is null");

        else if(!players.contains(player))
            System.out.println("The currentPlayer to delete is not in the list of players");

        else {
            players.remove(player);
            if(player.getID()<=currentPlayerIndex) nextPlayerIndex = increaseIndex(currentPlayerIndex - 1);
            else nextPlayerIndex = increaseIndex(currentPlayerIndex);
            currentPlayerIndex = -1;
            if(getPlayersNumber() == 1){
                winPlayer(getPlayers().get(0));
            }
        }
    }

    public void deleteCurrentPlayer() {
        deletePlayer(getCurrentPlayer());
    }

    /**
     * The current round has ended
     */
    public Player nextPlayerAndStartRound() {
        if(currentPlayerIndex!=-1)
            players.get(currentPlayerIndex).resetActionsValues();
        if(nextPlayer()==null)
            return null;
        currentActionOrder = players.get(currentPlayerIndex).getCard().getActionOrder();
        return players.get(currentPlayerIndex);
    }

    public Player nextPlayer() {
        if(getPlayersNumber()==0) {
            System.out.println("There are no players left in the game");
            return null;
        }
        currentPlayerIndex = nextPlayerIndex;
        nextPlayerIndex = increaseIndex(nextPlayerIndex);
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
    public void deleteWorker(Worker worker) throws IOException {
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
                    notifyLose(new LoseEvent(p.getID()));
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
    public void deleteCurrentWorker() throws IOException {
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
        ArrayList<Player> playersCopy = new ArrayList<>(getPlayers());
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

    public void winCurrentPlayer() {
        winPlayer(getCurrentPlayer());
    }

    public void winPlayer(Player player) {
        playerWinnerID = player.getID();
    }
}

