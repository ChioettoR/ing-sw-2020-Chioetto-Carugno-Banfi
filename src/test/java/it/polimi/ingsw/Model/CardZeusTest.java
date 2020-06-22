package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardZeusTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Alberto");
    private final Card card = new Card("Zeus", CardsBuilder.GodPower.CanBuildBelow);
    private Tile currentTile;
    private BuildAction buildAction;

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
     *Testing Zeus card
     */
    @Test
    void testZeus() {
        System.out.println("TEST: I'm testing Zeus Card");
        buildAction.build(worker, grid.getTiles().get(1));

        //Trying to do a normal Build
        assertEquals(1, grid.getTiles().get(1).getLevel());
        buildAction.build(worker, currentTile);

        //Trying to do a Build under worker's tile
        assertEquals(1, currentTile.getLevel());
        buildAction.build(worker, currentTile);
        assertEquals(2, currentTile.getLevel());
        buildAction.build(worker, currentTile);
        assertEquals(3, currentTile.getLevel());
        buildAction.build(worker, currentTile);
        assertEquals(3, currentTile.getLevel());
    }
}