package it.polimi.ingsw.View;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private final String ip;
    private final int port;
    Socket socket;
    Scanner stdin;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    CLIStdinReader cliStdinReader;
    CLIMessagesReader cliMessagesReader;
    private boolean login;

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

    public void runCLI() throws IOException {
        connect();
        System.out.println("Connection established");

        stdin = new Scanner(System.in);
        cliStdinReader = new CLIStdinReader(this, stdin, oos);
        cliMessagesReader = new CLIMessagesReader(this, ois);

        Thread stdinRead = new Thread(() -> {
            try { cliStdinReader.run(); }
            catch (IllegalStateException | NoSuchElementException e) { System.out.println("Connection closed from the client side."); }
            catch (IOException e) { System.out.println("Unable to send messages to the server"); }
            finally { closeConnection(); }
        });

        Thread messagesRead = new Thread(() -> {
            try { cliMessagesReader.run(); }
            catch (IOException e) { System.out.println("Connection closed from the server side"); }
            catch (ClassNotFoundException e) { System.err.println("Serializable class not found"); }
            finally { closeConnection(); }
        });

        stdinRead.start();
        messagesRead.start();
    }

    private void connect() throws IOException {
        socket = new Socket(this.ip, this.port);
        login = true;
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }
}