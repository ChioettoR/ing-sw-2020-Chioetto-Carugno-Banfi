package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoseTest implements ServerObserver {
    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Worker workerA1 = new Worker();
    private final Worker workerA2 = new Worker();
    private final Worker workerM1 = new Worker();
    private final Worker workerM2 = new Worker();
    private final Player player = new Player("Alberto");
    private final Player player1 = new Player("Marcello");
    private final StateManager stateManager = new StateManager();
    private final ActionManager actionManager = new ActionManager(stateManager);
    private int updateCounter;

    @BeforeEach
    void setUp() {
        new Builder().build();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        player.setWorker(workerA1);
        player.setWorker(workerA2);
        player1.setWorker(workerM1);
        player1.setWorker(workerM2);
        Card card = deck.getCardByName("Apollo");
        Card card1 = deck.getCardByName("Demeter");
        new CardsBuilder().createAction(card);
        new CardsBuilder().createAction(card1);
        player.setCard(card);
        player1.setCard(card1);
        workerM1.setPosition(grid.getTile(1,1));
        workerM2.setPosition(grid.getTile(2,1));
        workerA1.setPosition(grid.getTile(2,2));
        workerA2.setPosition(grid.getTile(1,2));
        grid.getTile(1,1).setWorker(workerM1);
        grid.getTile(2,1).setWorker(workerM2);
        grid.getTile(2,2).setWorker(workerA1);
        grid.getTile(1,2).setWorker(workerA2);
        playersManager.nextPlayerAndStartRound();
        playersManager.setCurrentWorker(workerA1);
        actionManager.addObserver(this);
        stateManager.addObserver(this);
    }

    @Test
    void actionsTest() throws IOException {
        actionManager.transition();
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Override
    public void update(ServerEvent serverEvent) throws IOException {

        if(serverEvent instanceof MessageEvent)
            return;

        if(updateCounter==0) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(1, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("MOVE", ((ActionEvent) serverEvent).getActions().get(0));
            updateCounter++;
            actionManager.actionSelect(0, "MOVE");
        }

        else if(updateCounter==1) {
            assertTrue(serverEvent instanceof AvailableTilesEvent);
            ArrayList<TileSimplified> tiles = ((AvailableTilesEvent) serverEvent).getTiles();
            assertEquals(7, tiles.size());
            assertEquals(1, tiles.get(0).getX());
            assertEquals(1, tiles.get(0).getY());
            assertEquals(2, tiles.get(1).getX());
            assertEquals(1, tiles.get(1).getY());
            assertEquals(3, tiles.get(2).getX());
            assertEquals(1, tiles.get(2).getY());
            assertEquals(3, tiles.get(3).getX());
            assertEquals(2, tiles.get(3).getY());
            assertEquals(1, tiles.get(4).getX());
            assertEquals(3, tiles.get(4).getY());
            assertEquals(2, tiles.get(5).getX());
            assertEquals(3, tiles.get(5).getY());
            assertEquals(3, tiles.get(6).getX());
            assertEquals(3, tiles.get(6).getY());
            updateCounter++;
            actionManager.move(0, 1, 1);
        }

        else if(updateCounter==2) {
            assertEquals(Grid.getGrid().getTile(1,1), workerA1.getPosition());
            assertEquals(Grid.getGrid().getTile(2,2), workerM1.getPosition());
            assertTrue(serverEvent instanceof ChangeEvent);
            assertEquals(2, ((ChangeEvent) serverEvent).getTiles().size());
            updateCounter++;
        }

        else if(updateCounter==3) {
            //Undo received
            updateCounter++;
            actionManager.sendActions();
        }

        else if(updateCounter==4) {
            assertTrue(serverEvent instanceof ChangeEvent);

            assertEquals(2, ((ChangeEvent) serverEvent).getTiles().size());
            assertEquals(1, ((ChangeEvent) serverEvent).getTiles().get(0).getX());
            assertEquals(1, ((ChangeEvent) serverEvent).getTiles().get(0).getY());
            assertEquals(2, ((ChangeEvent) serverEvent).getTiles().get(1).getX());
            assertEquals(2, ((ChangeEvent) serverEvent).getTiles().get(1).getY());
            assertEquals(1, ((ChangeEvent) serverEvent).getTiles().get(0).getWorkerSimplified().getLocalID());
            assertEquals(1, ((ChangeEvent) serverEvent).getTiles().get(1).getWorkerSimplified().getLocalID());

            assertEquals(Grid.getGrid().getTile(2,2), workerA1.getPosition());
            assertEquals(Grid.getGrid().getTile(1,1), workerM1.getPosition());
            updateCounter++;
        }
    }
}
