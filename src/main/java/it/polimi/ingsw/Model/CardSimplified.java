package it.polimi.ingsw.Model;

import java.io.Serializable;

public class CardSimplified implements Serializable {
    private final String name;

    public CardSimplified(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
