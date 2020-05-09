package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class SuccessEvent extends ServerEvent implements Serializable {
    private final String string;

    public String getString() {
        return string;
    }

    public SuccessEvent(String string, int playerID) {
        this.string = string;
        super.playerID = playerID;
    }

    public SuccessEvent(String string) {
        this.string = string;
    }
}