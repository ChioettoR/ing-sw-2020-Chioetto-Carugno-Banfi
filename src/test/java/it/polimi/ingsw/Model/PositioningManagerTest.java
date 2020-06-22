package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.ChangeEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.ServerEvent;
import it.polimi.ingsw.Observer.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PositioningManagerTest implements ServerObserver {

    final Grid grid = Grid.getGrid();
    final PlayersManager playersManager = PlayersManager.getPlayersManager();
    final Deck deck = Deck.getDeck();
    final Player player = new Player("Alberto");
    Card card;
    Card card1;
    Tile firstWorkerPosition;
    Tile secondWorkerPosition;
    Tile thirdWorkerPosition;
    Tile fourthWorkerPosition;
    final Player player1 = new Player("Franco");
    final CardsBuilder cardsBuilder = new CardsBuilder();
    final StateManager stateManager = new StateManager();
    final PositioningManager positioningManager = new PositioningManager(stateManager);
    int updateCounter;

    @BeforeEach
    void setUp() {
        new Builder().build();
        cardsBuilder.createCards();
        card = deck.getCardByName("Apollo");
        card1 = deck.getCardByName("Artemis");
        cardsBuilder.createAction(card);
        cardsBuilder.createAction(card1);
        playersManager.addPlayer(player);
        player.setCard(card);
        playersManager.addPlayer(player1);
        player1.setCard(card1);
        playersManager.setNextPlayerIndex(1);
        positioningManager.addObserver(this);
        firstWorkerPosition = grid.getTile(1, 1);
        secondWorkerPosition = grid.getTile(2, 1);
        thirdWorkerPosition = grid.getTile(3, 2);
        fourthWorkerPosition = grid.getTile(4, 2);
        stateManager.addObserver(this);
        stateManager.setGameState(GameState.POSITIONING);
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Test
    void positioningTest() throws IOException {
        assertTrue(firstWorkerPosition.isEmpty());
        positioningManager.positioning(0, firstWorkerPosition.getX(), firstWorkerPosition.getY());
    }

    @Override
    public void update(ServerEvent serverEvent) throws IOException {

        if(serverEvent instanceof MessageEvent) {
            if(((MessageEvent) serverEvent).getMessageID()==108 && serverEvent.getPlayerID()==playersManager.getCurrentPlayer().getID()) updateCounter++;
            else return;
        }

        if(updateCounter==0) {
            assertTrue(serverEvent instanceof ChangeEvent);
            checkWorkerPosition(player, firstWorkerPosition, 1);
            checkChanges(((ChangeEvent) serverEvent).getTiles(), player, firstWorkerPosition, 1);
            updateCounter++;
            positioningManager.positioning(0, secondWorkerPosition.getX(), secondWorkerPosition.getY());
        }

        if(updateCounter==1) {
            assertTrue(serverEvent instanceof ChangeEvent);
            checkWorkerPosition(player, secondWorkerPosition, 2);
            checkChanges(((ChangeEvent) serverEvent).getTiles(), player, secondWorkerPosition, 2);
            updateCounter++;
        }

        if(updateCounter==3) {
            assertEquals(player1.getID(), playersManager.getCurrentPlayer().getID());
            updateCounter++;
            positioningManager.positioning(1, thirdWorkerPosition.getX(), thirdWorkerPosition.getY());
        }

        if(updateCounter==4) {
            assertTrue(serverEvent instanceof ChangeEvent);
            checkWorkerPosition(player1, thirdWorkerPosition, 1);
            checkChanges(((ChangeEvent) serverEvent).getTiles(), player1, thirdWorkerPosition, 1);
            updateCounter++;
            positioningManager.positioning(1, fourthWorkerPosition.getX(), fourthWorkerPosition.getY());
        }

        if(updateCounter==5) {
            assertTrue(serverEvent instanceof ChangeEvent);
            checkWorkerPosition(player1, fourthWorkerPosition, 2);
            checkChanges(((ChangeEvent) serverEvent).getTiles(), player1, fourthWorkerPosition, 2);
            updateCounter++;
        }
    }

    //Check if the worker has been correctly placed
    private void checkWorkerPosition(Player currentPlayer, Tile workerPosition, int workerID) {
        assertFalse(workerPosition.isEmpty());
        assertEquals(currentPlayer.getWorkers().get(workerID-1), workerPosition.getWorker());
        assertEquals(currentPlayer.getWorkers().get(workerID-1).getPosition(), workerPosition);
        assertEquals(currentPlayer.getID(), workerPosition.getWorker().getPlayerID());
        assertEquals(workerID, workerPosition.getWorker().getLocalID());
    }

    private void checkChanges(ArrayList<TileSimplified> tilesChanged, Player currentPlayer, Tile workerPosition, int workerID) {
        assertEquals(1, tilesChanged.size());
        assertEquals(workerPosition.getX(), tilesChanged.get(0).getX());
        assertEquals(workerPosition.getY(), tilesChanged.get(0).getY());
        assertEquals(0, tilesChanged.get(0).getBuildLevel());
        assertEquals(workerID, tilesChanged.get(0).getWorkerSimplified().getLocalID());
        assertEquals(currentPlayer.getName(), tilesChanged.get(0).getWorkerSimplified().getPlayerName());
    }
}
