package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Server.ColorSelectingEvent;
import it.polimi.ingsw.Events.Server.PlayerChosenColorEvent;

import java.io.IOException;

public class ColorObservable extends MessageObservable {

    //TODO : javadoc

    public void notify(ColorSelectingEvent colorSelectingEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(colorSelectingEvent);
            }
        }
    }

    //TODO : javadoc

    public void notify(PlayerChosenColorEvent playerChosenColorEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(playerChosenColorEvent);
            }
        }
    }
}
