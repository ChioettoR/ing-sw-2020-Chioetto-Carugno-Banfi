package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class RequestEvent extends ServerEvent implements Serializable {
    private final String string;

    public String getString() {
        return string;
    }

    public RequestEvent(String string, int playerID) {
        this.string = string;
        super.playerID = playerID;
    }

    public RequestEvent(String string) {
        this.string = string;
    }
}
