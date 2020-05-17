package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.MiniDeckSimplified;

import java.io.Serializable;

public class DeckEvent extends ServerEvent implements Serializable {
    private final MiniDeckSimplified deck;

    public DeckEvent(MiniDeckSimplified deck) {
        this.deck = deck;
        super.playerID = -1;
    }

    public DeckEvent(MiniDeckSimplified deck, int playerID) {
        this.deck = deck;
        super.playerID = playerID;
    }

    public MiniDeckSimplified getDeck() {
        return deck;
    }
}
