package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.PlayerColor;

import java.io.Serializable;

public class PlayerChosenColorEvent extends ServerEvent implements Serializable {
    final PlayerColor playerColor;
    final String name;

    public PlayerChosenColorEvent(PlayerColor playerColor, String name, int playerID) {
        this.playerColor = playerColor;
        this.name = name;
        super.playerID = playerID;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public String getName() {
        return name;
    }
}
