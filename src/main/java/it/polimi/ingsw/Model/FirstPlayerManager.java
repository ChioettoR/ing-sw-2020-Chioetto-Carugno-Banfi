package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.FirstPlayerEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Observer.FirstPlayerObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FirstPlayerManager extends FirstPlayerObservable {

    PlayersManager playersManager = PlayersManager.getPlayersManager();
    ColorPoolManager colorPoolManager;
    StateManager stateManager;
    ArrayList<String> names;

    public FirstPlayerManager(StateManager stateManager, ColorPoolManager colorPoolManager) {
        this.stateManager = stateManager;
        this.colorPoolManager = colorPoolManager;
    }

    public void transition() throws IOException {
        names = (ArrayList<String>) playersManager.getPlayers().stream().map(Player::getName).collect(Collectors.toList());
        notify(new FirstPlayerEvent(names, playersManager.getCurrentPlayer().getID()));
        for(Player p : playersManager.getNextPlayers()) {
            notifyMessage(new MessageEvent(117, p.getID()));
        }
    }

    public void firstPlayerChosen(int playerID, String firstPlayerName) throws IOException {

        if (!stateManager.checkPlayerID(playerID))
            return;

        if (!stateManager.checkState(GameState.FIRSTPLAYERSELECTION))
            return;

        if(!names.contains(firstPlayerName)) notifyMessage(new MessageEvent(425, playersManager.getCurrentPlayer().getID()));
        else {
            for(int i=0; i<playersManager.getPlayersNumber(); i++) {
                if(playersManager.getPlayers().get(i).getName().equalsIgnoreCase(firstPlayerName)) {
                    playersManager.setNextPlayerIndex(i);
                    firstPlayerSelected();
                    return;
                }
            }
            notifyMessage(new MessageEvent(425, playersManager.getCurrentPlayer().getID()));
        }
    }

    public void firstPlayerSelected() throws IOException {
        colorPoolManager.transition();
    }
}
