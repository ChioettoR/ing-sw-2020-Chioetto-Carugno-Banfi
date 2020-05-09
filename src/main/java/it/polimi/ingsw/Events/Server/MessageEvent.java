package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class MessageEvent extends ServerEvent implements Serializable {
    private final String string;

    public String getString() {
        return string;
    }

    public MessageEvent(String string, int playerID) {
        this.string = string;
        super.playerID = playerID;
    }

    public MessageEvent(String string) {
        this.string = string;
    }
}
