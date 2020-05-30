package it.polimi.ingsw.Observer.Server;

import it.polimi.ingsw.Events.Server.ColorSelectingEvent;
import it.polimi.ingsw.Events.Server.PlayerChosenColorEvent;

import java.io.IOException;

public class ColorObservable extends MessageObservable {
    public void notify(ColorSelectingEvent colorSelectingEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(colorSelectingEvent);
            }
        }
    }

    public void notify(PlayerChosenColorEvent playerChosenColorEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(playerChosenColorEvent);
            }
        }
    }
}
