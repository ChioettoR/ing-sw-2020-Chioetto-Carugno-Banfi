package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GUI.GUIStagesManager;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) throws IOException {

        String ip = "127.0.0.1";
        int port = 1267;

        if(args.length!=0) try { new Client("127.0.0.1", 1267).runCLI(); }
        catch (IOException e) { System.err.println("Server unavailable"); }
        else GUIStagesManager.launch(ip, port);
        //else new Client("127.0.0.1", 1267).runCLI();
    }
}
