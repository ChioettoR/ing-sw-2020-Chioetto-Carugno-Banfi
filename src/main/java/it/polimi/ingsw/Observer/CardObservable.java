package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Server.DeckEvent;
import it.polimi.ingsw.Events.Server.FullDeckEvent;
import it.polimi.ingsw.Events.Server.PlayerChosenCardEvent;

import java.io.IOException;

public class CardObservable extends MessageObservable {
    /**
     *This is a notify for the client of deckEvent
     * @param deckEvent deck event notified
     * @throws IOException when socket closes
     */
    public void notifyDeck(DeckEvent deckEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(deckEvent);
            }
        }
    }

    /**
     * This is a notify for the client of the fullDeck event
     * @param fullDeckEvent event notified
     * @throws IOException when socket closes
     */
    public void notifyFullDeck(FullDeckEvent fullDeckEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(fullDeckEvent);
            }
        }
    }

    /**
     *This is a notify for the client when a player choose a card
     * @param playerChosenCardEvent player chosen card event notified
     * @throws IOException when socket closes
     */
    public void notifyPower(PlayerChosenCardEvent playerChosenCardEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(playerChosenCardEvent);
            }
        }
    }
 }
