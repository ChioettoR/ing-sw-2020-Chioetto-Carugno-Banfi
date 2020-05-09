package it.polimi.ingsw.Observer.Server;

import it.polimi.ingsw.Events.Server.ActionEvent;
import it.polimi.ingsw.Events.Server.AvailableTilesEvent;
import it.polimi.ingsw.Events.Server.ChangeEvent;
import it.polimi.ingsw.Observer.Client.ClientObserver;

import java.io.IOException;

public class ActionObservable extends MessageObservable {

    public void notify(ActionEvent actionEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(actionEvent);
            }
        }
    }

    public void notify(AvailableTilesEvent availableTilesEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(availableTilesEvent);
            }
        }
    }

    public void notify(ChangeEvent changeEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(changeEvent);
            }
        }
    }
}
