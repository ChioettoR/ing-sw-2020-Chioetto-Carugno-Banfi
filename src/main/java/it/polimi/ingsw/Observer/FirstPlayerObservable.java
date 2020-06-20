package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Server.FirstPlayerEvent;

import java.io.IOException;

public class FirstPlayerObservable extends MessageObservable {

    //TODO: javadoc

    public void notify(FirstPlayerEvent firstPlayerEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(firstPlayerEvent);
            }
        }
    }
}
