package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class PickCardEvent extends ClientEvent implements Serializable {
    private final String cardName;

    /**
     * Event for the choice of the card
     * @param cardName name of the card
     */
    public PickCardEvent(String cardName) {
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }
}
