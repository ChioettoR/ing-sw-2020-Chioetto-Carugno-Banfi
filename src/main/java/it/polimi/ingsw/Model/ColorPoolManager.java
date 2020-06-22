package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.ColorSelectingEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.PlayerChosenColorEvent;
import it.polimi.ingsw.Observer.ColorObservable;

import java.io.IOException;
import java.util.ArrayList;

public class ColorPoolManager extends ColorObservable {
    StateManager stateManager;
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    ArrayList<PlayerColor> colorsAvailable = new ArrayList<>();

    public ColorPoolManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void transition() throws IOException {
        playersManager.nextPlayer();
        stateManager.setGameState(GameState.COLORSELECTING);
        colorsAvailable.add(PlayerColor.P1_COLOR);
        colorsAvailable.add(PlayerColor.P2_COLOR);
        if(playersManager.getPlayersNumber()==3) colorsAvailable.add(PlayerColor.P3_COLOR);
        for(Player p : playersManager.getNextPlayers()) {
            notifyMessage(new MessageEvent(118, p.getID()));
        }
        notifyMessage(new MessageEvent(119, playersManager.getCurrentPlayer().getID()));
        notify(new ColorSelectingEvent(colorsAvailable, playersManager.getCurrentPlayer().getID()));
    }

    public void colorSelection(int playerID, PlayerColor playerColor) throws IOException {
        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.COLORSELECTING))
            return;

        if(!colorsAvailable.contains(playerColor)) notifyMessage(new MessageEvent(426, playersManager.getCurrentPlayer().getID()));
        else {
            colorsAvailable.remove(playerColor);
            playersManager.getCurrentPlayer().setColor(playerColor);
            notify(new PlayerChosenColorEvent(playerColor, playersManager.getCurrentPlayer().getName(), -1));
            playersManager.nextPlayer();
            ArrayList<PlayerColor> colorsAvailableCopy = new ArrayList<>(colorsAvailable);
            if(colorsAvailable.size()==1) {
                playersManager.getCurrentPlayer().setColor(colorsAvailable.get(0));
                notify(new PlayerChosenColorEvent(colorsAvailable.get(0), playersManager.getCurrentPlayer().getName(), -1));
                allColorsSelected();
            }
            else {
                notifyMessage(new MessageEvent(118, playerID));
                notifyMessage(new MessageEvent(119, playersManager.getCurrentPlayer().getID()));
                notify(new ColorSelectingEvent(colorsAvailableCopy, playersManager.getCurrentPlayer().getID()));
            }
        }
    }

    public void allColorsSelected() throws IOException {
        playersManager.nextPlayer();
        notifyMessage(new MessageEvent(108, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        for(Player p : playersManager.getNextPlayers()) notifyMessage(new MessageEvent(114, p.getID()));
        stateManager.setGameState(GameState.POSITIONING);
    }
}
