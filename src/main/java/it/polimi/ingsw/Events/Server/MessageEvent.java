package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class MessageEvent extends ServerEvent implements Serializable {
    private final int messageID;

    public int getMessageID() {
        return messageID;
    }

    /**
     * Event with a specific ID message directed to a specific playerID
     * @param messageID ID of the message
     * @param playerID player's ID
     */
    public MessageEvent(int messageID, int playerID) {
        this.messageID = messageID;
        super.playerID = playerID;
    }

    public MessageEvent(int messageID) {
        this.messageID = messageID;
    }
}
