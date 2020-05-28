package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class SpectatorEvent extends ServerEvent implements Serializable {

    public SpectatorEvent(int playerID) {
        super.playerID = playerID;
    }
}
