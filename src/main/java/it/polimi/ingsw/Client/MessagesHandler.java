package it.polimi.ingsw.Client;

import java.util.ArrayList;

public interface MessagesHandler {

    void sendLobbyInfo(String lobbyName, int lobbySize);
    void sendError(String error);
    void sendMessage(String message);
    void sendRequest(String request);
    void sendDrawMessage(String draw);
}
