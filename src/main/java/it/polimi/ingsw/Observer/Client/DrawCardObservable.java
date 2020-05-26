package it.polimi.ingsw.Observer.Client;

import it.polimi.ingsw.Events.Client.AllPlayersCardsEvent;
import it.polimi.ingsw.Events.Client.PickCardEvent;

import java.io.IOException;

public class DrawCardObservable extends GenericObservable {
    /**
     *This is a notify for the client of draw Event
     * @param allPlayersCardsEvent draw event notified
     * @throws IOException when socket closes
     */
    public void notify(AllPlayersCardsEvent allPlayersCardsEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(allPlayersCardsEvent);
            }
        }
    }
    /**
     *This is a notify for the client of pick card Event
     * @param pickCardEvent pick card event notified
     * @throws IOException when socket closes
     */
    public void notify(PickCardEvent pickCardEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(pickCardEvent);
            }
        }
    }
}
