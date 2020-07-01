package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Server.FirstPlayerEvent;

import java.io.IOException;

public class FirstPlayerObservable extends MessageObservable {

    /**
     * Notifies the firstPlayer event
     * @param firstPlayerEvent event to notify
     * @throws IOException when socket closes
     */
    public void notify(FirstPlayerEvent firstPlayerEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(firstPlayerEvent);
            }
        }
    }
}
