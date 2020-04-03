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
    Card card = new Card("Artemis", false, false);
    Card card1 = new Card("Tonino", false, false);
    Tile currentTile;
    Tile currentTile1;
    ActionOrder actionOrder = new ActionOrder();
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
        Action action = actionOrder.getActions().get(0);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.getActions().get(1);
        assertTrue(action instanceof MoveAction);
        moveAction2 = (MoveAction) action;
        action = actionOrder.getActions().get(2);
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
    void testArtemis(){
        ArrayList<Tile> actualTiles = moveAction.getAvailableTilesForAction(worker);
        ArrayList<Tile> expectedTiles = new ArrayList<Tile>();
        expectedTiles.add(grid.getTiles().get(1));
        expectedTiles.add(grid.getTiles().get(5));
        assertEquals(expectedTiles, actualTiles);
        expectedTiles.clear();
        moveAction.move(worker, grid.getTiles().get(1));
        actualTiles = moveAction2.getAvailableTilesForAction(worker);
        expectedTiles.add(grid.getTiles().get(2));
        expectedTiles.add(grid.getTiles().get(5));
        expectedTiles.add(grid.getTiles().get(7));
        assertEquals(expectedTiles, actualTiles);
        moveAction2.move(worker, grid.getTiles().get(2));
        assertEquals(grid.getTiles().get(2), worker.getPosition());
        moveAction.move(worker, grid.getTiles().get(1));
        moveAction2.move(worker, grid.getTiles().get(2));
        assertEquals(grid.getTiles().get(1), worker.getPosition());
        buildAction.build(worker, currentTile);
        moveAction.move(worker, currentTile);
        moveAction2.move(worker, grid.getTiles().get(5));
        assertEquals(grid.getTiles().get(5), worker.getPosition());
    }
}