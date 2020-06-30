package it.polimi.ingsw.Communication;

import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.util.ArrayList;

public class Communication implements CommunicationInterface {

    private final PickCardManager pickCardManager;
    private final PositioningManager positioningManager;
    private final SelectionWorkerManager selectionWorkerManager;
    private final FirstPlayerManager firstPlayerManager;
    private final ActionManager actionManager;
    private final ColorPoolManager colorPoolManager;

    public Communication(PickCardManager pickCardManager, PositioningManager positioningManager, SelectionWorkerManager selectionWorkerManager, ActionManager actionManager, FirstPlayerManager firstPlayerManager, ColorPoolManager colorPoolManager) {
        this.pickCardManager = pickCardManager;
        this.positioningManager = positioningManager;
        this.selectionWorkerManager = selectionWorkerManager;
        this.actionManager = actionManager;
        this.firstPlayerManager = firstPlayerManager;
        this.colorPoolManager = colorPoolManager;
    }


    @Override
    public void allPlayersCards(int playerID, ArrayList<String> cards) throws IOException {
        pickCardManager.allCardsChosen(playerID, cards);
    }

    @Override
    public void pick(int playerID, String cardName) throws IOException {
        pickCardManager.pick(playerID, cardName);
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
    public void pickColor(int playerID, PlayerColor playerColor) throws IOException {
        colorPoolManager.colorSelection(playerID, playerColor);
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
