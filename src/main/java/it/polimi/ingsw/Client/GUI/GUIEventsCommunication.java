package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Model.ActionType;
import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public class GUIEventsCommunication implements EventsCommunication {

    StagesManager stagesManager;
    //GUIRoundStage guiRoundStage;
    GUILoginStage guiLoginStage;
    private MessagesReader messagesReader;

    public void setStagesManager(StagesManager stagesManager) {
        this.stagesManager = stagesManager;
        messagesReader = new MessagesReader(new GUIMessagesHandler(stagesManager));
    }

    public void setGuiLoginStage(GUILoginStage guiLoginStage) {
        this.guiLoginStage = guiLoginStage;
    }

//    public void setGuiRoundStage(GUIRoundStage guiRoundStage) {
//        this.guiRoundStage = guiRoundStage;
//    }

    @Override
    public void lobbyInfo(String lobbyName, int lobbySize) {
        messagesReader.lobbyInfo(lobbyName, lobbySize);
    }

    @Override
    public void waiting(boolean isWaiting) {
        if(!isWaiting)
            guiLoginStage.waitWake();
        else
            guiLoginStage.waitError();
    }

    @Override
    public void endLogin(ArrayList<String> names) {
        stagesManager.endLogin(names);
    }

    @Override
    public void message(int messageID) {
        messagesReader.read(messageID);
    }

    @Override
    public void deck(ArrayList<CardSimplified> cards) {

    }

    @Override
    public void card(CardSimplified card) {

    }

    @Override
    public void playerChosenCard(String playerName, String cardName) {

    }

    @Override
    public void action(ArrayList<String> actions) {

    }

    @Override
    public void availableTiles(ArrayList<TileSimplified> tiles, ActionType actionType) {

    }

    @Override
    public void change(ArrayList<TileSimplified> tiles) {

    }

    @Override
    public void win(boolean youWin, String winnerName) {

    }

    @Override
    public void lose() {

    }

    @Override
    public void infoEffect(String cardName) {

    }
}
