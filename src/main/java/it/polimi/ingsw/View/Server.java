package it.polimi.ingsw.View;

import it.polimi.ingsw.Communication.Communication;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Events.Server.EndLoginEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final ArrayList<Connection> connections = new ArrayList<>();
    private int lobbySize;
    private boolean fullLobby;
    private boolean lobbyCreated;
    private String lobbyName;
    private final Map<String, Connection> acceptedConnections = new LinkedHashMap<>();
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<Connection> waitingConnections = new ArrayList<>();

    public Map<String, Connection> getAcceptedConnections() {
        return acceptedConnections;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public boolean addName(String name) {
        if(!names.contains(name)) {
            names.add(name);
            return true;
        }
        return false;
    }

    public void awakeConnections() {
        ArrayList<Connection> waitingConnectionsCopy = new ArrayList<>(waitingConnections);
        for(Connection c : waitingConnectionsCopy) {
            c.setWaiting(false);
            waitingConnections.remove(c);
        }
    }

    public void sleepConnection(Connection connection) {
        waitingConnections.add(connection);
    }

    public int getLobbySize() {
        return lobbySize;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public boolean isLobbyCreated() {
        return lobbyCreated;
    }

    public boolean isFullLobby() {
        return fullLobby;
    }

    public int getPlayersLeft() {
        return lobbySize - acceptedConnections.size();
    }

    private synchronized void registerConnection(Connection c) {
        connections.add(c);
        System.out.println("New connection registered");
    }

    public synchronized void sendAll(Serializable serializable) throws IOException {
        for(Connection connection : acceptedConnections.values())
            connection.send(serializable);
    }

    public synchronized void deregisterConnection(Connection c) {
        System.out.println("Deregister client...");
        if(c.isFirstPlayer()) {
            if (lobbyCreated) deregisterAllConnections();
            else { if(connections.size()>1) connections.get(1).setFirstPlayer(true); }
        }
        waitingConnections.remove(c);
        names.remove(c.getName());
        connections.remove(c);
        acceptedConnections.values().remove(c);
        c.closeConnection();
        awakeConnections();
    }

    public synchronized  void deregisterAllConnections() {
        System.out.println("Deregister all clients...");
        for(Connection connection : connections) connection.closeConnection();
        connections.clear();
        waitingConnections.clear();
        acceptedConnections.clear();
        names.clear();
        lobbyCreated = false;
        fullLobby = false;
        System.out.println("Done!");
    }

    public synchronized void lobby(Connection c, String name) throws IOException {
        acceptedConnections.put(name, c);
        if (acceptedConnections.size() == lobbySize) {
            fullLobby=true;
            setup();
        }
    }

    private void setup() throws IOException {

        List<String> keys = new ArrayList<>(acceptedConnections.keySet());

        new Builder().build();
        StateManager stateManager = new StateManager();
        PositioningManager positioningManager = new PositioningManager(stateManager);
        ColorPoolManager colorPoolManager = new ColorPoolManager(stateManager);
        FirstPlayerManager firstPlayerManager = new FirstPlayerManager(stateManager, colorPoolManager);
        DrawCardManager drawCardManager = new DrawCardManager(stateManager, firstPlayerManager);
        ActionManager actionManager = new ActionManager(stateManager);
        SelectionWorkerManager selectionWorkerManager = new SelectionWorkerManager(stateManager, actionManager);
        Communication communication = new Communication(drawCardManager, positioningManager, selectionWorkerManager, actionManager, firstPlayerManager, colorPoolManager);
        Controller controller = new Controller(communication);

        for(int i=0; i<lobbySize; i++) {
            Connection connection = acceptedConnections.get(keys.get(i));
            Player player = new Player(keys.get(i));
            PlayersManager.getPlayersManager().addPlayer(player);
            RemoteView remoteView = new RemoteView(player.getID(), connection, controller);
            connection.setRemoteView(remoteView);

            PlayersManager.getPlayersManager().addObserver(remoteView);
            firstPlayerManager.addObserver(remoteView);
            drawCardManager.addObserver(remoteView);
            positioningManager.addObserver(remoteView);
            colorPoolManager.addObserver(remoteView);
            selectionWorkerManager.addObserver(remoteView);
            actionManager.addObserver(remoteView);
            stateManager.addObserver(remoteView);
        }

        sendAll(new MessageEvent(305));
        sendAll(new EndLoginEvent(getNames()));
        stateManager.setGameState(GameState.CHOOSING);
        drawCardManager.transition();
    }

    public void lobby(Connection c, String name, int lobbySize) throws IOException {
        this.lobbySize = lobbySize;
        lobbyName = name;
        lobbyCreated = true;
        lobby(c, name);
    }

    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {

        System.out.println("Server listening on port: " + port);
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this, connections.size()==0);
                registerConnection(connection);
                executor.submit(connection);
            }
            catch (IOException e) { System.err.println("Connection error!"); }
        }
    }
}
