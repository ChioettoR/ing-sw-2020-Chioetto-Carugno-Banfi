package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardAtlasTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Alberto");
    private final Card card = new Card("Atlas", CardsBuilder.GodPower.CanBuildDome);
    private Tile buildTile;
    private BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid.createGrid(5,5);
        deck.addCard(card);
        playersManager.addPlayer(player);
        player.setWorker(worker);
        player.setCard(card);
        Tile currentTile = grid.getTiles().get(0);
        buildTile = grid.getTiles().get(1);
        currentTile.setWorker(worker);
        worker.setPosition(currentTile);
        new CardsBuilder().createAction(card);
        ArrayList<Action> actionOrder = card.getActionOrder();
        Action action = actionOrder.get(0);
        assertTrue(action instanceof MoveAction);
        MoveAction moveAction = (MoveAction) action;
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
     * Testing the Atlas card
     */
    @Test
    void testAtlas() {
        System.out.println("TEST: I'm testing Atlas Card");
        buildAction.build(worker, buildTile);
        //I'm trying to build a dome on a base block
        assertTrue(buildAction.canBuild(worker, buildTile, 4));

        buildAction.build(worker, buildTile, 4 );
        //Checking if the build of the dome doesn't modify the effective level of the building below it
        assertEquals(4, buildTile.getLevel());
        assertEquals(3, buildTile.getLevelsSize());

        Tile newBuildPosition = grid.getTiles().get(5);
        //I'm trying to build a dome on the ground level
        assertTrue(buildAction.canBuild(worker, newBuildPosition, 4));

        buildAction.build(worker, newBuildPosition, 4);
        //I'm checking the effective build of a dome on the ground level
        assertEquals(4, newBuildPosition.getLevel());
        assertEquals(2, newBuildPosition.getLevelsSize());

        buildAction.undo();
        // Verifies if the undo functionality for the building has been successful
        assertEquals(0, newBuildPosition.getLevel());
        assertEquals(1, newBuildPosition.getLevelsSize());

        buildAction.build(worker, newBuildPosition);
        assertEquals(1, newBuildPosition.getLevel());
        assertEquals(2, newBuildPosition.getLevelsSize());
        buildAction.undo();
    }
}
