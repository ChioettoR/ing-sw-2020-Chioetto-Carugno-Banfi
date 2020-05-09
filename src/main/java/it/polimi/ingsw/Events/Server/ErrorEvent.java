package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class ErrorEvent extends ServerEvent implements Serializable {
    private final String string;

    public String getString() {
        return string;
    }

    public ErrorEvent(String string, int playerID) {
        this.string = string;
        super.playerID = playerID;
    }

    public ErrorEvent(String string) {
        this.string = string;
    }
}
