package it.polimi.ingsw.Observer.Server;

import it.polimi.ingsw.Events.Server.ChangeEvent;
import it.polimi.ingsw.Observer.Client.ClientObserver;

import java.io.IOException;

public class ChangeObservable extends MessageObservable {
    /**
     *This is a notify for the client of changeEvent
     * @param changeEvent change event notified
     * @throws IOException when socket closes
     */
    public void notify(ChangeEvent changeEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(changeEvent);
            }
        }
    }
}
