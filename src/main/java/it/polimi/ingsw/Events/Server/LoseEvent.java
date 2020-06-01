package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class LoseEvent extends ServerEvent implements Serializable {

    private final boolean youLose;
    private final String loserName;

    public boolean isYouLose() {
        return youLose;
    }

    public String getLoserName() {
        return loserName;
    }

    public LoseEvent(boolean youLose, String loserName, int playerID) {
        this.youLose = youLose;
        super.playerID = playerID;
        this.loserName = loserName;
    }
}
