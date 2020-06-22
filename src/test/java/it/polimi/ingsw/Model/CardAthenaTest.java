package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardAthenaTest {
    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Worker worker1 = new Worker();
    private final Player player = new Player("Alberto");
    private final Player player1 = new Player("Marcello");
    private final Card card = new Card("Athena", CardsBuilder.GodPower.CantMoveUp);
    private final Card card1 = new Card("Atlas", CardsBuilder.GodPower.CanBuildDome);
    private MoveAction moveAction;
    private BuildAction buildAction;
    private RoundAction roundAction;
    private MoveAction moveAction1;
    private BuildAction buildAction1;

    @BeforeEach
    void setUp() {
        deck.addCard(card);
        deck.addCard(card1);
        grid.createGrid(5, 5);
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        player.setWorker(worker);
        player1.setWorker(worker1);
        player.setCard(card);
        player1.setCard(card1);
        Tile currentTile = grid.getTiles().get(0);
        Tile currentTile1 = grid.getTiles().get(6);
        worker.setPosition(currentTile);
        worker1.setPosition(currentTile1);
        currentTile.setWorker(worker);
        currentTile1.setWorker(worker1);
        new CardsBuilder().createAction(card);
        ArrayList<Action> actionOrder = card.getActionOrder();
        Action action = actionOrder.get(1);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.get(2);
        assertTrue(action instanceof BuildAction);
        buildAction = (BuildAction) action;
        action = actionOrder.get(3);
        assertTrue(action instanceof RoundAction);
        roundAction = (RoundAction) action;
        new CardsBuilder().createAction(card1);
        ArrayList<Action> actionOrder1 = card1.getActionOrder();
        Action action1 = actionOrder1.get(0);
        assertTrue(action1 instanceof MoveAction);
        moveAction1 = (MoveAction) action1;
        action1 = actionOrder1.get(1);
        assertTrue(action1 instanceof BuildAction);
        buildAction1 = (BuildAction) action1;
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    /**
     * Testing the Athena card
     */
    @Test
    void testAthena() throws IOException {
        System.out.println("TEST: I'm testing Athena Card");
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        buildAction.build(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(1));
        assertFalse(roundAction.isActionLock());
        roundAction.doAction();
        playersManager.getNextPlayers();
        buildAction1.build(worker1, grid.getTiles().get(5));
        ArrayList<Tile> expectedTiles = grid.getNeighbours(worker1.getPosition());
        expectedTiles.remove(grid.getTiles().get(1));
        expectedTiles.remove(grid.getTiles().get(5));
        //In this test worker builds and moves on level:1 forcing the other workers to stay on their level in the next move
        assertEquals(expectedTiles, moveAction1.getAvailableTilesForAction(worker1));

        //Testing the reset of the moveUp-lock
        player1.resetMoveUp();
        moveAction.move(worker, grid.getTiles().get(2));
        assertTrue(roundAction.isActionLock());
        moveAction.move(worker, grid.getTiles().get(1));
        assertFalse(roundAction.isActionLock());
        player1.resetActionsValues();
        ArrayList<Tile> expectedTiles1 = grid.getNeighbours(worker1.getPosition());
        expectedTiles1.remove(grid.getTiles().get(1));
        //After the reset the worker can now moveUp to a new level:x
        assertEquals(expectedTiles1, moveAction1.getAvailableTilesForAction(worker1));
    }
}