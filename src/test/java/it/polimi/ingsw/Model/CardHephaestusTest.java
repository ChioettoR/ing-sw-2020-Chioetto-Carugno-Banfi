package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardHephaestusTest {

    Grid grid;
    Worker worker = new Worker();
    Player player = new Player("Alberto");
    Deck deck = Deck.getDeck();
    Card card = new Card("Hephaestus");
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
    void testHephaestus() {
        System.out.println("TEST: I'm testing Hephaestus Card");
        moveAction.move(worker, grid.getTiles().get(1));
        buildAction.build(worker,currentTile);
        buildAction2.build(worker,currentTile);
        assertEquals(2, currentTile.getLevel());
        buildAction.build(worker,currentTile);
        buildAction2.build(worker,currentTile);
        assertEquals(3, currentTile.getLevel());
        buildAction.build(worker,currentTile);
        buildAction2.build(worker,currentTile);
        assertEquals(4, currentTile.getLevel());
        buildAction.build(worker,currentTile);
        assertEquals(4, currentTile.getLevel());
        buildAction.build(worker, grid.getTiles().get(5));
        buildAction2.build(worker,grid.getTiles().get(6));
        assertEquals(1, grid.getTiles().get(5).getLevel());
        assertEquals(0, grid.getTiles().get(6).getLevel());
    }
}