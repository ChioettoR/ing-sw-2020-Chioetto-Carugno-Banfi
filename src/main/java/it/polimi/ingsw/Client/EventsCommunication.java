package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.ActionType;
import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public interface EventsCommunication {

    void lobbyInfo(String lobbyName, int lobbySize);
    void waiting(boolean isWaiting);
    void endLogin(ArrayList<String> names);
    void message(int messageID);
    void deck(ArrayList<CardSimplified> cards);
    void card(CardSimplified card);
    void playerChosenCard(String playerName, String cardName);
    void action(ArrayList<String> actions);
    void availableTiles(ArrayList<TileSimplified> tiles, ActionType actionType);
    void change(ArrayList<TileSimplified> tiles);
    void win(String winnerName);
    void lose();
    void infoEffect(String cardName);
    void fullDeck(ArrayList<CardSimplified> cards);
    void firstPlayerSelection(ArrayList<String> names);
}
