package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.FirstPlayerEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Observer.Server.FirstPlayerObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FirstPlayerManager extends FirstPlayerObservable {

    PlayersManager playersManager = PlayersManager.getPlayersManager();
    StateManager stateManager;
    ArrayList<String> names;

    public FirstPlayerManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void transition() throws IOException {
        names = (ArrayList<String>) playersManager.getPlayers().stream().map(Player::getName).collect(Collectors.toList());
        notify(new FirstPlayerEvent(names, playersManager.getCurrentPlayer().getID()));
    }

    public void firstPlayerChosen(int playerID, String firstPlayerName) throws IOException {

        if (!stateManager.checkPlayerID(playerID))
            return;

        if (!stateManager.checkState(GameState.FIRSTPLAYERSELECTION))
            return;

        if(!names.contains(firstPlayerName)) notifyMessage(new MessageEvent(425, playersManager.getCurrentPlayer().getID()));
        else {
            for(Player p : playersManager.getPlayers()) {
                if(p.getName().equalsIgnoreCase(firstPlayerName)) {
                    playersManager.setNextPlayerIndex(playersManager.getPlayers().indexOf(p));
                    firstPlayerSelected();
                    return;
                }
                notifyMessage(new MessageEvent(425, playersManager.getCurrentPlayer().getID()));
            }
        }
    }

    public void firstPlayerSelected() throws IOException {
        playersManager.nextPlayer();
        notifyMessage(new MessageEvent(108, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        for(Player p : playersManager.getNextPlayers()) notifyMessage(new MessageEvent(114, p.getID()));
        stateManager.setGameState(GameState.POSITIONING);
    }
}
