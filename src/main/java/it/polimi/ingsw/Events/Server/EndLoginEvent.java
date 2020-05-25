package it.polimi.ingsw.Events.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class EndLoginEvent implements Serializable {
    private final ArrayList<String> names;

    /**
     * Event that marks the end of the login
     * @param names list of name of participants
     */
    public EndLoginEvent(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getNames() {
        return names;
    }
}
