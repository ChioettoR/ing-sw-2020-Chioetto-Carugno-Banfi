package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLIEventsCommunication;
import it.polimi.ingsw.Client.CLI.CLIStdinReader;
import it.polimi.ingsw.Client.GUI.GUIEventsCommunication;
import it.polimi.ingsw.Client.GUI.GUIMain;
import it.polimi.ingsw.CountdownInterface;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Observer.Client.ClientObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client implements ClientObserver, CountdownInterface {

    private final String ip;
    private final int port;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    EventsReader eventsReader;
    EventsCommunication eventsCommunication;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public EventsCommunication getEventsCommunication() {
        return eventsCommunication;
    }

    public void closeConnection() {
        try{
            socket.close();
            oos.close();
            ois.close();
            System.exit(0);
        }
        catch (IOException e) { System.err.println(e.getMessage()); }
    }

    public void read(Serializable serializable) {
        eventsReader.read(serializable);
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
        CLIStdinReader cliStdinReader = new CLIStdinReader(this);
        eventsCommunication = new CLIEventsCommunication(cliStdinReader);
        eventsReader = new EventsReader(this, eventsCommunication);
        run();
        cliStdinReader.run();
    }

    public void runGUI() throws IOException {
        eventsCommunication = new GUIEventsCommunication();
        eventsReader = new EventsReader(this, eventsCommunication);
        GUIMain.launch();
        run();
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