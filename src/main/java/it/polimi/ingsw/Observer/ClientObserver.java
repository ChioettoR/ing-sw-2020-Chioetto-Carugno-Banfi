package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Client.ClientEvent;

public interface ClientObserver {
    void update(ClientEvent event);
}
