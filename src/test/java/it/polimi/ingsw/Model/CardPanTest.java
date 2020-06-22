package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardPanTest {

    Grid grid = Grid.getGrid();
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    Deck deck = Deck.getDeck();
    Worker worker = new Worker();
    Player player = new Player("Marcello");
    Card card = new Card("Pan", CardsBuilder.GodPower.MoveDownToWin);
    Tile currentTile;
    Tile buildTile;
    ArrayList<Action> actionOrder = new ArrayList<>();
    MoveAction moveAction;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid.createGrid(5,5);
        deck.addCard(card);
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