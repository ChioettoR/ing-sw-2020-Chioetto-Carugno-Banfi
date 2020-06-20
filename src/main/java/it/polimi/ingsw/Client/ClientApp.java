package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GUI.GUIStagesManager;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {

        if(args.length==0) GUIStagesManager.launch();
        else {
            try {
                if(args.length == 1) System.err.println("Specify a port number");
                else if (args.length == 2) new Client(args[0], Integer.parseInt(args[1])).runCLI();
            }
            catch (IOException e) { System.err.println("Server unavailable"); }
            catch (NumberFormatException e) { System.err.println("Invalid port number"); }
        }
    }
}
