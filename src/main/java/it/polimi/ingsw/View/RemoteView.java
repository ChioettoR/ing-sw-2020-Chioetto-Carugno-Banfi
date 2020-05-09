package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Events.Server.ServerEvent;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Observer.Client.ClientObserver;
import it.polimi.ingsw.Observer.Server.ServerObserver;

import java.io.IOException;
import java.io.Serializable;

public class RemoteView implements ServerObserver {
    int playerID;
    Connection connection;
    Controller controller;

    public RemoteView(int playerID, Connection connection, Controller controller){
        this.playerID = playerID;
        this.connection = connection;
        this.controller = controller;
    }

    public void sendMessage(ClientEvent event) throws IOException {
        controller.send(event);
    }

    @Override
    public void update(ServerEvent serverEvent) throws IOException {
        if(serverEvent.getPlayerID()==-1 || serverEvent.getPlayerID()==playerID)
           connection.send((Serializable) serverEvent);
    }
}