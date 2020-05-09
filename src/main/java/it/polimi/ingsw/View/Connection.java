package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Events.Client.LobbySizeEvent;
import it.polimi.ingsw.Events.Client.LoginNameEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Connection implements Runnable {

    private final Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final Server server;
    private RemoteView remoteView;
    private boolean active = true;
    private final boolean firstPlayer;
    private final ArrayList<Integer> acceptedLobbySizes = new ArrayList<>(Arrays.asList(2, 3));
    private String name;

    public String getName() {
        return name;
    }

    public Connection(Socket socket, Server server, boolean firstPlayer){
        this.socket = socket;
        this.server = server;
        this.firstPlayer = firstPlayer;
    }

    public synchronized void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public void send(Serializable serializable) throws IOException {
        oos.writeObject(serializable);
        oos.flush();
    }

    public void sendAll(Serializable serializable) throws IOException {
        server.sendAll(serializable);
    }

    public synchronized void closeConnection() {
        try{
            socket.close();
            oos.close();
            ois.close();
        }
        catch (IOException e) { System.err.println(e.getMessage()); }
        active = false;
    }

    @Override
    public void run() {
        try{
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            Serializable object;

            try {
                if (!server.isLobbyCreated() && !firstPlayer) {
                    send(new ErrorEvent("Another player is already creating a lobby"));
                    server.deregisterConnection(this);
                    return;
                }

                if (server.isFullLobby()) {
                    send(new ErrorEvent("The lobby is full"));
                    server.deregisterConnection(this);
                    return;
                }
            }
            catch (IOException e) {
                server.deregisterConnection(this);
            }

            if(firstPlayer)
                send(new MessageEvent("No lobbies found. Create one"));
            else
                send(new MessageEvent("Entered in " + server.getLobbyName() + "'s lobby of " + server.getLobbySize() + " players"));

            send(new RequestEvent("What's your name?"));
            boolean validName = false;

            while(!validName) {

                object = (Serializable) ois.readObject();

                while(!(object instanceof LoginNameEvent)) {
                    send(new ErrorEvent("Invalid input. Name required."));
                    send(new RequestEvent("Please, give me your name"));
                    object = (Serializable) ois.readObject();
                }

                name = ((LoginNameEvent) object).getName();

                if(!server.addName(name))
                    send(new ErrorEvent("This name has already been chosen"));
                else
                    validName = true;
            }

            send(new MessageEvent("Welcome " + name));

            if(firstPlayer) {
                send(new RequestEvent("Insert lobby players number"));

                int lobbySize = -1;
                boolean validInput = false;

                while(!validInput) {

                    object = (Serializable) ois.readObject();

                    while(!(object instanceof LobbySizeEvent)) {
                        send(new ErrorEvent("Invalid input. Number required."));
                        send(new RequestEvent("Please, insert lobby players number"));
                        object = (Serializable) ois.readObject();
                    }

                    lobbySize = ((LobbySizeEvent) object).getLobbySize();

                    if(!acceptedLobbySizes.contains(lobbySize))
                        send(new ErrorEvent("You can only create a 2/3 players lobby"));
                    else
                        validInput = true;
                }
                send(new SuccessEvent("Success!"));
                server.lobby(this, name, lobbySize);
            }
            else
                server.lobby(this, name);

            int playersLeft = server.getPlayersLeft();

            if(playersLeft==1)
                send(new MessageEvent("Waiting for another player to connect"));

            else if(playersLeft!=0)
                send(new MessageEvent("Waiting for other " + playersLeft + " players to connect"));

            else
                sendAll(new EndLoginEvent());

            while(isActive()) {
                Serializable read = (Serializable) ois.readObject();
                if(remoteView==null) send(new ErrorEvent("The game has not started yet. Please be patient"));
                else{
                    ClientEvent event = (ClientEvent) read;
                    event.setPlayerID(remoteView.playerID);
                    remoteView.sendMessage(event);
                }
            }
        }

        catch(IOException | ClassNotFoundException e) {
            System.err.println("Connection interrupted");
            if(server.isFullLobby()) server.deregisterAllConnections();
            else server.deregisterConnection(this);
        }
    }
}
