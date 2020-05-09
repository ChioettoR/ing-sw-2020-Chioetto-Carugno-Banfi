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

    public void selection(int playerID, int workerID) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.SELECTING))
            return;

        Worker worker = playersManager.getWorkerWithID(playersManager.getCurrentPlayer().getID(), workerID);

        if(worker==null) {
            notifyError(new ErrorEvent("It's not your worker!", playersManager.getCurrentPlayer().getID()));
            notifyRequest(new RequestEvent("Select another worker", playersManager.getCurrentPlayer().getID()));
            return;
        }

        if(!worker.isAvailable()) {
            notifyError(new ErrorEvent("This worker is unavailable", playersManager.getCurrentPlayer().getID()));
            if(!checkLose())
                notifyRequest(new RequestEvent("Select another worker", playersManager.getCurrentPlayer().getID()));
            else
                checkWin();
            return;
        }

        playersManager.setCurrentWorker(playersManager.getWorkerWithID(playersManager.getCurrentPlayer().getID(), workerID));
        notifySuccess(new SuccessEvent("Worker selected", playersManager.getCurrentPlayer().getID()));
        notifyRequest(new RequestEvent("Select the action you want to perform", playersManager.getCurrentPlayer().getID()));
        actionManager.transition();
    }

    private void checkWin() throws IOException {
        int winnerID = playersManager.getPlayerWinnerID();
        if(winnerID!=-1) {
            notifyWin(new WinEvent("You win", playersManager.getCurrentPlayer().getID()));
            for(Player p : playersManager.getNextPlayers())
                notifyLose(new LoseEvent("You lose", p.getID()));
            stateManager.setGameState(GameState.END);
        }
    }

    private boolean checkLose() throws IOException {
        ArrayList<Worker> workers = playersManager.getCurrentPlayer().getWorkers();
        if(workers.stream().noneMatch(Worker::isAvailable)) {
            notifyLose(new LoseEvent("You lose", playersManager.getCurrentPlayer().getID()));
            playersManager.deleteCurrentPlayer();
            playersManager.nextPlayerAndStartRound();
            return true;
        }
        return false;
    }
}
