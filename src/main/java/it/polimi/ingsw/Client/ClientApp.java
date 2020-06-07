package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GUI.GUIStagesManager;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) throws IOException {

        String ip = "25.74.135.68";
        int port = 2620;

        if(args.length!=0) try { new Client(ip, port).runCLI(); }
        catch (IOException e) { System.err.println("Server unavailable"); }
        else GUIStagesManager.launch(ip, port);
    }
}
