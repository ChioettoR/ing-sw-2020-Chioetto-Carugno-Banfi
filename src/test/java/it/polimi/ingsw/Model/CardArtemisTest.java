package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardArtemisTest {

    Grid grid;
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Deck deck = Deck.getDeck();
    Card card = new Card("Artemis");
    Card card1 = new Card("Carlo");
    Tile currentTile;
    Tile currentTile1;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    MoveAction moveAction2;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        grid.createGrid(5, 5);
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        player.setWorker(worker);
        player1.setWorker(worker1);
        player.setCard(card);
        player1.setCard(card1);
        currentTile = grid.getTiles().get(0);
        currentTile1 = grid.getTiles().get(6);
        worker.setPosition(currentTile);
        worker1.setPosition(currentTile1);
        currentTile.setWorker(worker);
        currentTile1.setWorker(worker1);
        new CardsBuilder().createAction(card);
        actionOrder = card.getActionOrder();
        Action action = actionOrder.get(0);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.get(1);
        assertTrue(action instanceof MoveAction);
        moveAction2 = (MoveAction) action;
        action = actionOrder.get(2);
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
     * Testing the card Artemis
     */
    @Test
    void testArtemis(){
        System.out.println("TEST: I'm testing Artemis Card");
        ArrayList<Tile> actualTiles = moveAction.getAvailableTilesForAction(worker);
        ArrayList<Tile> expectedTiles = new ArrayList<Tile>();
        expectedTiles.add(grid.getTiles().get(1));
        expectedTiles.add(grid.getTiles().get(5));
        // Check the available tiles for the move of worker
        assertEquals(expectedTiles, actualTiles);

        // Reset of the expected tiles of worker for a new check
        expectedTiles.clear();
        moveAction.move(worker, grid.getTiles().get(1));
        actualTiles = moveAction2.getAvailableTilesForAction(worker);
        expectedTiles.add(grid.getTiles().get(2));
        expectedTiles.add(grid.getTiles().get(5));
        expectedTiles.add(grid.getTiles().get(7));
        // Check the available tiles for the move of worker
        assertEquals(expectedTiles, actualTiles);

        moveAction2.move(worker, grid.getTiles().get(2));
        // Check of the second move in a row of Artemis
        assertEquals(grid.getTiles().get(2), worker.getPosition());

        moveAction.move(worker, grid.getTiles().get(1));
        moveAction2.move(worker, grid.getTiles().get(2));
        // Trying to do two moves in a row and go back in the same place where we started (2nd move locked up by the effect of the card)
        assertEquals(grid.getTiles().get(1), worker.getPosition());

        buildAction.build(worker, currentTile);
        moveAction.move(worker, currentTile);
        moveAction2.move(worker, grid.getTiles().get(5));
        // Testing if the worker can move from different levels(in this case from level:1 to level:0)
        assertEquals(grid.getTiles().get(5), worker.getPosition());
    }
}