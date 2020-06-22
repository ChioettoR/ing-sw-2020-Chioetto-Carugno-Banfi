package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardChronusTest {

    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker worker = new Worker();
    private final Player player = new Player("Alberto");
    private final Card card = new Card("Chronus", CardsBuilder.GodPower.CompleteTowersObserver, true);
    private Tile currentTile;
    private MoveAction moveAction;
    private BuildAction buildAction;
    private int completeTowersNumber;

    @BeforeEach
    void setUp() {
        grid.createGrid(5, 5);
        playersManager.addPlayer(player);
        player.setWorker(worker);
        deck.addCard(card);
        player.setCard(card);
        currentTile = grid.getTiles().get(0);
        worker.setPosition(currentTile);
        currentTile.setWorker(worker);
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
     *Testing Chronus card
     * @param tileWhereBuildCompleteTower : tile used to check the WinCondition of Chronos
     */
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
        assertEquals(-1, playersManager.getPlayerWinnerID());
        buildCompleteTower(worker,currentTile);
        assertEquals(-1, playersManager.getPlayerWinnerID());
        buildCompleteTower(worker, grid.getTiles().get(1));
        assertEquals(-1, playersManager.getPlayerWinnerID());
        buildCompleteTower(worker, grid.getTiles().get(2));
        assertEquals(-1, playersManager.getPlayerWinnerID());
        buildCompleteTower(worker, grid.getTiles().get(5));
        assertEquals(-1, playersManager.getPlayerWinnerID());
        buildCompleteTower(worker, grid.getTiles().get(7));
        assertEquals(0, playersManager.getPlayerWinnerID());
        buildCompleteTower(worker, grid.getTiles().get(10));
    }
}