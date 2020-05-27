package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.Server.MessageObservable;

import java.io.IOException;
import java.util.ArrayList;

public class SelectionWorkerManager extends MessageObservable {

    PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final StateManager stateManager;
    private final ActionManager actionManager;

    public SelectionWorkerManager(StateManager stateManager, ActionManager actionManager) {
        this.stateManager = stateManager;
        this.actionManager = actionManager;
    }

    /**
     * This method selects the worker from the list of workers
     * @param playerID id of the player
     * @param workerID id of the worker selected
     * @throws IOException when socket closes
     */
    public void selection(int playerID, int workerID, String playerName) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.SELECTING))
            return;

        if(playerName!=null && !playerName.equals(playersManager.getCurrentPlayer().getName())) {
            notifyMessage(new MessageEvent(409, playersManager.getCurrentPlayer().getID()));
            return;
        }

        Worker worker = playersManager.getWorkerWithID(playersManager.getCurrentPlayer().getID(), workerID);

        if(worker==null) {
            notifyMessage(new MessageEvent(409, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
            notifyMessage(new MessageEvent(104, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
            return;
        }

        if(!worker.isAvailable()) notifyMessage(new MessageEvent(410, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));

        playersManager.setCurrentWorker(playersManager.getWorkerWithID(playersManager.getCurrentPlayer().getID(), workerID));
        actionManager.transition();
    }
}
