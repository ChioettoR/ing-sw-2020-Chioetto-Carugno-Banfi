package it.polimi.ingsw.Observer.Client;

import it.polimi.ingsw.Events.Client.PositioningEvent;

import java.io.IOException;

public class PositioningObservable extends GenericObservable {

    public void notify(PositioningEvent positioningEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(positioningEvent);
            }
        }
    }
}
