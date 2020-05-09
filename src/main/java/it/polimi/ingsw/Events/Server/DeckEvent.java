package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.MiniDeckSimplified;

import java.io.Serializable;

public class DeckEvent extends ServerEvent implements Serializable {
    private final MiniDeckSimplified deck;

    public DeckEvent(MiniDeckSimplified deck) {
        this.deck = deck;
        super.playerID = -1;
    }

    public MiniDeckSimplified getDeck() {
        return deck;
    }
}
