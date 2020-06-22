package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Observer.MessageObservable;

import java.io.IOException;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class StateManager extends MessageObservable {
    private GameState gameState;
    final PlayersManager playersManager = PlayersManager.getPlayersManager();

    public StateManager() {
        gameState = GameState.START;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Checks if the playerID received is the currentPlayer
     * @param playerID id of the player
     * @return true if correct, false otherwise
     * @throws IOException when socket closes
     */
    public synchronized boolean checkPlayerID(int playerID) throws IOException {
        if(playersManager.getCurrentPlayer().getID()!=playerID) {
            notifyMessage(new MessageEvent(411, playerID));
            return false;
        }
        return true;
    }

    /**
     * Checks the current gameState with the received one
     * @param gameState state to check
     * @return true if correct, false otherwise
     * @throws IOException when socket closes
     */
    public boolean checkState(GameState gameState) throws IOException {
        if(this.gameState != gameState) {
            notifyMessage(new MessageEvent(412, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
            return false;
        }
        return true;
    }
}
