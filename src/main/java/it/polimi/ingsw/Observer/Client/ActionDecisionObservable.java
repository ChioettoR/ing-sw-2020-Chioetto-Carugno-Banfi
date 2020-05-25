package it.polimi.ingsw.Observer.Client;

import it.polimi.ingsw.Events.Client.ActionSelectEvent;
import it.polimi.ingsw.Events.Client.BuildDecisionEvent;
import it.polimi.ingsw.Events.Client.MoveDecisionEvent;

import java.io.IOException;

public class ActionDecisionObservable extends GenericObservable {
    /**
     *This is a notify for the client of a move Event
     * @param moveDecisionEvent move event notified
     * @throws IOException when socket closes
     */
    public void notify(MoveDecisionEvent moveDecisionEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(moveDecisionEvent);
            }
        }
    }
    /**
     *This is a notify for the client of a build Event
     * @param buildDecisionEvent build event notified
     * @throws IOException when socket closes
     */
    public void notify(BuildDecisionEvent buildDecisionEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(buildDecisionEvent);
            }
        }
    }
    /**
     *This is a notify for the client of action select Event
     * @param actionSelectEvent action select event notified
     * @throws IOException when socket closes
     */
    public void notify(ActionSelectEvent actionSelectEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(actionSelectEvent);
            }
        }
    }
}
