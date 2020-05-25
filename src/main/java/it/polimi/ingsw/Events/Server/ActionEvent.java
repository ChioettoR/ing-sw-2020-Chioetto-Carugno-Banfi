package it.polimi.ingsw.Events.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class ActionEvent extends ServerEvent implements Serializable {
    private final ArrayList<String> actions;

    /**
     * Event with all the list of actions of a certain player
     * @param actions list of actions
     * @param playerID player's ID
     */
    public ActionEvent(ArrayList<String> actions, int playerID) {
        this.actions = actions;
        super.playerID = playerID;
    }

    public ArrayList<String> getActions() {
        return actions;
    }
}
