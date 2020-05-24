package it.polimi.ingsw.Model;

import java.io.Serializable;

public class CardSimplified implements Serializable {
    private final String name;
    private final String effectName;
    private final String description;

    /**
     * This is a simplified version of the Card, used to have a faster and lighter communication with client
     * @param name name of the card
     * @param effectName effect of the card
     * @param description description of the effect of the card
     */
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
