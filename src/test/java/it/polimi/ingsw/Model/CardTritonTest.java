package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTritonTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Alberto");
    private Card card;
    private Tile currentTile;
    private MoveAction moveAction;
    private BuildAction buildAction;
    private RoundAction roundAction;

    @BeforeEach
    void setUp() {
        grid.createGrid(5, 5);
        new Builder().build();
        card = deck.getCardByName("Triton");
        playersManager.addPlayer(player);
        player.setWorker(worker);
        player.setCard(card);
        currentTile = grid.getTiles().get(0);
        worker.setPosition(currentTile);
        currentTile.setWorker(worker);
        new CardsBuilder().createAction(card);
        ArrayList<Action> actionOrder = card.getActionOrder();
        Action action = actionOrder.get(0);
        assertTrue(action instanceof RoundAction);
        roundAction = (RoundAction) action;
        action = actionOrder.get(1);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.get(2);
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
     * Testing Triton card
     */
    @Test
    void testTriton() {
        System.out.println("TEST: I'm testing Triton Card");
        moveAction.move(worker, grid.getTiles().get(6));
        ArrayList<Action> expectedActions = new ArrayList<>();
        expectedActions.add(roundAction);
        expectedActions.add(moveAction);
        expectedActions.add(buildAction);
        assertEquals(expectedActions, card.getActionOrder());
        moveAction.move(worker, currentTile);
        expectedActions.clear();
        expectedActions.add(roundAction);
        expectedActions.add(moveAction);
        expectedActions.add(moveAction);
        expectedActions.add(buildAction);
        assertEquals(expectedActions, card.getActionOrder());

        //Trying to move to a perimeter tile, being able to do a 2nd move in a row to another tile
        moveAction.move(worker, grid.getTiles().get(6));
        assertEquals(expectedActions, card.getActionOrder());
        moveAction.move(worker, grid.getTiles().get(1));
        expectedActions.clear();
        expectedActions.add(roundAction);
        expectedActions.add(moveAction);
        expectedActions.add(moveAction);
        expectedActions.add(moveAction);
        expectedActions.add(buildAction);
        assertEquals(expectedActions, card.getActionOrder());
    }
}