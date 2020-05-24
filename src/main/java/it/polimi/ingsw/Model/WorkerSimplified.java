package it.polimi.ingsw.Model;

import java.io.Serializable;

public class WorkerSimplified implements Serializable {
    final String playerName;
    final int localID;

    /**
     * This is a simplified version of the Worker, used to have a faster and lighter communication with client
     * @param playerName name of the player
     * @param localID unique ID of the worker used to distinguish him
     */
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
