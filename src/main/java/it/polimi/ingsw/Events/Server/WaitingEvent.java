package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class WaitingEvent extends ServerEvent implements Serializable {

    private final boolean isWaiting;

    public boolean isWaiting() {
        return isWaiting;
    }

    public WaitingEvent(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }
}
