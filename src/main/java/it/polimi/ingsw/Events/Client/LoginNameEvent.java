package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class LoginNameEvent extends ClientEvent implements Serializable {
    final String name;

    /**
     * Event triggered with the name of the participant
     * @param name participant's name
     */
    public LoginNameEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
