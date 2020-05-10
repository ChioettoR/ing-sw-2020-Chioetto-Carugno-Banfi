package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class LoseEvent extends ServerEvent implements Serializable {

    public LoseEvent(int playerID) {
        super.playerID = playerID;
    }
}
