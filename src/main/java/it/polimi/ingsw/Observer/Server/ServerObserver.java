package it.polimi.ingsw.Observer.Server;

import it.polimi.ingsw.Events.Server.ServerEvent;

import java.io.IOException;

public interface ServerObserver {
    void update(ServerEvent serverEvent) throws IOException;
}
