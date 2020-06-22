package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

public class ActionManagerTest implements ServerObserver {

    final Grid grid = Grid.getGrid();
    final PlayersManager playersManager = PlayersManager.getPlayersManager();
    final Deck deck = Deck.getDeck();
    final Worker workerA1 = new Worker();
    final Worker workerA2 = new Worker();
    final Worker workerM1 = new Worker();
    final Player player = new Player("Alberto");
    final Player player1 = new Player("Marcello");
    Card card;
    Card card1;
    final StateManager stateManager = new StateManager();
    final ActionManager actionManager = new ActionManager(stateManager);
    int updateCounter;

    @BeforeEach
    void setUp() {
        new Builder().build();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        player.setWorker(workerA1);
        player.setWorker(workerA2);
        player1.setWorker(workerM1);
        card = deck.getCardByName("Artemis");
        card1 = deck.getCardByName("Demeter");
        new CardsBuilder().createAction(card);
        new CardsBuilder().createAction(card1);
        player.setCard(card);
        player1.setCard(card1);
        workerA1.setPosition(grid.getTile(1,1));
        workerA2.setPosition(grid.getTile(2,2));
        workerM1.setPosition(grid.getTile(3,3));
        grid.getTile(1,1).setWorker(workerA1);
        grid.getTile(2,2).setWorker(workerA2);
        grid.getTile(3,3).setWorker(workerM1);
        playersManager.nextPlayerAndStartRound();
        playersManager.setCurrentWorker(workerA1);
        actionManager.addObserver(this);
        stateManager.addObserver(this);
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Test
    void actionsTest() throws IOException {
        actionManager.transition();
    }

    @Override
    public void update(ServerEvent serverEvent) throws IOException {

        if(serverEvent instanceof MessageEvent)
            return;

        if(updateCounter==0 || updateCounter == 26) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(1, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("MOVE", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals(GameState.ACTIONSELECTING, stateManager.getGameState());
            updateCounter++;
            actionManager.actionSelect(0, "MOVE");
        }

        else if(updateCounter==1) {
            assertTrue(serverEvent instanceof AvailableTilesEvent);
            ArrayList<TileSimplified> tiles = ((AvailableTilesEvent) serverEvent).getTiles();
            assertEquals(2, tiles.size());
            assertEquals(2, tiles.get(0).getX());
            assertEquals(1, tiles.get(0).getY());
            assertEquals(1, tiles.get(1).getX());
            assertEquals(2, tiles.get(1).getY());
            updateCounter++;
            actionManager.move(0, 1, 2);
        }

        else if(updateCounter==2) {
            assertTrue(serverEvent instanceof ChangeEvent);
            ArrayList<TileSimplified> changedTiles = ((ChangeEvent) serverEvent).getTiles();
            assertEquals(2, changedTiles.size());
            assertEquals(1, changedTiles.get(0).getX());
            assertEquals(1, changedTiles.get(0).getY());
            assertEquals(1, changedTiles.get(1).getX());
            assertEquals(2, changedTiles.get(1).getY());
            updateCounter++;
        }

        else if(updateCounter==3) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("UNDO", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("CONFIRM", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(0, "CONFIRM");
        }

        else if(updateCounter==4) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("MOVE", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("BUILD", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(0, "BUILD");
        }

        else if(updateCounter==5) {
            assertTrue(serverEvent instanceof AvailableTilesEvent);
            ArrayList<TileSimplified> tiles = ((AvailableTilesEvent) serverEvent).getTiles();

            assertEquals(4, tiles.size());
            assertEquals(1, tiles.get(0).getX());
            assertEquals(1, tiles.get(0).getY());
            assertEquals(2, tiles.get(1).getX());
            assertEquals(1, tiles.get(1).getY());
            assertEquals(1, tiles.get(2).getX());
            assertEquals(3, tiles.get(2).getY());
            assertEquals(2, tiles.get(3).getX());
            assertEquals(3, tiles.get(3).getY());
            updateCounter++;
            actionManager.move(0, 2,1);
        }

        else if(updateCounter==6) {
            assertTrue(serverEvent instanceof ChangeEvent);
            ArrayList<TileSimplified> changedTiles = ((ChangeEvent) serverEvent).getTiles();

            assertEquals(2, changedTiles.size());
            assertEquals(2, changedTiles.get(0).getX());
            assertEquals(1, changedTiles.get(0).getY());
            assertEquals(1, changedTiles.get(1).getX());
            assertEquals(2, changedTiles.get(1).getY());
            updateCounter++;
        }

        else if(updateCounter==7) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("UNDO", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("CONFIRM", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(0, "UNDO");
        }

        else if(updateCounter==8) {
            assertTrue(serverEvent instanceof ChangeEvent);
            ArrayList<TileSimplified> changedTiles = ((ChangeEvent) serverEvent).getTiles();

            assertEquals(2, changedTiles.size());
            assertEquals(2, changedTiles.get(0).getX());
            assertEquals(1, changedTiles.get(0).getY());
            assertEquals(1, changedTiles.get(1).getX());
            assertEquals(2, changedTiles.get(1).getY());
            updateCounter++;
        }

        else if(updateCounter==9) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("MOVE", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("BUILD", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(0, "BUILD");
        }

        else if(updateCounter==10) {
            assertTrue(serverEvent instanceof AvailableTilesEvent);
            ArrayList<TileSimplified> tiles = ((AvailableTilesEvent) serverEvent).getTiles();

            assertEquals(4, tiles.size());
            assertEquals(1, tiles.get(0).getX());
            assertEquals(1, tiles.get(0).getY());
            assertEquals(2, tiles.get(1).getX());
            assertEquals(1, tiles.get(1).getY());
            assertEquals(1, tiles.get(2).getX());
            assertEquals(3, tiles.get(2).getY());
            assertEquals(2, tiles.get(3).getX());
            assertEquals(3, tiles.get(3).getY());
            updateCounter++;
            actionManager.move(0, 2,1);
        }

        else if(updateCounter==11) {
            assertTrue(serverEvent instanceof ChangeEvent);
            ArrayList<TileSimplified> changedTiles = ((ChangeEvent) serverEvent).getTiles();

            assertEquals(2, changedTiles.size());
            assertEquals(2, changedTiles.get(0).getX());
            assertEquals(1, changedTiles.get(0).getY());
            assertEquals(1, changedTiles.get(1).getX());
            assertEquals(2, changedTiles.get(1).getY());
            updateCounter++;
        }

        else if(updateCounter==12) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("UNDO", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("CONFIRM", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(0, "CONFIRM");
        }

        else if(updateCounter==13) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(1, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("BUILD", ((ActionEvent) serverEvent).getActions().get(0));
            updateCounter++;
            actionManager.actionSelect(0, "BUILD");
        }

        else if(updateCounter==14) {
            assertTrue(serverEvent instanceof AvailableTilesEvent);
            ArrayList<TileSimplified> tiles = ((AvailableTilesEvent) serverEvent).getTiles();
            assertEquals(4, tiles.size());
            assertEquals(1, tiles.get(0).getX());
            assertEquals(1, tiles.get(0).getY());
            assertEquals(3, tiles.get(1).getX());
            assertEquals(1, tiles.get(1).getY());
            assertEquals(1, tiles.get(2).getX());
            assertEquals(2, tiles.get(2).getY());
            assertEquals(3, tiles.get(3).getX());
            assertEquals(2, tiles.get(3).getY());
            updateCounter++;
            actionManager.build(0, 1, 1, 1);
        }

        else if(updateCounter==15) {
            assertTrue(serverEvent instanceof ChangeEvent);
            ArrayList<TileSimplified> changedTiles = ((ChangeEvent) serverEvent).getTiles();

            assertEquals(1, changedTiles.size());
            assertEquals(1, changedTiles.get(0).getX());
            assertEquals(1, changedTiles.get(0).getY());
            assertEquals(0, playersManager.getCurrentPlayer().getID());
            playersManager.setCurrentWorker(workerM1);
            actionManager.transition();
            updateCounter++;
        }

        else if(updateCounter==16) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("UNDO", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("CONFIRM", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(1, "CONFIRM");
        }

        else if(updateCounter==17) {

            assertEquals(1, playersManager.getCurrentPlayer().getID());
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(1, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("MOVE", ((ActionEvent) serverEvent).getActions().get(0));
            updateCounter++;
            actionManager.actionSelect(1, "MOVE");
        }

        else if(updateCounter==18) {
            assertTrue(serverEvent instanceof AvailableTilesEvent);
            ArrayList<TileSimplified> tiles = ((AvailableTilesEvent) serverEvent).getTiles();
            assertEquals(7, tiles.size());
            assertEquals(3, tiles.get(0).getX());
            assertEquals(2, tiles.get(0).getY());
            assertEquals(4, tiles.get(1).getX());
            assertEquals(2, tiles.get(1).getY());
            assertEquals(2, tiles.get(2).getX());
            assertEquals(3, tiles.get(2).getY());
            assertEquals(4, tiles.get(3).getX());
            assertEquals(3, tiles.get(3).getY());
            assertEquals(2, tiles.get(4).getX());
            assertEquals(4, tiles.get(4).getY());
            assertEquals(3, tiles.get(5).getX());
            assertEquals(4, tiles.get(5).getY());
            assertEquals(4, tiles.get(6).getX());
            assertEquals(4, tiles.get(6).getY());
            updateCounter++;
            actionManager.move(1, 3, 2);
        }

        else if(updateCounter==19) {
            assertTrue(serverEvent instanceof ChangeEvent);
            ArrayList<TileSimplified> changedTiles = ((ChangeEvent) serverEvent).getTiles();

            assertEquals(2, changedTiles.size());
            assertEquals(3, changedTiles.get(0).getX());
            assertEquals(2, changedTiles.get(0).getY());
            assertEquals(3, changedTiles.get(1).getX());
            assertEquals(3, changedTiles.get(1).getY());
            updateCounter++;
        }

        else if(updateCounter==20) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("UNDO", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("CONFIRM", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(1, "CONFIRM");
        }

        else if(updateCounter==21) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(1, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("BUILD", ((ActionEvent) serverEvent).getActions().get(0));
            updateCounter++;
            actionManager.actionSelect(1, "BUILD");
        }

        else if(updateCounter==22) {
            assertTrue(serverEvent instanceof AvailableTilesEvent);
            ArrayList<TileSimplified> tiles = ((AvailableTilesEvent) serverEvent).getTiles();
            assertEquals(6, tiles.size());
            assertEquals(3, tiles.get(0).getX());
            assertEquals(1, tiles.get(0).getY());
            assertEquals(4, tiles.get(1).getX());
            assertEquals(1, tiles.get(1).getY());
            assertEquals(4, tiles.get(2).getX());
            assertEquals(2, tiles.get(2).getY());
            assertEquals(2, tiles.get(3).getX());
            assertEquals(3, tiles.get(3).getY());
            assertEquals(3, tiles.get(4).getX());
            assertEquals(3, tiles.get(4).getY());
            assertEquals(4, tiles.get(5).getX());
            assertEquals(3, tiles.get(5).getY());
            updateCounter++;
            actionManager.build(1, 3, 1, -1);
        }

        else if(updateCounter==23) {
            assertTrue(serverEvent instanceof ChangeEvent);
            ArrayList<TileSimplified> changedTiles = ((ChangeEvent) serverEvent).getTiles();

            assertEquals(1, changedTiles.size());
            assertEquals(3, changedTiles.get(0).getX());
            assertEquals(1, changedTiles.get(0).getY());
            updateCounter++;
        }

        else if(updateCounter==24) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("UNDO", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("CONFIRM", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            actionManager.actionSelect(1, "CONFIRM");
        }

        else if(updateCounter==25) {
            assertTrue(serverEvent instanceof ActionEvent);
            assertEquals(2, ((ActionEvent) serverEvent).getActions().size());
            assertEquals("BUILD", ((ActionEvent) serverEvent).getActions().get(0));
            assertEquals("ENDROUND", ((ActionEvent) serverEvent).getActions().get(1));
            updateCounter++;
            assertEquals(1, playersManager.getCurrentPlayer().getID());
            actionManager.actionSelect(1, "ENDROUND");
            assertEquals(0, playersManager.getCurrentPlayer().getID());
            System.out.println("WORKS!");
            playersManager.setCurrentWorker(workerA2);
            actionManager.transition();
        }
    }
}
