package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardMedusaTest {

    Grid grid = Grid.getGrid();
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    Deck deck = Deck.getDeck();
    Worker workerA = new Worker();
    Worker workerA1 = new Worker();
    Worker workerF = new Worker();
    Worker workerF1 = new Worker();
    Worker workerM = new Worker();
    Player playerA = new Player("Alberto");
    Player playerF = new Player("Filippo");
    Player playerM = new Player("Marcello");
    Card card = new Card("Medusa", CardsBuilder.GodPower.PetrifyOpponents);
    ArrayList<Action> actionOrder = new ArrayList<>();
    MoveAction moveAction;
    BuildAction buildAction;
    RoundAction roundAction;

    @BeforeEach
    void setUp() {
        grid.createGrid(5, 5);
        playersManager.addPlayer(playerA);
        playersManager.addPlayer(playerF);
        playersManager.addPlayer(playerM);
        playerA.setWorker(workerA);
        playerA.setWorker(workerA1);
        playerF.setWorker(workerF);
        playerF.setWorker(workerF1);
        playerM.setWorker(workerM);
        deck.addCard(card);
        playerA.setCard(card);
        workerA.setPosition(grid.getTiles().get(0));
        workerA1.setPosition(grid.getTiles().get(5));
        workerF.setPosition(grid.getTiles().get(7));
        workerF1.setPosition(grid.getTiles().get(11));
        workerM.setPosition(grid.getTiles().get(2));
        new CardsBuilder().createAction(card);
        actionOrder = card.getActionOrder();
        Action action = actionOrder.get(0);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.get(1);
        assertTrue(action instanceof BuildAction);
        buildAction = (BuildAction) action;
        action = actionOrder.get(2);
        assertTrue(action instanceof RoundAction);
        roundAction = (RoundAction) action;
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Test
    void testMedusa() {
        System.out.println("TEST: I'm testing Medusa Card");
        PlayersManager.getPlayersManager().setCurrentWorker(workerA);
        buildAction.build(workerA, grid.getTiles().get(6));
        moveAction.move(workerA, grid.getTiles().get(6));
        buildAction.build(workerA, grid.getTiles().get(12));
        moveAction.move(workerA, grid.getTiles().get(12));
        buildAction.build(workerA, grid.getTiles().get(6));
        moveAction.move(workerA, grid.getTiles().get(6));
        grid.getTiles().get(11).setEmpty(true);
        workerF1.setPosition(grid.getTiles().get(12));
        grid.getTiles().get(12).setWorker(workerF1);
        roundAction.doAction();
        assertEquals(workerA1, grid.getTiles().get(5).getWorker());
        assertEquals(0, workerA1.getPosition().getLevel());
        assertEquals(1, grid.getTiles().get(2).getLevel());
        assertTrue(grid.getTiles().get(2).isEmpty());
        assertEquals(1, grid.getTiles().get(7).getLevel());
        assertTrue(grid.getTiles().get(7).isEmpty());
        assertEquals(2, grid.getTiles().get(12).getLevel());
        assertTrue(grid.getTiles().get(12).isEmpty());
    }
}