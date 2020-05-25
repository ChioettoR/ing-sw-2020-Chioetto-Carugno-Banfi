package it.polimi.ingsw.Observer.Client;

import it.polimi.ingsw.Events.Client.PositioningEvent;

import java.io.IOException;

public class PositioningObservable extends GenericObservable {
    /**
     *This is a notify for the client of positioning Event
     * @param positioningEvent positioning event notified
     * @throws IOException when socket closes
     */
    public void notify(PositioningEvent positioningEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(positioningEvent);
            }
        }
    }
}
