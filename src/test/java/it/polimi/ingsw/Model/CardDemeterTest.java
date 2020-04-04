package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardDemeterTest {

    Grid grid;
    Worker worker = new Worker();
    Player player = new Player("Alberto");
    Deck deck = Deck.getDeck();
    Card card = new Card("Demeter");
    Tile currentTile;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    BuildAction buildAction;
    BuildAction buildAction2;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        grid.createGrid(5, 5);
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
        action = actionOrder.get(2);
        assertTrue(action instanceof BuildAction);
        buildAction2 = (BuildAction) action;
    }

    @AfterEach
    void tearDown() {
        PlayersManager.getPlayersManager().deletePlayer(player);
        deck.deleteAllCards();
        grid.destroyGrid();
    }

    @Test
    void testDemeter(){
        System.out.println("TEST: I'm testing Demeter Card");
        moveAction.move(worker, grid.getTiles().get(1));
        buildAction.build(worker,currentTile);
        buildAction2.build(worker,grid.getTiles().get(5));
        assertEquals(1, grid.getTiles().get(5).getLevel());
        assertEquals(1, currentTile.getLevel());
        buildAction.build(worker,grid.getTiles().get(5));
        assertEquals(2, grid.getTiles().get(5).getLevel());
        buildAction2.build(worker,grid.getTiles().get(5));
        assertEquals(2, grid.getTiles().get(5).getLevel());
    }

}