package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.FirstPlayerEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Observer.FirstPlayerObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FirstPlayerManager extends FirstPlayerObservable {

    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final ColorPoolManager colorPoolManager;
    private final StateManager stateManager;
    private ArrayList<String> names;

    public FirstPlayerManager(StateManager stateManager, ColorPoolManager colorPoolManager) {
        this.stateManager = stateManager;
        this.colorPoolManager = colorPoolManager;
    }

    /**
     * Sends to the challenger the choice of the 1st player to begin the game
     * @throws IOException when socket closes
     */
    public void transition() throws IOException {
        names = (ArrayList<String>) playersManager.getPlayers().stream().map(Player::getName).collect(Collectors.toList());
        notify(new FirstPlayerEvent(names, playersManager.getCurrentPlayer().getID()));
        for(Player p : playersManager.getNextPlayers()) {
            notifyMessage(new MessageEvent(117, p.getID()));
        }
    }

    /**
     * Sets the first player
     * @param playerID ID of the player
     * @param firstPlayerName name of the first player
     * @throws IOException when socket closes
     */
    public void firstPlayerChosen(int playerID, String firstPlayerName) throws IOException {

        if (!stateManager.checkPlayerID(playerID))
            return;

        if (!stateManager.checkState(GameState.FIRSTPLAYERSELECTION))
            return;

        if(names.contains(firstPlayerName)) {
            for(int i=0; i<playersManager.getPlayersNumber(); i++) {
                if(playersManager.getPlayers().get(i).getName().equalsIgnoreCase(firstPlayerName)) {
                    playersManager.setNextPlayerIndex(i);
                    firstPlayerSelected();
                    return;
                }
            }
        }
        notifyMessage(new MessageEvent(425, playersManager.getCurrentPlayer().getID()));
    }

    /**
     * Transition to the color choice phase
     * @throws IOException
     */
    public void firstPlayerSelected() throws IOException {
        colorPoolManager.transition();
    }
}
