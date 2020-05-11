package it.polimi.ingsw.View;

import it.polimi.ingsw.Communication.Communication;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT= 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final ArrayList<Connection> connections = new ArrayList<>();
    private int lobbySize;
    private boolean fullLobby;
    private boolean lobbyCreated;
    private String lobbyName;
    private final Map<String, Connection> acceptedConnections = new LinkedHashMap<>();
    private final ArrayList<String> names = new ArrayList<>();

    public boolean addName(String name) {
        if(!names.contains(name)) {
            names.add(name);
            return true;
        }
        return false;
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
        return lobbySize - connections.size();
    }

    private synchronized void registerConnection(Connection c) {
        connections.add(c);
        System.out.println("New connection registered");
    }

    public synchronized void sendAll(Serializable serializable) throws IOException {
        for(Connection connection : connections)
            connection.send(serializable);
    }

    public synchronized void deregisterConnection(Connection c) {
        names.remove(c.getName());
        c.closeConnection();
        connections.remove(c);
        acceptedConnections.values().remove(c);
        if(c.isFirstPlayer()) deregisterAllConnections();
    }

    public synchronized  void deregisterAllConnections() {
        if(!lobbyCreated) return;
        System.out.println("Deregister all clients...");
        for(Connection connection : connections) connection.closeConnection();
        connections.clear();
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
        DrawCardManager drawCardManager = new DrawCardManager(stateManager);
        PositioningManager positioningManager = new PositioningManager(stateManager);
        ActionManager actionManager = new ActionManager(stateManager);
        SelectionWorkerManager selectionWorkerManager = new SelectionWorkerManager(stateManager, actionManager);
        Communication communication = new Communication(drawCardManager, positioningManager, selectionWorkerManager, actionManager);
        Controller controller = new Controller(communication);

        for(int i=0; i<lobbySize; i++) {
            Connection connection = acceptedConnections.get(keys.get(i));
            Player player = new Player(keys.get(i));
            PlayersManager.getPlayersManager().addPlayer(player);
            RemoteView remoteView = new RemoteView(player.getID(), connection, controller);
            connection.setRemoteView(remoteView);

            drawCardManager.addObserver(remoteView);
            positioningManager.addObserver(remoteView);
            selectionWorkerManager.addObserver(remoteView);
            actionManager.addObserver(remoteView);
            stateManager.addObserver(remoteView);
            connection.startPing();
        }

        stateManager.setGameState(GameState.DRAWING);
        drawCardManager.transition();
    }

    public void lobby(Connection c, String name, int lobbySize) throws IOException {
        this.lobbySize = lobbySize;
        lobbyName = name;
        lobbyCreated = true;
        lobby(c, name);
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {

        System.out.println("Server listening on port: " + PORT);
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
