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

            Tile tile = Grid.getGrid().getTile(x,y);
            if(tile == null || !tile.isEmpty()) {
                notifyError(new ErrorEvent("Invalid tile", playersManager.getCurrentPlayer().getID()));
                notifyRequest(new RequestEvent("Select another tile", playersManager.getCurrentPlayer().getID()));
                return;
            }
            Worker worker = new Worker();
            worker.setPosition(tile);
            tile.setWorker(worker);
            playersManager.getCurrentPlayer().setWorker(worker);
            int workerID = worker.getLocalID();
            TileSimplified tileSimplified = new TileSimplified(x, y, 0, new WorkerSimplified(playersManager.getCurrentPlayer().getID(), workerID));
            ArrayList<TileSimplified> arrayList = new ArrayList<>();
            arrayList.add(tileSimplified);
            notify(new ChangeEvent(arrayList));
            currentWorkersNumber++;

            //If all players have finished positioning
            if(currentWorkersNumber == playersManager.getPlayersNumber()*totalWorkersNumber) {
                stateManager.setGameState(GameState.SELECTING);
                playersManager.nextPlayerAndStartRound();
                notifyAllMessage(new AllMessageEvent("The round is started"));
                notifyRequest(new RequestEvent("Choose your worker", playersManager.getCurrentPlayer().getID()));
            }

            //If you have finished positioning
            else if(currentWorkersNumber % totalWorkersNumber==0) {
                playersManager.nextPlayer();
                notifyRequest(new RequestEvent("Position your first worker", playersManager.getCurrentPlayer().getID()));
            }

            //If you have positioned the first worker only
            else
                notifyRequest(new RequestEvent("Position your second worker", playersManager.getCurrentPlayer().getID()));
        }
    }
}
