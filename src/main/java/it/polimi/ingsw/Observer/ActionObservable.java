package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Server.ActionEvent;
import it.polimi.ingsw.Events.Server.AvailableTilesEvent;
import it.polimi.ingsw.Events.Server.ChangeEvent;

import java.io.IOException;

public class ActionObservable extends MessageObservable {
    /**
     *This is a notify for the client of actionEvent
     * @param actionEvent action event notified
     * @throws IOException when socket closes
     */
    public void notify(ActionEvent actionEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(actionEvent);
            }
        }
    }
    /**
     *This is a notify for the client of the available tiles for him
     * @param availableTilesEvent available tile event notified
     * @throws IOException when socket closes
     */
    public void notify(AvailableTilesEvent availableTilesEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(availableTilesEvent);
            }
        }
    }
    /**
     *This is a notify for the client of actionEvent
     * @param changeEvent action event notified
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
