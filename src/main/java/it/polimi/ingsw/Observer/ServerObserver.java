package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Events.Server.ServerEvent;

import java.io.IOException;

public interface ServerObserver {
    void update(ServerEvent serverEvent) throws IOException;
}
