package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardHephaestusTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Alberto");
    private final Card card = new Card("Hephaestus", CardsBuilder.GodPower.CanBuildTwice);
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
     * Testing the Hephaestus card
     */
    @Test
    void testHephaestus() {
        System.out.println("TEST: I'm testing Hephaestus Card");
        moveAction.move(worker, grid.getTiles().get(1));
        buildAction.build(worker,currentTile);
        buildAction2.build(worker,currentTile);
        // Checking if i can do the 2nd build on the same tile used in the 1st build from ground to level:2
        assertEquals(2, currentTile.getLevel());

        buildAction.build(worker,currentTile);
        buildAction2.build(worker,currentTile);
        // Checking if i can do the 2nd build on the same tile used in the 1st build from level:2 to level:3 and it doesn't build the dome
        assertEquals(3, currentTile.getLevel());

        buildAction.build(worker,currentTile);
        buildAction2.build(worker,currentTile);
        // Checking if i can do the 2nd build on the same tile used in the 1st build from level:3 to level:4 (dome)
        assertEquals(4, currentTile.getLevel());

        buildAction.build(worker,currentTile);
        // Checking that i can't build another level higher than level:4
        assertEquals(4, currentTile.getLevel());

        buildAction.build(worker, grid.getTiles().get(5));
        buildAction2.build(worker,grid.getTiles().get(6));
        // Trying to build in two different tiles in the same turn getting the 2nd build denied
        assertEquals(1, grid.getTiles().get(5).getLevel());
        assertEquals(0, grid.getTiles().get(6).getLevel());
    }
}