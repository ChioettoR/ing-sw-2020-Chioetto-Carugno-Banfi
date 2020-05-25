package it.polimi.ingsw.Observer.Client;

import it.polimi.ingsw.Events.Client.SelectionEvent;

import java.io.IOException;

public class SelectionObservable extends GenericObservable {
    /**
     *This is a notify for the client of selection Event
     * @param selectionEvent selection event notified
     * @throws IOException when socket closes
     */
    public void notify(SelectionEvent selectionEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(selectionEvent);
            }
        }
    }
}
