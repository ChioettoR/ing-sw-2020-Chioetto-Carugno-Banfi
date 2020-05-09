package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class LoseEvent extends ServerEvent implements Serializable {
    private final String string;

    public String getString() {
        return string;
    }

    public LoseEvent(String string, int playerID) {
        this.string = string;
        super.playerID = playerID;
    }
}
