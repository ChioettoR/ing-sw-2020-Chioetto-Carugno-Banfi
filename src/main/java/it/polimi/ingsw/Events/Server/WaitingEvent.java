package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class WaitingEvent extends ServerEvent implements Serializable {

    private final boolean isWaiting;

    public boolean isWaiting() {
        return isWaiting;
    }

    /**
     * Waiting event when someone is already creating the lobby
     * @param isWaiting true if is waiting, false otherwise
     */
    public WaitingEvent(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }
}
