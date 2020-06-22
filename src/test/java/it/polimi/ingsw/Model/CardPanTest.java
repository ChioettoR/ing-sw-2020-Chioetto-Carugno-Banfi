package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardPanTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Marcello");
    private final Card card = new Card("Pan", CardsBuilder.GodPower.MoveDownToWin);
    private MoveAction moveAction;
    private BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid.createGrid(5,5);
        deck.addCard(card);
        playersManager.addPlayer(player);
        player.setWorker(worker);
        player.setCard(card);
        Tile currentTile = grid.getTiles().get(0);
        currentTile.setWorker(worker);
        worker.setPosition(currentTile);
        new CardsBuilder().createAction(card);
        ArrayList<Action> actionOrder = card.getActionOrder();
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

    /**
     * Testing Pan card
     */
    @Test
    void testPan() {
        System.out.println("TEST: I'm testing Pan Card");
        //I'm building and moving the worker in a tile with building level:3
        buildAction.build(worker, grid.getTiles().get(1));
        moveAction.move(worker, grid.getTiles().get(1));
        buildAction.build(worker, grid.getTiles().get(2));
        buildAction.build(worker, grid.getTiles().get(2));
        moveAction.move(worker, grid.getTiles().get(2));
        buildAction.build(worker, grid.getTiles().get(1));
        buildAction.build(worker, grid.getTiles().get(1));
        assertEquals(-1, playersManager.getPlayerWinnerID());
        //Try to win with the normal Win Condition from level:2 to level:3
        moveAction.move(worker, grid.getTiles().get(1));
        assertEquals(0, playersManager.getPlayerWinnerID());
        playersManager.setPlayerWinnerID(-1);
        //Try to win with the new Win Condition from level:2 to level:0
        moveAction.move(worker, grid.getTiles().get(0));
        buildAction.build(worker, grid.getTiles().get(5));
        moveAction.move(worker, grid.getTiles().get(5));
        assertEquals(0, playersManager.getPlayerWinnerID());
        playersManager.setPlayerWinnerID(-1);
        //Try to win with the normal Win Condition from level:1 to level:0
        moveAction.move(worker, grid.getTiles().get(0));
        assertEquals(-1, playersManager.getPlayerWinnerID());
    }
}