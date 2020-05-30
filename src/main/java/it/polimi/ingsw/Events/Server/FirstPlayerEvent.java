package it.polimi.ingsw.Events.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class FirstPlayerEvent extends ServerEvent implements Serializable {
    private final ArrayList<String> players;

    public FirstPlayerEvent(ArrayList<String> players, int playerID) {
        this.players = players;
        super.playerID = playerID;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
}
