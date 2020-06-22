package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.ChangeObservable;

import java.io.IOException;
import java.util.ArrayList;

public class PositioningManager extends ChangeObservable {
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final StateManager stateManager;
    private int currentWorkersNumber;

    public PositioningManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    /**
     * This method allows the player to position his workers
     * @param playerID id of the player
     * @param x X axis of the grid
     * @param y Y axis of the grid
     * @throws IOException when socket closes
     */
    public void positioning(int playerID, int x, int y) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.POSITIONING))
            return;

        int totalWorkersNumber = 2;
        if(currentWorkersNumber < playersManager.getPlayersNumber()* totalWorkersNumber) {

            boolean wrongTile = checkWrongTile(x,y);
            if(wrongTile) return;

            positionWorker(x,y);

            //If all players have finished positioning
            if(currentWorkersNumber == playersManager.getPlayersNumber()* totalWorkersNumber) phaseFinishedForAllPlayers();

                //If you have finished positioning
            else if(currentWorkersNumber % totalWorkersNumber ==0) phaseFinished();

                //If you have positioned the first worker only
            else notifyMessage(new MessageEvent(109, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        }
    }

    /**
     * Invoked when Position Phase ends, prepares the grid and the players to the game
     * @throws IOException when socket closes
     */
    private void phaseFinishedForAllPlayers() throws IOException {
        stateManager.setGameState(GameState.SELECTING);
        playersManager.nextPlayerAndStartRound();
        for(Player p : playersManager.getNextPlayers())
            notifyMessage(new MessageEvent(115, p.getID()));
        notifyMessage(new MessageEvent(103, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
    }

    /**
     * Invoked when only a player finished positioning
     * @throws IOException when socket closes
     */
    private void phaseFinished() throws IOException {
        playersManager.nextPlayer();
        for(Player p : playersManager.getNextPlayers())
            notifyMessage(new MessageEvent(114, p.getID()));
        notifyMessage(new MessageEvent(108, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
    }

    /**
     * Checks if the tile is null or full
     * @return true if wrong, false otherwise
     * @throws IOException when socket closes
     */
    private boolean checkWrongTile(int x, int y) throws IOException {
        Tile tile = Grid.getGrid().getTile(x,y);
        if(tile == null || !tile.isEmpty()) {
            notifyMessage(new MessageEvent(408, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
            notifyMessage(new MessageEvent(110, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
            return true;
        }
        return false;
    }

    /**
     * This method creates and positions a new worker on the grid
     * @throws IOException when socket closes
     */
    private void positionWorker(int x, int y) throws IOException {
        Tile tile = Grid.getGrid().getTile(x,y);
        Worker worker = new Worker();
        worker.setPosition(tile);
        tile.setWorker(worker);
        playersManager.getCurrentPlayer().setWorker(worker);
        int workerID = worker.getLocalID();
        TileSimplified tileSimplified = new TileSimplified(x, y, 0, new WorkerSimplified(playersManager.getCurrentPlayer().getName(), workerID));
        ArrayList<TileSimplified> arrayList = new ArrayList<>();
        arrayList.add(tileSimplified);
        notify(new ChangeEvent(arrayList));
        currentWorkersNumber++;
    }
}
