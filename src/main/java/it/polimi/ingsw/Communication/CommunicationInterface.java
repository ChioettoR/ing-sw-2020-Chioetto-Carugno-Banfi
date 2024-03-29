package it.polimi.ingsw.Communication;

import it.polimi.ingsw.Model.PlayerColor;

import java.io.IOException;
import java.util.ArrayList;

public interface CommunicationInterface {
    void allPlayersCards(int playerID, ArrayList<String> cards) throws IOException;
    void pick(int playerID, String cardName) throws IOException;
    void positioning(int playerID, int x, int y) throws IOException;
    void firstPlayerChoose(int playerID, String name) throws IOException;
    void pickColor(int playerID, PlayerColor playerColor) throws  IOException;
    void selection(int playerID, int workerID, String playerName) throws IOException;
    void actionSelect(int playerID, String actionName) throws IOException;
    void build(int playerID, int x, int y, int buildLevel) throws IOException;
    void move(int playerID, int x, int y) throws IOException;
}
