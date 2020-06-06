package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLIEventsCommunication;
import it.polimi.ingsw.Client.CLI.CLIStdinReader;
import it.polimi.ingsw.Client.GUI.GUIEventsCommunication;
import it.polimi.ingsw.CountdownInterface;
import it.polimi.ingsw.CountdownTask;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Events.Client.PongEvent;
import it.polimi.ingsw.Events.Server.PingEvent;
import it.polimi.ingsw.Observer.Client.ClientObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Timer;

public class Client implements ClientObserver, CountdownInterface {

    private final String ip;
    private final int port;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private EventsReader eventsReader;
    private EventsCommunication eventsCommunication;
    private CLIStdinReader cliStdinReader;
    public static final String ANSI_CYAN = "\u001B[36m";
    private Timer pongTimer;
    private CountdownTask pongTask;
    private boolean pongCountdownStarted = false;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public EventsCommunication getEventsCommunication() {
        return eventsCommunication;
    }

    public void closeConnection() {
        try{
            pongTimer.cancel();
            socket.close();
            oos.close();
            ois.close();
            if(cliStdinReader != null) System.exit(0);
            else ((GUIEventsCommunication) eventsCommunication).disconnection();
        }
        catch (IOException e) { System.err.println(e.getMessage()); }
    }

    public void read(Serializable serializable) {
        if(!checkPing(serializable)) eventsReader.read(serializable);
    }

    private boolean checkPing(Serializable serializable) {
        if(serializable instanceof PingEvent) {
            System.out.println("PING RECEIVED");
            int pingDelay = 7;
            if(pongCountdownStarted) pongTimer.cancel();
            update(new PongEvent());
            pongTimer = new Timer();
            pongTask = new CountdownTask(pingDelay,this);
            pongTimer.schedule(pongTask, 1000);
            pongCountdownStarted = true;
        }
        return false;
    }

    @Override
    public void update(ClientEvent event) {

        try { oos.writeObject(event); }
        catch (IOException e) {
            System.err.println("Unable to send messages to the server");
            closeConnection();
        }
    }

    public void run() throws IOException {
        connect();
        System.out.println("Connection established");
        System.out.println(ANSI_CYAN +
                        "███████╗ █████╗ ███╗   ██╗████████╗ ██████╗ ██████╗ ██╗███╗   ██╗██╗\n" +
                        "██╔════╝██╔══██╗████╗  ██║╚══██╔══╝██╔═══██╗██╔══██╗██║████╗  ██║██║\n" +
                        "███████╗███████║██╔██╗ ██║   ██║   ██║   ██║██████╔╝██║██╔██╗ ██║██║\n" +
                        "╚════██║██╔══██║██║╚██╗██║   ██║   ██║   ██║██╔══██╗██║██║╚██╗██║██║\n" +
                        "███████║██║  ██║██║ ╚████║   ██║   ╚██████╔╝██║  ██║██║██║ ╚████║██║\n" +
                        "╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝\n " + Color.RESET);

        Thread messagesRead = new Thread(() -> {
            Serializable object;
            try {
                while (true) {
                    object = (Serializable) ois.readObject();
                    read(object);
                }
            }
            catch (IOException e) { System.out.println("Connection closed from the server side"); }
            catch (ClassNotFoundException e) { System.err.println("Serializable class not found"); }
            finally { closeConnection(); }
        });

        messagesRead.start();
    }

    public void runCLI() throws IOException {
        cliStdinReader = new CLIStdinReader(this);
        eventsCommunication = new CLIEventsCommunication(cliStdinReader);
        eventsReader = new EventsReader(this, eventsCommunication);
        run();
        cliStdinReader.run();
    }

    public void setupGUI() {
        eventsCommunication = new GUIEventsCommunication();
        eventsReader = new EventsReader(this, eventsCommunication);
    }

    private void connect() throws IOException {
        socket = new Socket(this.ip, this.port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void countdownEnded() {
        System.err.println("Server is unreachable");
        closeConnection();
    }
}