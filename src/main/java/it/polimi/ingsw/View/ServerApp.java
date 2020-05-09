package it.polimi.ingsw.View;

import java.io.IOException;

public class ServerApp {

    public static void main(String[] args) {
        try { new Server().run(); }
        catch (IOException e) { System.err.println("Unable to start the server"); }
    }
}
