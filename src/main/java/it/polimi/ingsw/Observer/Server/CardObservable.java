package it.polimi.ingsw.Observer.Server;

import it.polimi.ingsw.Events.Server.CardEvent;
import it.polimi.ingsw.Events.Server.DeckEvent;
import it.polimi.ingsw.Observer.Client.ClientObserver;

import java.io.IOException;

public class CardObservable extends MessageObservable {

    public void notifyDeck(DeckEvent deckEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(deckEvent);
            }
        }
    }

    public void notifyCard(CardEvent cardEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(cardEvent);
            }
        }
    }
}
