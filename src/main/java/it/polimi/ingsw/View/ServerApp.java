package it.polimi.ingsw.View;

import java.io.IOException;

public class ServerApp {

    public static void main(String[] args) {
        try {
            if(args.length==0) System.err.println("Specify a port number");
            else if(args.length==1) new Server().run(Integer.parseInt(args[0]));
        }
        catch (IOException e) { System.err.println("Unable to start the server"); }
        catch (NumberFormatException e) { System.err.println("Invalid port number"); }
    }
}
