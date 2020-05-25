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

    /**
     * Event with information about lobby
     * @param lobbyName name of the first player who created the lobby
     * @param lobbySize size of the lobby
     */
    public LobbyInfoEvent(String lobbyName, int lobbySize) {
        this.lobbyName = lobbyName;
        this.lobbySize = lobbySize;
    }
}
