package it.polimi.ingsw.View;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {
        try { new Client("127.0.0.1", 12345).runCLI(); }
        catch (IOException e) { System.err.println("Server unavailable"); }
    }
}
