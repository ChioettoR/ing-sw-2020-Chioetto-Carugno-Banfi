package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTritonTest {

    Grid grid;
    Worker worker = new Worker();
    Player player = new Player("Alberto");
    Deck deck = Deck.getDeck();
    Card card;
    Tile currentTile;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        grid.createGrid(5, 5);
        new Builder().build();
        card = deck.getCardByName("Triton");
        PlayersManager playersManager = PlayersManager.getPlayersManager();
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
        PlayersManager.getPlayersManager().deletePlayer(player);
        deck.deleteAllCards();
        grid.destroyGrid();
    }

    /**
     * Testing Triton card
     */
    @Test
    void testTriton() {
        System.out.println("TEST: I'm testing Triton Card");
        moveAction.move(worker, grid.getTiles().get(6));
        ArrayList<Action> expectedActions = new ArrayList<Action>();
        expectedActions.add(moveAction);
        expectedActions.add(buildAction);
        assertEquals(expectedActions, card.getActionOrder());
        moveAction.move(worker, currentTile);
        expectedActions.clear();
        expectedActions.add(moveAction);
        expectedActions.add(moveAction);
        expectedActions.add(buildAction);
        assertEquals(expectedActions, card.getActionOrder());

        //Trying to move to a perimetric tile, being able to do a 2nd move in a row to another tile
        moveAction.move(worker, grid.getTiles().get(6));
        assertEquals(expectedActions, card.getActionOrder());
        moveAction.move(worker, grid.getTiles().get(1));
        expectedActions.clear();
        expectedActions.add(moveAction);
        expectedActions.add(moveAction);
        expectedActions.add(moveAction);
        expectedActions.add(buildAction);
        assertEquals(expectedActions, card.getActionOrder());
    }
}