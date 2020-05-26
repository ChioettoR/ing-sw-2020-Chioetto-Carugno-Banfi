package it.polimi.ingsw.Events.Client;

import java.io.Serializable;
import java.util.ArrayList;

public class AllPlayersCardsEvent extends ClientEvent implements Serializable {
    private final ArrayList<String> cards;

    public AllPlayersCardsEvent(ArrayList<String> cards) {
        this.cards = cards;
    }

    public ArrayList<String> getCards() {
        return cards;
    }
}
