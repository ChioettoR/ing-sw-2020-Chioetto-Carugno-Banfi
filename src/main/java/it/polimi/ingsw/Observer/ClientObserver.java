package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Client.ClientEvent;

import java.io.IOException;

public interface ClientObserver {
    void update(ClientEvent event) throws IOException;
}
