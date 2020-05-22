package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Model.ActionType;
import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public class GUIEventsCommunication implements EventsCommunication {

    @Override
    public void lobbyInfo(String lobbyName, int lobbySize) {

    }

    @Override
    public void waiting(boolean isWaiting) {

    }

    @Override
    public void endLogin(ArrayList<String> names) {

    }

    @Override
    public void message(int messageID) {

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
