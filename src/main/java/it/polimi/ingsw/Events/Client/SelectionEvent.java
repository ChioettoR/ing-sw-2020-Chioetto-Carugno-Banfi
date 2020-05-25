package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class SelectionEvent extends ClientEvent implements Serializable {
    private final int workerID;

    /**
     * Event triggered when selecting a worker
     * @param workerID specific ID of a worker
     */
    public SelectionEvent(int workerID) {
        this.workerID = workerID;
    }

    public int getWorkerID() {
        return workerID;
    }
}
