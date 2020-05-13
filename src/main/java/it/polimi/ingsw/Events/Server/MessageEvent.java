package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class MessageEvent extends ServerEvent implements Serializable {
    private final int messageID;

    public int getMessageID() {
        return messageID;
    }

    public MessageEvent(int messageID, int playerID) {
        this.messageID = messageID;
        super.playerID = playerID;
    }

    public MessageEvent(int messageID) {
        this.messageID = messageID;
    }
}
