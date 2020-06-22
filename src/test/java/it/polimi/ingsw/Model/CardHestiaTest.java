package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardHestiaTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Alberto");
    private final Card card = new Card("Hestia", CardsBuilder.GodPower.BuildTwiceNoPerimeter);
    private Tile currentTile;
    private MoveAction moveAction;
    private BuildAction buildAction;
    private BuildAction buildAction2;

    @BeforeEach
    void setUp() {
        grid.createGrid(5, 5);
        playersManager.addPlayer(player);
        player.setWorker(worker);
        player.setCard(card);
        currentTile = grid.getTiles().get(0);
        worker.setPosition(currentTile);
        currentTile.setWorker(worker);
        new CardsBuilder().createAction(card);
        ArrayList<Action> actionOrder = card.getActionOrder();
        Action action = actionOrder.get(0);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.get(1);
        assertTrue(action instanceof BuildAction);
        buildAction = (BuildAction) action;
        action = actionOrder.get(2);
        assertTrue(action instanceof BuildAction);
        buildAction2 = (BuildAction) action;
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    /**
     * Testing the Hestia card
     */
    @Test
    void testHestia() {
        System.out.println("TEST: I'm testing Hestia Card");
        moveAction.move(worker,grid.getTiles().get(1));
        buildAction.build(worker, currentTile);
        buildAction2.build(worker, grid.getTiles().get(6));
        // Building in non-perimetric tiles
        assertEquals(1, currentTile.getLevel());
        assertEquals(1, grid.getTiles().get(6).getLevel());

        buildAction.build(worker, currentTile);
        buildAction2.build(worker, grid.getTiles().get(5));
        // Building in a non-perimetric tile, then trying to build in a perimetric tile
        assertEquals(2, currentTile.getLevel());
        assertEquals(0, grid.getTiles().get(5).getLevel());

        ArrayList<Tile> expectedTiles = new ArrayList<>();
        moveAction.move(worker, grid.getTiles().get(6));
        expectedTiles.add(grid.getTiles().get(7));
        expectedTiles.add(grid.getTiles().get(11));
        expectedTiles.add(grid.getTiles().get(12));
        assertEquals(expectedTiles, buildAction2.getAvailableTilesForAction(worker));
        expectedTiles.clear();
        moveAction.move(worker, currentTile);
        expectedTiles.add(grid.getTiles().get(6));
        assertEquals(expectedTiles, buildAction2.getAvailableTilesForAction(worker));
        moveAction.move(worker, grid.getTiles().get(6));
        moveAction.move(worker, grid.getTiles().get(12));
        buildAction.build(worker, grid.getTiles().get(11));
        buildAction2.build(worker, grid.getTiles().get(16));
        assertEquals(1, grid.getTiles().get(11).getLevel());
        assertEquals(1, grid.getTiles().get(16).getLevel());
    }
}