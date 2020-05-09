package it.polimi.ingsw.Observer.Client;

import it.polimi.ingsw.Events.Client.ActionSelectEvent;
import it.polimi.ingsw.Events.Client.BuildDecisionEvent;
import it.polimi.ingsw.Events.Client.MoveDecisionEvent;

import java.io.IOException;

public class ActionDecisionObservable extends GenericObservable {

    public void notify(MoveDecisionEvent moveDecisionEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(moveDecisionEvent);
            }
        }
    }

    public void notify(BuildDecisionEvent buildDecisionEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(buildDecisionEvent);
            }
        }
    }

    public void notify(ActionSelectEvent actionSelectEvent) throws IOException {
        synchronized (observers) {
            for (ClientObserver observer : observers) {
                observer.update(actionSelectEvent);
            }
        }
    }
}
