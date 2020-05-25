package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class LobbySizeEvent extends ClientEvent implements Serializable {
    final int lobbySize;

    public int getLobbySize() {
        return lobbySize;
    }

    /**
     * Triggers with the size of the lobby
     * @param lobbySize size of the lobby
     */
    public LobbySizeEvent(int lobbySize) {
        this.lobbySize = lobbySize;
    }
}
