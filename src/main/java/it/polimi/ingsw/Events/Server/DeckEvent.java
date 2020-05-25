package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.MiniDeckSimplified;

import java.io.Serializable;

public class DeckEvent extends ServerEvent implements Serializable {
    private final MiniDeckSimplified deck;

    /**
     * Event for the deck simplified
     * @param deck deck simplified
     */
    public DeckEvent(MiniDeckSimplified deck) {
        this.deck = deck;
        super.playerID = -1;
    }

    /**
     * Event for the mini deck simplified
     * @param deck deck based on the number of player without card, goes from the lobby size down to 0
     * @param playerID id of the picking player
     */
    public DeckEvent(MiniDeckSimplified deck, int playerID) {
        this.deck = deck;
        super.playerID = playerID;
    }

    public MiniDeckSimplified getDeck() {
        return deck;
    }
}
