package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardAthenaTest {
    Grid grid;
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Deck deck;
    Card card = new Card("Athena");
    Card card1 = new Card("Atlas");
    Tile currentTile;
    Tile currentTile1;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    BuildAction buildAction;
    ArrayList<Action> actionOrder1 = new ArrayList<Action>();
    MoveAction moveAction1;
    BuildAction buildAction1;

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
        assertTrue(action instanceof BuildAction);
        buildAction = (BuildAction) action;
        new CardsBuilder().createAction(card1);
        actionOrder1 = card1.getActionOrder();
        Action action1 = actionOrder1.get(0);
        assertTrue(action1 instanceof MoveAction);
        moveAction1 = (MoveAction) action1;
        action1 = actionOrder1.get(1);
        assertTrue(action1 instanceof BuildAction);
        buildAction1 = (BuildAction) action1;
    }

    @AfterEach
    void tearDown() {
        PlayersManager.getPlayersManager().deletePlayer(player);
        PlayersManager.getPlayersManager().deletePlayer(player1);
        deck.deleteAllCards();
        grid.destroyGrid();
    }
    @Test
    void testAthena() {
        System.out.println("TEST: I'm testing Athena Card");
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        buildAction.build(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(1));
        playersManager.getNextPlayers();
        buildAction1.build(worker1, grid.getTiles().get(5));
        ArrayList<Tile> expectedTiles = grid.getNeighbours(worker1.getPosition());
        expectedTiles.remove(grid.getTiles().get(1));
        expectedTiles.remove(grid.getTiles().get(5));
        assertEquals(expectedTiles, moveAction1.getAvailableTilesForAction(worker1));
        player1.resetActionsValues();
        ArrayList<Tile> expectedTiles1 = grid.getNeighbours(worker1.getPosition());
        expectedTiles1.remove(grid.getTiles().get(1));
        assertEquals(expectedTiles1, moveAction1.getAvailableTilesForAction(worker1));
    }
}