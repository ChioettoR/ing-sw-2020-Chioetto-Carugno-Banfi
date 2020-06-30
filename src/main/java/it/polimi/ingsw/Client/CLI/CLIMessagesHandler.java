package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;
import it.polimi.ingsw.Client.MessagesHandler;

import java.util.ArrayList;

public class CLIMessagesHandler implements MessagesHandler {
    /**
     * Prints the error messages
     * @param error string that contains error message
     */
    private void printError(String error) { System.out.println(Color.ANSI_RED.escape() + "ERROR: " + error + Color.RESET); }

    /**
     * Prints a generic message
     * @param message string that contains generic message
     */
    private void printGeneric(String message) { System.out.println(message); }

    /**
     * Handles the informations about the lobby
     * @param lobbyName name of the creator of the lobby
     * @param lobbySize size of the current lobby
     */
    @Override
    public void sendLobbyInfo(String lobbyName, int lobbySize) { printGeneric("Entered in " + lobbyName + "'s lobby of " + lobbySize + " players"); }

    /**
     * Handles an error message
     * @param error string that contains error message
     */
    @Override
    public void sendError(String error) {
        printError(error);
    }

    /**
     * Handles a generic message
     * @param message string that contains error message
     */
    @Override
    public void sendMessage(String message) { printGeneric(message); }

    /**
     * Handles a request message
     * @param request string that contains request message
     */
    @Override
    public void sendRequest(String request) {
        printGeneric(request);
    }

    /**
     * Handles a choose message
     * @param choose string that contains the choose message
     */
    @Override
    public void sendChooseMessage(String choose) {
        ArrayList<String> list = new ArrayList<>();
        list.add(choose);
        new CLIActionPrinter().printAction(list);
    }
}
