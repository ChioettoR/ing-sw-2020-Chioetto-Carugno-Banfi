package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.Server.ChangeObservable;

import java.io.IOException;
import java.util.ArrayList;

public class PositioningManager extends ChangeObservable {
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final StateManager stateManager;
    final int totalWorkersNumber = 2;
    int currentWorkersNumber;

    public PositioningManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void positioning(int playerID, int x, int y) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.POSITIONING))
            return;

        if(currentWorkersNumber < playersManager.getPlayersNumber()*totalWorkersNumber) {

            boolean wrongTile = checkWrongTile(x,y);
            if(wrongTile) return;

            positionWorker(x,y);

            //If all players have finished positioning
            if(currentWorkersNumber == playersManager.getPlayersNumber()*totalWorkersNumber) phaseFinishedForAllPlayers();

                //If you have finished positioning
            else if(currentWorkersNumber % totalWorkersNumber==0) phaseFinished();

                //If you have positioned the first worker only
            else notifyMessage(new MessageEvent(109, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        }
    }

    private void phaseFinishedForAllPlayers() throws IOException {
        stateManager.setGameState(GameState.SELECTING);
        playersManager.nextPlayerAndStartRound();
        for(Player p : playersManager.getNextPlayers())
            notifyMessage(new MessageEvent(115, p.getID()));
        notifyMessage(new MessageEvent(103, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
    }

    private void phaseFinished() throws IOException {
        playersManager.nextPlayer();
        for(Player p : playersManager.getNextPlayers())
            notifyMessage(new MessageEvent(114, p.getID()));
        notifyMessage(new MessageEvent(108, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
    }

    private boolean checkWrongTile(int x, int y) throws IOException {
        Tile tile = Grid.getGrid().getTile(x,y);
        if(tile == null || !tile.isEmpty()) {
            notifyMessage(new MessageEvent(408, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
            notifyMessage(new MessageEvent(110, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
            return true;
        }
        return false;
    }

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
