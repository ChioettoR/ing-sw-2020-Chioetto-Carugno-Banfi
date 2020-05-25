package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.CardSimplified;

import java.io.Serializable;

public class CardEvent extends ServerEvent implements Serializable {
    private final CardSimplified card;

    /**
     * Event triggered after the card is picked
     * @param card card picked
     * @param playerID player's ID
     */
    public CardEvent(CardSimplified card, int playerID) {
        this.card = card;
        super.playerID = playerID;
    }

    public CardSimplified getCard() {
        return card;
    }
}
