package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.MessagesHandler;

public class GUIMessagesHandler implements MessagesHandler {

    final GUIStagesManager stagesManager;

    public GUIMessagesHandler(GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;
    }

    @Override
    public void sendLobbyInfo(String lobbyName, int lobbySize) {
        stagesManager.lobbyInfo(lobbyName, lobbySize);
    }

    @Override
    public void sendError(String error) {
        stagesManager.readError(error);
    }

    @Override
    public void sendMessage(String message) {
        stagesManager.readMessage(message);
    }

    @Override
    public void sendRequest(String request) {
        stagesManager.readRequest(request);
    }

    @Override
    public void sendChooseMessage(String draw) { stagesManager.readDrawMessage(draw); }
}
