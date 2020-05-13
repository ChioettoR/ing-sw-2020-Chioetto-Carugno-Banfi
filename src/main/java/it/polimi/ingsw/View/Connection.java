package it.polimi.ingsw.View;

import it.polimi.ingsw.CountdownInterface;
import it.polimi.ingsw.Events.Client.*;
import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Model.PlayersManager;

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
    private boolean firstPlayer;
    private final ArrayList<Integer> acceptedLobbySizes = new ArrayList<>(Arrays.asList(2, 3));
    private String name;
    Timer pingTimer;
    PingPongTask pingPongTask;
    private static final Object lock = new Object();

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

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
            if(pingTimer!=null) pingTimer.cancel();
            socket.close();
            oos.close();
            ois.close();
        }
        catch (IOException e) { System.err.println(e.getMessage()); }
        active = false;
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            checkLobbyCreated();
            if (fullLobby()) return;
            //TODO:Inserire messaggio di attesa

            synchronized (lock) {

                send(new WaitingEvent(false));

                if (fullLobby()) return;

                if (firstPlayer) send(new MessageEvent(303));
                else send(new LobbyInfoEvent(server.getLobbyName(), server.getLobbySize()));
                send(new MessageEvent(112));
                waitName();

                if (firstPlayer) {
                    send(new MessageEvent(113));;
                    int lobbySize = waitLobbySize();
                    server.lobby(this, name, lobbySize);
                    server.awakeConnections();
                } else server.lobby(this, name);

                int playersLeft = server.getPlayersLeft();
                if (playersLeft == 1) send(new MessageEvent(304));
                else if (playersLeft != 0) send(new MessageEvent(302));
                else sendAll(new EndLoginEvent());
            }

            waitInput();
        }
        catch(IOException | ClassNotFoundException e) {
            System.err.println("Connection interrupted");
            if(server.isFullLobby() && server.getAcceptedConnections().containsValue(this)) server.deregisterAllConnections();
            else server.deregisterConnection(this);
        }
    }

    private void checkLobbyCreated() throws IOException{

        if (!server.isLobbyCreated() && !firstPlayer) {
            send(new MessageEvent(413));
            send(new WaitingEvent(true));
            try { synchronized (Server.waiting) { Server.waiting.wait(); } }
            catch (InterruptedException e) { System.err.println(e.getMessage()); }
        }
    }

    public boolean fullLobby() throws IOException {
        if (server.isFullLobby()) {
            send(new MessageEvent(414));
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
                send(new MessageEvent(415));
                send(new MessageEvent(112));
                object = (Serializable) ois.readObject();
            }

            name = ((LoginNameEvent) object).getName();
            if(!server.addName(name)) send(new MessageEvent(416));
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
                send(new MessageEvent(417));
                send(new MessageEvent(113));
                object = (Serializable) ois.readObject();
            }

            lobbySize = ((LobbySizeEvent) object).getLobbySize();
            if(!acceptedLobbySizes.contains(lobbySize)) send(new MessageEvent(418));
            else validInput = true;
        }
        return lobbySize;
    }

    private void waitInput() throws IOException, ClassNotFoundException {

        while(isActive()) {
            Serializable read = (Serializable) ois.readObject();

            if(remoteView==null) {
                send(new MessageEvent(419));
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
