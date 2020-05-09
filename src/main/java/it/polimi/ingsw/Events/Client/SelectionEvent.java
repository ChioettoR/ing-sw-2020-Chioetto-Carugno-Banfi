package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class SelectionEvent extends ClientEvent implements Serializable {
    private final int workerID;

    public SelectionEvent(int workerID) {
        this.workerID = workerID;
    }

    public int getWorkerID() {
        return workerID;
    }
}
