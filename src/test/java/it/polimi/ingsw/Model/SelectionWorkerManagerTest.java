package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.ServerEvent;
import it.polimi.ingsw.Observer.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SelectionWorkerManagerTest implements  ServerObserver {
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
    final SelectionWorkerManager selectionWorkerManager = new SelectionWorkerManager(stateManager, actionManager);


    @BeforeEach
    void setUp() throws IOException {
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
        stateManager.setGameState(GameState.SELECTING);
        selectionTest();
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }
    @Test
    public void selectionTest() throws IOException {
        selectionWorkerManager.selection(player.getID(),player.getWorkers().get(0).getLocalID(),player.getName());

    }


    @Override
    public void update(ServerEvent serverEvent) {
        if(serverEvent instanceof MessageEvent)
            return;
        assertSame(stateManager.getGameState(), GameState.ACTIONSELECTING);
        assertNotNull(stateManager.playersManager.getCurrentPlayer());
        assertEquals(playersManager.getCurrentPlayer().getID(), playersManager.getCurrentWorker().getPlayerID());
        assertNotNull(stateManager.playersManager.getCurrentWorker());
        assertSame(stateManager.playersManager.getCurrentWorker(), workerA1);
    }
}