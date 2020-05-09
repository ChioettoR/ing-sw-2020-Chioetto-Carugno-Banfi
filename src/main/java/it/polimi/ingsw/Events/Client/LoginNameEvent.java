package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class LoginNameEvent implements Serializable {
    final String name;

    public LoginNameEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
