package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class WinEvent extends ServerEvent implements Serializable {
    private final String winnerName;
    private final boolean youWin;

    public String getWinnerName() {
        return winnerName;
    }

    public boolean isYouWin() {
        return youWin;
    }

    public WinEvent(String string, boolean youWin, int playerID) {
        this.winnerName = string;
        this.youWin = youWin;
        super.playerID = playerID;
    }
}
