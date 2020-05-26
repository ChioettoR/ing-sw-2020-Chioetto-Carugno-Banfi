package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Model.*;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;

public class GUIEventsCommunication implements EventsCommunication {

    GUIStagesManager stagesManager;
    GUIRoundStage guiRoundStage;
    GUILoginStage guiLoginStage;
    GUIDrawStage guiDrawStage;
    private MessagesReader messagesReader;

    public void setStagesManager(GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;
        messagesReader = new MessagesReader(new GUIMessagesHandler(stagesManager));
    }

    public void setGuiLoginStage(GUILoginStage guiLoginStage) {
        this.guiLoginStage = guiLoginStage;
    }

    public void setGuiDrawStage(GUIDrawStage guiDrawStage) {
        this.guiDrawStage = guiDrawStage;
    }

    public void setGuiRoundStage(GUIRoundStage guiRoundStage) {
        this.guiRoundStage = guiRoundStage;
    }

    @Override
    public void lobbyInfo(String lobbyName, int lobbySize) {
        messagesReader.lobbyInfo(lobbyName, lobbySize);
    }

    @Override
    public void waiting(boolean isWaiting) {
        if(!isWaiting) guiLoginStage.waitWake();
        else guiLoginStage.waitError();
    }

    @Override
    public void endLogin(ArrayList<String> names) {
        stagesManager.endLogin(names);
    }

    @Override
    public void message(int messageID) {
        stagesManager.messagesToDirect(messageID);
        messagesReader.read(messageID);
    }

    @Override
    public void deck(ArrayList<CardSimplified> cards) {
        Platform.runLater(() -> guiDrawStage.sendDeck(cards));
    }

    @Override
    public void card(CardSimplified card) {
        Platform.runLater(() -> guiDrawStage.sendCard());
    }

    @Override
    public void playerChosenCard(String playerName, String cardName) {
        stagesManager.playerChosenCard(playerName, cardName);
    }

    @Override
    public void action(ArrayList<String> actions) {
        guiRoundStage.showActions();
    }

    @Override
    public void availableTiles(ArrayList<TileSimplified> tiles, ActionType actionType) {
        guiRoundStage.setSelectedActionType(actionType);
        for(TileSimplified t : tiles) {
            int x = t.getX();
            int y = t.getY();
            Platform.runLater(() -> guiRoundStage.getGuiGridManager().highLight(x,y));
        }
    }

    @Override
    public void change(ArrayList<TileSimplified> tiles) {
        guiRoundStage.getGuiGridManager().deColor();
        for(TileSimplified t : tiles) {
            int level = t.getBuildLevel();
            int x = t.getX();
            int y = t.getY();
            Platform.runLater(() -> guiRoundStage.getGuiGridManager().build(level,x , y));
            WorkerSimplified w = t.getWorkerSimplified();
            if(w==null) guiRoundStage.getGuiGridManager().setWorkerNull(x, y);
            else guiRoundStage.getGuiGridManager().setWorker(w.getPlayerName(), w.getLocalID(), x, y);
        }
    }

    @Override
    public void win(boolean youWin, String winnerName) {

    }

    @Override
    public void lose() {

    }

    @Override
    public void infoEffect(String cardName) { }

    @Override
    public void fullDeck(ArrayList<CardSimplified> cards) {

    }

    public void disconnection() {
        Platform.runLater(() -> {
            try { stagesManager.setDisconnectedScene(); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }
}

