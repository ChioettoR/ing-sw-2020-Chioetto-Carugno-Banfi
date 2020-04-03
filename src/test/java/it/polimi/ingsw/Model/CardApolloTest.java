package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardApolloTest {

    Grid grid = Grid.getGrid();
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Worker worker2 = new Worker();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Deck deck = Deck.getDeck();
    Card card = new Card("Apollo", true, false);
    Card card1 = new Card("Tonino", false, false);
    Tile currentTile;
    Tile currentTile1;
    Tile currentTile2;
    ActionOrder actionOrder = new ActionOrder();
    MoveAction moveAction;
    BuildAction buildAction;

    @BeforeEach
    void setUp() {
        grid.createGrid(5, 5);
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        player.setWorker(worker);
        player1.setWorker(worker1);
        player.setWorker(worker2);
        player.setCard(card);
        player1.setCard(card1);
        currentTile = grid.getTiles().get(0);
        currentTile1 = grid.getTiles().get(6);
        currentTile2 = grid.getTiles().get(5);
        worker.setPosition(currentTile);
        worker1.setPosition(currentTile1);
        worker2.setPosition(currentTile2);
        currentTile.setWorker(worker);
        currentTile1.setWorker(worker1);
        currentTile2.setWorker(worker2);
        new CardsBuilder().createAction(card);
        actionOrder = card.getActionOrder();
        Action action = actionOrder.getActions().get(0);
        assertTrue(action instanceof MoveAction);
        moveAction = (MoveAction) action;
        action = actionOrder.getActions().get(1);
        assertTrue(action instanceof BuildAction);
        buildAction = (BuildAction) action;
    }

    @AfterEach
    void tearDown() {
        PlayersManager.getPlayersManager().deletePlayer(player);
        PlayersManager.getPlayersManager().deletePlayer(player1);
        Deck.getDeck().deleteAllCards();
        Grid.getGrid().destroyGrid();
    }

    @Test
    void testApollo() {
        System.out.println(moveAction.getAvailableTilesForAction(worker));
        moveAction.getAvailableTilesForAction(worker).stream().forEach(tile -> System.out.println(tile.getX()));
        moveAction.getAvailableTilesForAction(worker).stream().forEach(tile -> System.out.println(tile.getY()));
        moveAction.move(worker,currentTile1);
        assertEquals(currentTile, worker1.getPosition());
        assertEquals(currentTile1, worker.getPosition());
        buildAction.build(worker, grid.getTiles().get(1), 1);
        buildAction.build(worker, grid.getTiles().get(1), 2);
        buildAction.build(worker, grid.getTiles().get(1), 3);
        worker1.setPosition(grid.getTiles().get(1));
        moveAction.move(worker,grid.getTiles().get(1));
        assertEquals(worker.getPosition(), currentTile1);
        assertEquals(worker1.getPosition(), grid.getTiles().get(1));
        worker1.setPosition(currentTile);
        worker.setPosition(grid.getTiles().get(1));
        moveAction.move(worker, currentTile);
        assertEquals(worker.getPosition(), currentTile);
        assertEquals(worker1.getPosition(), grid.getTiles().get(1));
    }
}
