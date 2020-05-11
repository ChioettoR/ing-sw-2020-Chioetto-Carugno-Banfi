package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.CLI.CLIMessagesReader;
import it.polimi.ingsw.Client.CLI.CLIStdinReader;
import it.polimi.ingsw.CountdownInterface;
import it.polimi.ingsw.CountdownTask;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Events.Client.PongEvent;
import it.polimi.ingsw.Observer.Client.ClientObserver;

import java.io.*;
import java.net.Socket;
import java.util.Timer;

public class Client implements ClientObserver, CountdownInterface {

    private final String ip;
    private final int port;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    private boolean login;
    Timer countdownTimer;
    CountdownTask countdownTask;
    boolean firstPing = true;

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
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

    @Override
    public void update(ClientEvent event) {

        try { oos.writeObject(event); }
        catch (IOException e) {
            System.out.println("Unable to send messages to the server");
            closeConnection();
        }
    }

    public void run(MessagesReader messagesReader) throws IOException {
        connect();
        System.out.println("Connection established");

        Thread messagesRead = new Thread(() -> {
            Serializable object;
            try {
                while (true) {
                    object = (Serializable) ois.readObject();
                    messagesReader.read(object);
                }
            }
            catch (IOException e) {
                System.out.println("Connection closed from the server side"); }
            catch (ClassNotFoundException e) { System.err.println("Serializable class not found"); }
            finally { closeConnection(); }
        });

        messagesRead.start();
    }

    public void runCLI() throws IOException {
        run(new CLIMessagesReader(this));
        new CLIStdinReader(this).run();
    }

    public void runGUI() throws IOException {

    }

    private void connect() throws IOException {
        socket = new Socket(this.ip, this.port);
        login = true;
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public void pingReceived() {
        if(!firstPing) countdownTimer.cancel();
        update(new PongEvent());
        countdownTimer = new Timer();
        countdownTask = new CountdownTask(20,this);
        countdownTimer.schedule(countdownTask, 0, 1000);
        firstPing = false;
    }

    @Override
    public void countdownEnded() {
        System.err.println("Server is unreachable");
        closeConnection();
    }
}