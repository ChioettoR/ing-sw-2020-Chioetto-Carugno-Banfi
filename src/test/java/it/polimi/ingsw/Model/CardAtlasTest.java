package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardAtlasTest {

    Grid grid;
    Worker worker = new Worker();
    Player player = new Player("Alberto");
    Deck deck;
    Card card = new Card("Atlas");
    Tile currentTile;
    Tile buildTile;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        grid.createGrid(5,5);
        deck = Deck.getDeck();
        deck.addCard(card);
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        player.setWorker(worker);
        player.setCard(card);
        currentTile = grid.getTiles().get(0);
        buildTile = grid.getTiles().get(1);
        currentTile.setWorker(worker);
        worker.setPosition(currentTile);
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
        grid.destroyGrid();
        PlayersManager.getPlayersManager().deletePlayer(player);
        deck.deleteAllCards();
    }

    /**
     * Testing the Atlas card
     */
    @Test
    void testAtlas() {
        System.out.println("TEST: I'm testing Atlas Card");
        buildAction.build(worker, buildTile);
        //I'm trying to build a dome on a base block
        assertFalse(buildAction.canBuild(worker, buildTile, 4));

        buildAction.build(worker, buildTile, 4 );
        //Checking if the build of the dome doesn't modify the effective level of the building below it
        assertEquals(1, buildTile.getLevel());
        assertEquals(2, buildTile.getLevelsSize());

        Tile newBuildPosition = grid.getTiles().get(5);
        //I'm trying to build a dome on the ground level
        assertTrue(buildAction.canBuild(worker, newBuildPosition, 4));

        buildAction.build(worker, newBuildPosition, 4);
        //I'm checking the effective build of a dome on the ground level
        assertEquals(4, newBuildPosition.getLevel());
        assertEquals(2, buildTile.getLevelsSize());

        buildAction.undo();
        // Verifies if the undo functionality for the building has been successful
        assertEquals(0, newBuildPosition.getLevel());
        assertEquals(1, newBuildPosition.getLevelsSize());

        buildAction.build(worker, newBuildPosition);
        assertEquals(1, buildTile.getLevel());
        assertEquals(2, buildTile.getLevelsSize());
        buildAction.undo();
    }
}
