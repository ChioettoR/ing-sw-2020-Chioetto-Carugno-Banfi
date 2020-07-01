package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Server.ColorSelectingEvent;
import it.polimi.ingsw.Events.Server.PlayerChosenColorEvent;

import java.io.IOException;

public class ColorObservable extends MessageObservable {

    /**
     * Notifies when selecting a color
     * @param colorSelectingEvent event to notify
     * @throws IOException when socket closes
     */
    public void notify(ColorSelectingEvent colorSelectingEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(colorSelectingEvent);
            }
        }
    }

    /**
     * Notifies when choosing a color
     * @param playerChosenColorEvent event to notify
     * @throws IOException when socket closes
     */
    public void notify(PlayerChosenColorEvent playerChosenColorEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(playerChosenColorEvent);
            }
        }
    }
}
