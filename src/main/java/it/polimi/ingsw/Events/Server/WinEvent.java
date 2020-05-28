package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class WinEvent extends ServerEvent implements Serializable {
    private final String winnerName;

    public String getWinnerName() {
        return winnerName;
    }

    /**
     * Win event of a certain player
     * @param string name of the winner
     * @param playerID id of the winner
     */
    public WinEvent(String string, int playerID) {
        this.winnerName = string;
        super.playerID = playerID;
    }
}
