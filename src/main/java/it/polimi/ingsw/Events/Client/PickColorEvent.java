package it.polimi.ingsw.Events.Client;

import it.polimi.ingsw.Model.PlayerColor;

import java.io.Serializable;

public class PickColorEvent extends ClientEvent implements Serializable {
    private final PlayerColor playerColor;

    public PickColorEvent(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }
}
