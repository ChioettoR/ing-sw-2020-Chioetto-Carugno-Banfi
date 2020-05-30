package it.polimi.ingsw.Communication;

import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.util.ArrayList;

public class Communication implements CommunicationInterface {

    DrawCardManager drawCardManager;
    PositioningManager positioningManager;
    SelectionWorkerManager selectionWorkerManager;
    FirstPlayerManager firstPlayerManager;
    ActionManager actionManager;

    public Communication(DrawCardManager drawCardManager, PositioningManager positioningManager, SelectionWorkerManager selectionWorkerManager, ActionManager actionManager, FirstPlayerManager firstPlayerManager) {
        this.drawCardManager = drawCardManager;
        this.positioningManager = positioningManager;
        this.selectionWorkerManager = selectionWorkerManager;
        this.actionManager = actionManager;
        this.firstPlayerManager = firstPlayerManager;
    }


    @Override
    public void allPlayersCards(int playerID, ArrayList<String> cards) throws IOException {
        drawCardManager.allCardsChosen(playerID, cards);
    }

    @Override
    public void pick(int playerID, String cardName) throws IOException {
        drawCardManager.pick(playerID, cardName);
    }

    @Override
    public void positioning(int playerID, int x, int y) throws IOException {
        positioningManager.positioning(playerID, x, y);
    }

    @Override
    public void firstPlayerChoose(int playerID, String name) throws IOException {
        firstPlayerManager.firstPlayerChosen(playerID, name);
    }

    @Override
    public void selection(int playerID, int workerID, String playerName) throws IOException {
        selectionWorkerManager.selection(playerID, workerID, playerName);
    }

    @Override
    public void actionSelect(int playerID, String actionName) throws IOException {
        actionManager.actionSelect(playerID, actionName);
    }

    @Override
    public void build(int playerID, int x, int y, int buildLevel) throws IOException {
        actionManager.build(playerID, x, y, buildLevel);
    }

    @Override
    public void move(int playerID, int x, int y) throws IOException {
        actionManager.move(playerID, x, y);
    }
}
