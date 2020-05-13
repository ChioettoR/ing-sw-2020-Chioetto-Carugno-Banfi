package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class LobbyInfoEvent extends ServerEvent implements Serializable {
    final String lobbyName;
    final int lobbySize;

    public String getLobbyName() {
        return lobbyName;
    }

    public int getLobbySize() {
        return lobbySize;
    }

    public LobbyInfoEvent(String lobbyName, int lobbySize) {
        this.lobbyName = lobbyName;
        this.lobbySize = lobbySize;
    }
}
