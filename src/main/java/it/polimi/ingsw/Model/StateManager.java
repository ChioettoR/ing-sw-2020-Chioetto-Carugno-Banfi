package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.ErrorEvent;
import it.polimi.ingsw.Observer.Server.MessageObservable;

import java.io.IOException;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class StateManager extends MessageObservable {
    private GameState gameState;
    PlayersManager playersManager = PlayersManager.getPlayersManager();

    public StateManager() {
        gameState = GameState.START;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public synchronized boolean checkPlayerID(int playerID) throws IOException {
        if(playersManager.getCurrentPlayer().getID()!=playerID) {
            notifyError(new ErrorEvent("It's not your turn!", playerID));
            return false;
        }
        return true;
    }

    public boolean checkState(GameState gameState) throws IOException {
        if(this.gameState != gameState) {
            notifyError(new ErrorEvent("Invalid input!", playersManager.getCurrentPlayer().getID()));
            return false;
        }
        return true;
    }
}
