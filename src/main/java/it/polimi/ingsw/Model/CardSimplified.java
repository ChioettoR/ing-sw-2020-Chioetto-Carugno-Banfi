package it.polimi.ingsw.Model;

import java.io.Serializable;

public class CardSimplified implements Serializable {
    private final String name;
    private final String effectName;
    private final String description;

    public CardSimplified(String name, String effectName, String description) {
        this.name = name;
        this.effectName = effectName;
        this.description = description;
    }

    public String getEffectName() {
        return effectName;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
