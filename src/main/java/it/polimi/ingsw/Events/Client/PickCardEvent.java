package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class PickCardEvent extends ClientEvent implements Serializable {
    private final String cardName;

    public PickCardEvent(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }
}
