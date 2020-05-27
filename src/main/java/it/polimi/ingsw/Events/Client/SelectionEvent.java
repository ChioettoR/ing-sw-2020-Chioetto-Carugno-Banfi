package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class SelectionEvent extends ClientEvent implements Serializable {
    private final int workerID;
    private String playerName;

    /**
     * Event triggered when selecting a worker
     * @param workerID specific ID of a worker
     */
    public SelectionEvent(int workerID) {
        this.workerID = workerID;
    }

    public SelectionEvent(int workerID, String playerName) {
        this.workerID = workerID;
        this.playerName = playerName;
    }

    public int getWorkerID() {
        return workerID;
    }

    public String getPlayerName() {
        return playerName;
    }
}
