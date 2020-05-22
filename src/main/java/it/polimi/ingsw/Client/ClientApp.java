package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GUI.StagesManager;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {

        String ip = "127.0.0.1";
        int port = 12345;

        try {
            if(args.length!=0) new Client("127.0.0.1", 12345).runCLI();
            else StagesManager.launch(ip, port);
        }
        catch (IOException e) { System.err.println("Server unavailable"); }
    }
}
