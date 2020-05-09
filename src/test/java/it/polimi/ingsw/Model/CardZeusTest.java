package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardZeusTest {

    Grid grid = Grid.getGrid();
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    Deck deck = Deck.getDeck();
    Worker worker = new Worker();
    Player player = new Player("Alberto");
    Card card = new Card("Zeus", CardsBuilder.GodPower.CanBuildBelow);
    Tile currentTile;
    ArrayList<Action> actionOrder = new ArrayList<>();
    MoveAction moveAction;
    BuildAction buildAction;

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
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Test
    void testZeus() {
        System.out.println("TEST: I'm testing Zeus Card");
        buildAction.build(worker, grid.getTiles().get(1));
        assertEquals(1, grid.getTiles().get(1).getLevel());
        buildAction.build(worker, currentTile);
        assertEquals(1, currentTile.getLevel());
        buildAction.build(worker, currentTile);
        assertEquals(2, currentTile.getLevel());
        buildAction.build(worker, currentTile);
        assertEquals(3, currentTile.getLevel());
        buildAction.build(worker, currentTile);
        assertEquals(3, currentTile.getLevel());
    }
}