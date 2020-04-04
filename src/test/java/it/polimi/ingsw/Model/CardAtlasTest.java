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

    @Test
    void testAtlas() {
        System.out.println("TEST: I'm testing Atlas Card");

        // Verifies if the worker can build a dome
        //I'm trying to build a dome on a base block
        buildAction.build(worker, buildTile);
        assertFalse(buildAction.canBuild(worker, buildTile, 4));
        buildAction.build(worker, buildTile, 4 );
        assertEquals(1, buildTile.getLevel());
        assertEquals(2, buildTile.getLevelsSize());
        //I'm trying to build a dome on the ground level
        Tile newBuildPosition = grid.getTiles().get(5);
        assertTrue(buildAction.canBuild(worker, newBuildPosition, 4));
        buildAction.build(worker, newBuildPosition, 4);
        assertEquals(4, newBuildPosition.getLevel());
        assertEquals(2, buildTile.getLevelsSize());

        // Verifies if the undo functionality for the building has been successful
        buildAction.undo();
        assertEquals(0, newBuildPosition.getLevel());
        assertEquals(1, newBuildPosition.getLevelsSize());

        buildAction.build(worker, newBuildPosition);
        assertEquals(1, buildTile.getLevel());
        assertEquals(2, buildTile.getLevelsSize());
        buildAction.undo();
    }
}
