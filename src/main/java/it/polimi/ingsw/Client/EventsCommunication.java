package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public interface EventsCommunication {

    void lobbyInfo(String lobbyName, int lobbySize);
    void waiting(boolean isWaiting);
    void endLogin();
    void message(int messageID);
    void deck(ArrayList<CardSimplified> cards);
    void card(CardSimplified card);
    void action(ArrayList<String> actions);
    void availableTiles(ArrayList<TileSimplified> tiles);
    void change(ArrayList<TileSimplified> tiles);
    void win(boolean youWin, String winnerName);
    void lose();
}
