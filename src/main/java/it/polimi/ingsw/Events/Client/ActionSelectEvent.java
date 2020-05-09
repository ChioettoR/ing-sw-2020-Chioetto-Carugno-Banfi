package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class ActionSelectEvent extends ClientEvent implements Serializable {
    private final String action;

    public ActionSelectEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
