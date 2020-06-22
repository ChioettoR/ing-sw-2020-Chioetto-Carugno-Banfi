package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardDemeterTest {

    final Grid grid = Grid.getGrid();
    final PlayersManager playersManager = PlayersManager.getPlayersManager();
    final Deck deck = Deck.getDeck();
    final Worker worker = new Worker();
    final Player player = new Player("Alberto");
    final Card card = new Card("Demeter", CardsBuilder.GodPower.CanBuildTwiceDifferent);
    Tile currentTile;
    ArrayList<Action> actionOrder = new ArrayList<>();
    MoveAction moveAction;
    BuildAction buildAction;
    BuildAction buildAction2;

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
        actionOrder = card.getActionOrder();
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
     * Testing Demeter Card
     */
    @Test
    void testDemeter(){
        System.out.println("TEST: I'm testing Demeter Card");
        moveAction.move(worker, grid.getTiles().get(1));
        buildAction.build(worker,currentTile);
        buildAction2.getAvailableTilesForAction(worker);
        ArrayList<Tile> expectedTiles = new ArrayList<>();
        expectedTiles.add(grid.getTiles().get(2));
        expectedTiles.add(grid.getTiles().get(5));
        expectedTiles.add(grid.getTiles().get(6));
        expectedTiles.add(grid.getTiles().get(7));
        // Checks if the available 2nd build move tiles contain the tile used in the 1st build
        assertEquals(expectedTiles, buildAction2.getAvailableTilesForAction(worker));

        buildAction2.build(worker,grid.getTiles().get(5));
        // Checks the effective build with the 2nd build
        assertEquals(1, grid.getTiles().get(5).getLevel());
        assertEquals(1, currentTile.getLevel());

        buildAction.build(worker,grid.getTiles().get(5));
        // Doing a build on the tile no.5
        assertEquals(2, grid.getTiles().get(5).getLevel());

        buildAction2.build(worker,grid.getTiles().get(5));
        // Trying to do another build on the tile no.5 and check the result
        assertEquals(2, grid.getTiles().get(5).getLevel());
    }

}