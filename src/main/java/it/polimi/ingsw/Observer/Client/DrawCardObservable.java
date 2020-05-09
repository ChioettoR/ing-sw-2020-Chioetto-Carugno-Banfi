package it.polimi.ingsw.Observer.Client;

import it.polimi.ingsw.Events.Client.DrawEvent;
import it.polimi.ingsw.Events.Client.PickCardEvent;

import java.io.IOException;

public class DrawCardObservable extends GenericObservable {

    public void notify(DrawEvent drawEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(drawEvent);
            }
        }
    }

    public void notify(PickCardEvent pickCardEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(pickCardEvent);
            }
        }
    }
}
