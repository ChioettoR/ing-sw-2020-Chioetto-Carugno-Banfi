package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardChronusTest {

    Grid grid;
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Player player = new Player("Alberto");
    Deck deck = Deck.getDeck();
    Card card = new Card("Chronus", true);
    Tile currentTile;
    ArrayList<Action> actionOrder = new ArrayList<Action>();
    MoveAction moveAction;
    BuildAction buildAction;
    int completeTowersNumber;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        grid.createGrid(5, 5);
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        player.setWorker(worker);
        deck.addCard(card);
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

    public void buildCompleteTower(Worker worker, Tile tileWhereBuildCompleteTower) {
        for(int i = 0; i<4; i++)
            buildAction.build(worker, tileWhereBuildCompleteTower);
        completeTowersNumber++;
        assertEquals(4, tileWhereBuildCompleteTower.getLevel());
        assertEquals(5, tileWhereBuildCompleteTower.getLevelsSize());
        assertEquals(completeTowersNumber, grid.getCompleteTowersCount());
    }


    @Test
    void testChronus() {
        System.out.println("TEST: I'm testing Chronus Card");
        moveAction.move(worker, grid.getTiles().get(6));
        buildCompleteTower(worker,currentTile);
        buildCompleteTower(worker, grid.getTiles().get(1));
        buildCompleteTower(worker, grid.getTiles().get(2));
        buildCompleteTower(worker, grid.getTiles().get(5));
        buildCompleteTower(worker, grid.getTiles().get(7));
        buildCompleteTower(worker, grid.getTiles().get(10));
    }
}