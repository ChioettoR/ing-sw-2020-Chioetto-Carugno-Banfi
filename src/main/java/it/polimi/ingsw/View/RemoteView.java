package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Events.Server.ServerEvent;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Observer.ServerObserver;

import java.io.IOException;
import java.io.Serializable;

public class RemoteView implements ServerObserver {
    private final int playerID;
    private final Connection connection;
    private final Controller controller;

    public RemoteView(int playerID, Connection connection, Controller controller){
        this.playerID = playerID;
        this.connection = connection;
        this.controller = controller;
    }

    public int getPlayerID() {
        return playerID;
    }

    /**
     * Sends the message
     * @param event event
     * @throws IOException when socket closes
     */
    public void sendMessage(ClientEvent event) throws IOException {
        controller.send(event);
    }

    /**
     * Sends the event if i am the receiver
     * @param serverEvent event to send
     * @throws IOException when socket closes
     */
    @Override
    public void update(ServerEvent serverEvent) throws IOException {
        if(serverEvent.getPlayerID()==-1 || serverEvent.getPlayerID()==playerID)
           connection.send((Serializable) serverEvent);
    }
}
