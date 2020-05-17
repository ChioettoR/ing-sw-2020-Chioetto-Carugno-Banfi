package it.polimi.ingsw.Model;

import java.io.Serializable;

public class WorkerSimplified implements Serializable {
    final String playerName;
    final int localID;

    public WorkerSimplified(String playerName, int localID) {
        this.playerName = playerName;
        this.localID = localID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLocalID() {
        return localID;
    }
}
