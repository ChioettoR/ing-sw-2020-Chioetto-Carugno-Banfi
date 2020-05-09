package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardMinotaurTest {

    Grid grid = Grid.getGrid();
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    Deck deck = Deck.getDeck();
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Worker worker2 = new Worker();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Card card = new Card("Minotaur", CardsBuilder.GodPower.CanPush);
    Card card1 = new Card("Philip", null);
    Tile currentTile;
    Tile currentTile1;
    Tile currentTile2;
    ArrayList<Action> actionOrder = new ArrayList<>();
    MoveAction moveAction;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        deck.addCard(card);
        deck.addCard(card1);
        grid.createGrid(5, 5);
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
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    /**
     * Testing the Minotaur card
     */
    @Test
    void testMinotaur() {
        System.out.println("TEST: I'm testing Minotaur Card");
        moveAction.move(worker,currentTile2);
        // Check that the worker can't move another worker of the same player
        assertEquals(currentTile, worker.getPosition());
        assertEquals(currentTile2, worker2.getPosition());

        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(7));
        buildAction.build(worker, grid.getTiles().get(11));
        buildAction.build(worker, grid.getTiles().get(11));
        buildAction.build(worker, grid.getTiles().get(11));
        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(6));
        // Trying to move the worker to tile no.6 knocking back the opponent's worker1 to tile no.11 expecting to be at level:3
        assertEquals(worker.getPosition(), grid.getTiles().get(6));
        assertEquals(worker1.getPosition(),grid.getTiles().get(11));
        assertEquals(3, worker1.getPosition().getLevel());

        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker1, grid.getTiles().get(6));
        buildAction.build(worker2,grid.getTiles().get(11));
        moveAction.move(worker,grid.getTiles().get(6));
        // Trying to move worker to tile no.6 but the worker1 in this tile can't be knocked back because the tile no.11 is level:4
        assertEquals(grid.getTiles().get(1), worker.getPosition());
        assertEquals(grid.getTiles().get(6), worker1.getPosition());

        moveAction.move(worker, grid.getTiles().get(0));
        moveAction.move(worker, grid.getTiles().get(6));
        // Move of worker from tile no.0 to tile no.6 knocking back the worker1 in diagonal to tile no.12
        assertEquals(grid.getTiles().get(6), worker.getPosition());
        assertEquals(grid.getTiles().get(12), worker1.getPosition());

        moveAction.undo();
        // Testing the undo functionality on this card
        assertEquals(grid.getTiles().get(0), worker.getPosition());
        assertEquals(grid.getTiles().get(6), worker1.getPosition());

        moveAction.move(worker, grid.getTiles().get(1));
        moveAction.move(worker1, grid.getTiles().get(0));
        moveAction.move(worker, grid.getTiles().get(0));
        // Trying to knock back worker1 out of the gaming board without success
        assertEquals(grid.getTiles().get(0), worker1.getPosition());
        assertEquals(grid.getTiles().get(1), worker.getPosition());
    }
}