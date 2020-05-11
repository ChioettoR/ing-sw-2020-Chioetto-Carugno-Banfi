package it.polimi.ingsw.View;

import it.polimi.ingsw.CountdownInterface;
import it.polimi.ingsw.Events.Client.PongEvent;
import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Events.Client.LobbySizeEvent;
import it.polimi.ingsw.Events.Client.LoginNameEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

public class Connection implements Runnable, CountdownInterface {

    private final Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final Server server;
    private RemoteView remoteView;
    private boolean active = true;
    private final boolean firstPlayer;
    private final ArrayList<Integer> acceptedLobbySizes = new ArrayList<>(Arrays.asList(2, 3));
    private String name;
    Timer pingTimer;
    PingPongTask pingPongTask;

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

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
            pingTimer.cancel();
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

            if(unavailableLobby()) return;

            if(firstPlayer) send(new MessageEvent("No lobbies found. Create one"));
            else send(new MessageEvent("Entered in " + server.getLobbyName() + "'s lobby of " + server.getLobbySize() + " players"));

            send(new RequestEvent("What's your name?"));
            waitName();
            send(new MessageEvent("Welcome " + name));

            if(firstPlayer) {
                send(new RequestEvent("Insert lobby players number"));
                int lobbySize = waitLobbySize();
                server.lobby(this, name, lobbySize);
            }
            else server.lobby(this, name);

            int playersLeft = server.getPlayersLeft();
            if (playersLeft==1) send(new MessageEvent("Waiting for another player to connect"));
            else if(playersLeft!=0) send(new MessageEvent("Waiting for other " + playersLeft + " players to connect"));
            else sendAll(new EndLoginEvent());

            waitInput();
        }

        catch(IOException | ClassNotFoundException e) {
            System.err.println("Connection interrupted");
            if(server.isFullLobby()) server.deregisterAllConnections();
            else server.deregisterConnection(this);
        }
    }

    private boolean unavailableLobby () throws IOException{

        if (!server.isLobbyCreated() && !firstPlayer) {
            send(new ErrorEvent("Another player is already creating a lobby"));
            server.deregisterConnection(this);
            return true;
        }

        else if (server.isFullLobby()) {
            send(new ErrorEvent("The lobby is full"));
            server.deregisterConnection(this);
            return true;
        }
        return false;
    }

    private void waitName() throws IOException, ClassNotFoundException {

        boolean validName = false;
        Serializable object;

        while(!validName) {
            object = (Serializable) ois.readObject();

            while(!(object instanceof LoginNameEvent)) {
                send(new ErrorEvent("Invalid input. Name required."));
                send(new RequestEvent("Please, give me your name"));
                object = (Serializable) ois.readObject();
            }

            name = ((LoginNameEvent) object).getName();
            if(!server.addName(name)) send(new ErrorEvent("This name has already been chosen"));
            else validName = true;
        }
    }

    private int waitLobbySize() throws IOException, ClassNotFoundException {

        int lobbySize = 0;
        boolean validInput = false;
        Serializable object;

        while(!validInput) {
            object = (Serializable) ois.readObject();

            while(!(object instanceof LobbySizeEvent)) {
                send(new ErrorEvent("Invalid input. Number required."));
                send(new RequestEvent("Please, insert lobby players number"));
                object = (Serializable) ois.readObject();
            }

            lobbySize = ((LobbySizeEvent) object).getLobbySize();
            if(!acceptedLobbySizes.contains(lobbySize)) send(new ErrorEvent("You can only create a 2/3 players lobby"));
            else validInput = true;
        }
        return lobbySize;
    }

    private void waitInput() throws IOException, ClassNotFoundException {

        while(isActive()) {
            Serializable read = (Serializable) ois.readObject();

            if(remoteView==null) {
                send(new ErrorEvent("The game has not started yet. Please be patient"));
                return;
            }

            if(read instanceof PongEvent)
                pongReceived();

            ClientEvent event = (ClientEvent) read;
            event.setPlayerID(remoteView.playerID);
            remoteView.sendMessage(event);
        }
    }

    public void startPing() {
        pingTimer = new Timer();
        pingPongTask = new PingPongTask(this);
        pingTimer.schedule( pingPongTask, 10, 10000 );
    }

    private void pongReceived() {
        System.out.println("PONG RECEIVED " + remoteView.playerID);
        pingPongTask.cancelCountdown();
    }

    @Override
    public void countdownEnded() {
        System.out.println("Client " + remoteView.playerID + " unreachable");
        closeConnection();
    }
}
