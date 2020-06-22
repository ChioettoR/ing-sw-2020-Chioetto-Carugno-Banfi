package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StandardActionsTest {
    private Grid grid = Grid.getGrid();
    private PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final MoveActionStandard moveActionStandard = new MoveActionStandard();
    private final BuildActionStandard buildActionStandard = new BuildActionStandard();
    private final Player player = new Player("Marcello");
    private final Card card = new Card("Card", null);

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @BeforeEach
    public void setUp() {
        grid = Grid.getGrid();
        grid.createGrid(5,5);
        deck.addCard(card);
        playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        player.setWorker(worker);
        playersManager.getPlayerWithID(worker.getPlayerID()).setCard(card);
    }

    /**
     * Tests standard move and standard build of a worker and the undo function.
     */
    @Test
    public void actionTestStarter() {
        buildAndMoveTest();
    }

    public void buildAndMoveTest() {
        System.out.println("TEST: I'm testing buildings and movements standard");
        Tile currentPosition = grid.getTiles().get(0);
        worker.setPosition(currentPosition);
        Tile nextPosition = grid.getTiles().get(5);
        Tile buildPosition = grid.getTiles().get(1);

        // Verifies if the available tiles for the standard worker movement are correct
        assertEquals(grid.getNeighbours(worker.getPosition()), moveActionStandard.getAvailableTilesForAction(worker));

        // Verifies if the available tiles for the standard worker build are correct
        assertEquals(grid.getNeighbours(worker.getPosition()), buildActionStandard.getAvailableTilesForAction(worker));

        /*
         Verifies if the standard worker movement has been successful : the previous tile is empty, it doesn't contain
         the reference to the worker anymore, the next tile is not empty and now contains the reference
         to the worker
         */
        moveActionStandard.move(worker, nextPosition);
        assertEquals(nextPosition, worker.getPosition());
        assertNull(currentPosition.getWorker());
        assertTrue(currentPosition.isEmpty());
        assertEquals(worker, nextPosition.getWorker());
        assertFalse(nextPosition.isEmpty());

        // Verifies if the undo functionality for the movement has been successful
        moveActionStandard.undo();
        assertEquals(currentPosition, worker.getPosition());
        assertEquals(worker, currentPosition.getWorker());
        assertNull(nextPosition.getWorker());
        assertTrue(nextPosition.isEmpty());
        assertFalse(currentPosition.isEmpty());
        assertFalse(currentPosition.isEmpty());

        // Verifies if the standard worker building has been successful : the new level of the tile is updated
        // I'm trying to build a wrong level
        buildActionStandard.getAvailableTilesForAction(worker);
        ArrayList<Tile> expectedTiles = new ArrayList<>();
        expectedTiles.add(grid.getTiles().get(1));
        expectedTiles.add(grid.getTiles().get(5));
        expectedTiles.add(grid.getTiles().get(6));
        assertEquals(expectedTiles, buildActionStandard.getAvailableTilesForAction(worker));
        buildActionStandard.build(worker, buildPosition, 2);
        assertEquals(0, buildPosition.getLevel());
        assertEquals(1, buildPosition.getLevelsSize());
        // I'm trying to build a correct level
        buildActionStandard.build(worker, buildPosition);
        assertEquals(1, buildPosition.getLevel());
        assertEquals(2, buildPosition.getLevelsSize());

        // Verifies if the undo functionality for the building has been successful
        buildActionStandard.undo();
        assertEquals(0, buildPosition.getLevel());
        assertEquals(1, buildPosition.getLevelsSize());
    }
}