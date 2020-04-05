package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardMinotaurTest {

    Grid grid;
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Worker worker2 = new Worker();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Deck deck;
    Card card = new Card("Minotaur", true, false);
    Card card1 = new Card("Philip");
    Tile currentTile;
    Tile currentTile1;
    Tile currentTile2;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        deck = Deck.getDeck();
        deck.addCard(card);
        deck.addCard(card1);
        grid.createGrid(5, 5);
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        player.setWorker(worker);
        player1.setWorker(worker1);
        player.setWorker(worker2);
        player.setCard(card);
        player1.setCard(card1);
        currentTile = grid.getTiles().get(0);
        currentTile1 = grid.getTiles().get(6);
        currentTile2 = grid.getTiles().get(5);
        worker.setPosition(currentTile);
        worker1.setPosition(currentTile1);
        worker2.setPosition(currentTile2);
        currentTile.setWorker(worker);
        currentTile1.setWorker(worker1);
        currentTile2.setWorker(worker2);
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
        PlayersManager.getPlayersManager().deletePlayer(player);
        PlayersManager.getPlayersManager().deletePlayer(player1);
        deck.deleteAllCards();
        grid.destroyGrid();
    }
    @Test
    void testMinotaur() {
        System.out.println("TEST: I'm testing Minotaur Card");
        moveAction.move(worker,currentTile2);
        assertEquals(currentTile, worker.getPosition());
        assertEquals(currentTile2, worker2.getPosition());
        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(7));
        buildAction.build(worker, grid.getTiles().get(11));
        buildAction.build(worker, grid.getTiles().get(11));
        buildAction.build(worker, grid.getTiles().get(11));
        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(6));
        assertEquals(worker.getPosition(), grid.getTiles().get(6));
        assertEquals(worker1.getPosition(),grid.getTiles().get(11));
        assertEquals(3, worker1.getPosition().getLevel());
        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker1, grid.getTiles().get(6));
        buildAction.build(worker2,grid.getTiles().get(11));
        moveAction.move(worker,grid.getTiles().get(6));
        assertEquals(grid.getTiles().get(1), worker.getPosition());
        assertEquals(grid.getTiles().get(6), worker1.getPosition());
        moveAction.move(worker, grid.getTiles().get(0));
        moveAction.move(worker, grid.getTiles().get(6));
        assertEquals(grid.getTiles().get(6), worker.getPosition());
        assertEquals(grid.getTiles().get(12), worker1.getPosition());
        moveAction.undo();
        assertEquals(grid.getTiles().get(0), worker.getPosition());
        assertEquals(grid.getTiles().get(6), worker1.getPosition());
        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker1, grid.getTiles().get(0));
        moveAction.move(worker, grid.getTiles().get(0));
        assertEquals(grid.getTiles().get(0), worker1.getPosition());
        assertEquals(grid.getTiles().get(1), worker.getPosition());
    }
}