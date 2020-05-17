package it.polimi.ingsw.Client;

public interface MessagesHandler {

    void sendLobbyInfo(String lobbyName, int lobbySize);
    void sendError(String error);
    void sendMessage(String message);
    void sendRequest(String request);
}
