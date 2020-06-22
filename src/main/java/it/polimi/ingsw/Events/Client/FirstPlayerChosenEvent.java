package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class FirstPlayerChosenEvent extends ClientEvent implements Serializable {

    private final String name;

    public FirstPlayerChosenEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
