package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;
import it.polimi.ingsw.Client.MessagesHandler;

public class CLIMessagesHandler implements MessagesHandler {

    private void printError(String error) { System.out.println(Color.ANSI_RED.escape() + "ERROR: " + error + Color.RESET); }
    private void printGeneric(String message) { System.out.println(message); }

    @Override
    public void sendLobbyInfo(String lobbyName, int lobbySize) { printGeneric("Entered in " + lobbyName + "'s lobby of " + lobbySize + " players"); }

    @Override
    public void sendError(String error) {
        printError(error);
    }

    @Override
    public void sendMessage(String message) { printGeneric(message); }

    @Override
    public void sendRequest(String request) {
        printGeneric(request);
    }
}
