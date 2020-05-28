package it.polimi.ingsw.Observer.Server;

import it.polimi.ingsw.Events.Server.ChangeEvent;
import it.polimi.ingsw.Events.Server.SpectatorEvent;

import java.io.IOException;

public class SpectatorObservable extends MessageObservable {

    public void notify(SpectatorEvent spectatorEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(spectatorEvent);
            }
        }
    }
}

