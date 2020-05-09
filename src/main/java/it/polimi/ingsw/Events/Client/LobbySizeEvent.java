package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class LobbySizeEvent implements Serializable {
    final int lobbySize;

    public int getLobbySize() {
        return lobbySize;
    }

    public LobbySizeEvent(int lobbySize) {
        this.lobbySize = lobbySize;
    }
}
