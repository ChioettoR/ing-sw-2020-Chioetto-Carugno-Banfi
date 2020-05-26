package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.CardSimplified;

import java.io.Serializable;
import java.util.ArrayList;

public class FullDeckEvent extends ServerEvent implements Serializable {

    private final ArrayList<CardSimplified> cards;

    public FullDeckEvent(ArrayList<CardSimplified> cards, int playerID) {
        this.cards = cards;
        super.playerID = playerID;
    }

    public ArrayList<CardSimplified> getCards() {
        return cards;
    }
}
