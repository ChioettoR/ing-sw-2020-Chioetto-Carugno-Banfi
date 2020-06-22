package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardPrometheusTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Alberto");
    private final Card card = new Card("Prometheus", CardsBuilder.GodPower.BuildBeforeMove);
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
        Action action = actionOrder.get(1);
        assertTrue(action instanceof BuildAction);
        buildAction = (BuildAction) action;
        action = actionOrder.get(2);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.get(3);
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
     * Testing Prometheus card
     */
    @Test
    void testPrometheus() {
        System.out.println("TEST: I'm testing Prometheus Card");

        buildAction.getAvailableTilesForAction(worker);
        ArrayList<Tile> expectedTiles = new ArrayList<>();
        expectedTiles.add(grid.getTiles().get(1));
        expectedTiles.add(grid.getTiles().get(5));
        expectedTiles.add(grid.getTiles().get(6));
        assertEquals(expectedTiles, buildAction.getAvailableTilesForAction(worker));
        buildAction.build(worker, grid.getTiles().get(1));

        //Trying to Build first and then Move; checking if the successive Build(3rd action) isn't successful
        assertEquals(1, grid.getTiles().get(1).getLevel());
        moveAction.move(worker, grid.getTiles().get(1));
        assertEquals(currentTile, worker.getPosition());
        moveAction.move(worker, grid.getTiles().get(5));
        assertEquals(grid.getTiles().get(5), worker.getPosition());
        buildAction2.build(worker, grid.getTiles().get(10));
        assertEquals(1, grid.getTiles().get(10).getLevel());
        assertFalse(buildAction2.isActionLock());
        assertTrue(moveAction.isCantMoveUp());

        player.resetActionsValues();
        player.resetMoveUp();

        assertFalse(buildAction2.isActionLock());
        assertFalse(moveAction.isCantMoveUp());

        //Trying to Move first and then Build
        moveAction.move(worker, grid.getTiles().get(1));
        assertEquals(1, worker.getPosition().getLevel());
        buildAction2.build(worker, grid.getTiles().get(2));
        assertEquals(1, grid.getTiles().get(2).getLevel());
    }
}