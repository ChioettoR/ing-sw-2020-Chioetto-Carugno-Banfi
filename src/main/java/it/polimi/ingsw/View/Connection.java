package it.polimi.ingsw.View;

import it.polimi.ingsw.CountdownInterface;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Events.Client.LobbySizeEvent;
import it.polimi.ingsw.Events.Client.LoginNameEvent;
import it.polimi.ingsw.Events.Client.PongEvent;
import it.polimi.ingsw.Events.Server.LobbyInfoEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.WaitingEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
    private static final Object lock = new Object();
    int maxNameLength = 16;
    Timer pingTimer;
    PingPongTask pingTask;
    boolean waiting;

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

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

    public synchronized void send(Serializable serializable) throws IOException {
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
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            startPing();

            checkLobbyCreated();
            if (fullLobby()) return;
            if (firstPlayer) send(new MessageEvent(303));
            else send(new LobbyInfoEvent(server.getLobbyName(), server.getLobbySize()));
            send(new MessageEvent(112));
            waitName();

            synchronized (lock) {

                if(fullLobby()) return;

                if (firstPlayer) {
                    send(new MessageEvent(113));;
                    int lobbySize = waitLobbySize();
                    server.lobby(this, name, lobbySize);
                    server.awakeConnections();
                }
                else server.lobby(this, name);

                int playersLeft = server.getPlayersLeft();
                if (playersLeft == 1) send(new MessageEvent(304));
                else if (playersLeft != 0) send(new MessageEvent(302));
            }
            waitInput();
        }
        catch(IOException | ClassNotFoundException e) {
            System.err.println("Connection interrupted");
            if(server.isFullLobby() && server.getAcceptedConnections().containsValue(this)) server.deregisterAllConnections();
            else { try { server.deregisterConnection(this); }
            catch (IOException ioException) { ioException.printStackTrace(); }
            }
        }
    }

    private void checkLobbyCreated() throws IOException, ClassNotFoundException {

        if (!server.isLobbyCreated() && !firstPlayer) {
            send(new MessageEvent(413));
            send(new WaitingEvent(true));
            waiting = true;
            server.sleepConnection(this);
            while(waiting) {
                Serializable object = (Serializable) ois.readObject();
                checkPong(object);
            }
            send(new WaitingEvent(false));
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
            while (checkPong(object)) { object = (Serializable) ois.readObject(); }

            while(!(object instanceof LoginNameEvent)) {
                send(new MessageEvent(415));
                send(new MessageEvent(112));
                object = (Serializable) ois.readObject();
                while (checkPong(object)) { object = (Serializable) ois.readObject(); }
            }

            name = ((LoginNameEvent) object).getName();
            if(name.length()>maxNameLength) send(new MessageEvent(424));
            else if(!server.addName(name)) send(new MessageEvent(416));
            else validName = true;
        }
    }

    private int waitLobbySize() throws IOException, ClassNotFoundException {

        int lobbySize = 0;
        boolean validInput = false;
        Serializable object;

        while(!validInput) {
            object = (Serializable) ois.readObject();
            while (checkPong(object)) { object = (Serializable) ois.readObject(); }

            while(!(object instanceof LobbySizeEvent)) {
                send(new MessageEvent(417));
                send(new MessageEvent(113));
                object = (Serializable) ois.readObject();
                while (checkPong(object)) { object = (Serializable) ois.readObject(); }
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
            while (checkPong(read)) { read = (Serializable) ois.readObject(); }

            if(remoteView==null)
                send(new MessageEvent(419));

            else {
                ClientEvent event = (ClientEvent) read;
                event.setPlayerID(remoteView.playerID);
                remoteView.sendMessage(event);
            }
        }
    }

    private boolean checkPong(Serializable serializable) {
        if(serializable instanceof PongEvent) {
            pingTask.cancelCountdown();
            System.out.println("PONG RECEIVED FROM " + toString());
            return true;
        }
        return false;
    }

    public void startPing() {
        int pongDelay = 2;
        int pingDelay = 5;
        pingTimer = new Timer();
        pingTask = new PingPongTask(pongDelay, this);
        pingTimer.schedule(pingTask, 0, pingDelay * 1000);
    }

    @Override
    public void countdownEnded() {
        System.err.println("CLIENT " + name + " UNREACHABLE");
        closeConnection();
    }
}
