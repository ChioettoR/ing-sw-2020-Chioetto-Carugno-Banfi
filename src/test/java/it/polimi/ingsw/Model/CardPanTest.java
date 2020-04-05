package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardPanTest {

    Grid grid;
    Worker worker = new Worker();
    Player player = new Player("Marcello");
    Deck deck;
    Card card = new Card("Pan");
    Tile currentTile;
    Tile buildTile;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        grid.createGrid(5,5);
        deck = Deck.getDeck();
        deck.addCard(card);
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        player.setWorker(worker);
        player.setCard(card);
        currentTile = grid.getTiles().get(0);
        buildTile = grid.getTiles().get(1);
        currentTile.setWorker(worker);
        worker.setPosition(currentTile);
        new CardsBuilder().createAction(card);
        actionOrder = card.getActionOrder();
        Action action = actionOrder.get(0);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.get(1);
        assertTrue(action instanceof BuildAction);
        buildAction = (BuildAction) action;
    }

    @AfterEach
    void tearDown() {
        grid.destroyGrid();
        PlayersManager.getPlayersManager().deletePlayer(player);
        deck.deleteAllCards();
    }

    @Test
    void testPan() {
        System.out.println("TEST: I'm testing Pan Card");

        buildAction.build(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(1));
        buildAction.build(worker, grid.getTiles().get(2));
        buildAction.build(worker, grid.getTiles().get(2));
        moveAction.move(worker, grid.getTiles().get(2));
        buildAction.build(worker, grid.getTiles().get(1));
        buildAction.build(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(0));
        buildAction.build(worker, grid.getTiles().get(5));
        moveAction.move(worker, grid.getTiles().get(5));
        moveAction.move(worker, grid.getTiles().get(0));
    }
}