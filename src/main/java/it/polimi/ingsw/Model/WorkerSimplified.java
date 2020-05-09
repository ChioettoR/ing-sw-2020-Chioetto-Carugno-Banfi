package it.polimi.ingsw.Model;

import java.io.Serializable;

public class WorkerSimplified implements Serializable {
    final int playerID;
    final int localID;

    public WorkerSimplified(int playerID, int localID) {
        this.playerID = playerID;
        this.localID = localID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getLocalID() {
        return localID;
    }
}
