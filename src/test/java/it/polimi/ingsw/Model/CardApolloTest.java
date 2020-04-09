package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardApolloTest {

    Grid grid;
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Worker worker2 = new Worker();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Deck deck;
    Card card = new Card("Apollo");
    Card card1 = new Card("Carlo");
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

    /**
     * Testing the card Apollo
     */
    @Test
    void testApollo() {
        System.out.println("TEST: I'm testing Apollo Card");
        ArrayList<Tile> actualTiles = moveAction.getAvailableTilesForAction(worker);
        ArrayList<Tile> expectedTiles = new ArrayList<Tile>();
        expectedTiles.add(grid.getTiles().get(1));
        expectedTiles.add(currentTile1);
        // Check the available tiles for the worker movement
        assertEquals(expectedTiles, actualTiles);

        moveAction.move(worker,currentTile1);

        // Check worker movement from currentTile to currentTile1, forcing the worker1 to move from currentTile1 to currentTile
        assertEquals(currentTile, worker1.getPosition());
        assertEquals(currentTile1, worker.getPosition());

        buildAction.build(worker, grid.getTiles().get(1), 1);
        buildAction.build(worker, grid.getTiles().get(1), 2);
        buildAction.build(worker, grid.getTiles().get(1), 3);
        worker1.setPosition(grid.getTiles().get(1));
        moveAction.move(worker,grid.getTiles().get(1));

        // Check worker movement from currentTile1 to grid.getTiles().get(1) (Which is level 3) testing the Win condition of a move on a level 3 structure
        assertEquals(worker.getPosition(), currentTile1);
        assertEquals(worker1.getPosition(), grid.getTiles().get(1));

        worker1.setPosition(currentTile);
        worker.setPosition(grid.getTiles().get(1));
        moveAction.move(worker, currentTile);

        // Checking the opposite thing with workers and Win Condition inverted
        assertEquals(worker.getPosition(), currentTile);
        assertEquals(worker1.getPosition(), grid.getTiles().get(1));
    }
}