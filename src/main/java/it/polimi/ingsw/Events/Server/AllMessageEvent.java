package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class AllMessageEvent extends ServerEvent implements Serializable {
    private final String string;

    public String getString() {
        return string;
    }

    public AllMessageEvent(String string) {
        this.string = string;
        super.playerID = -1;
    }
}
