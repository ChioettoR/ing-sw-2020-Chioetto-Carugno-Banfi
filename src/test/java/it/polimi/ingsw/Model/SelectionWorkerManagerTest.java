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
    Grid grid = Grid.getGrid();
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    Deck deck = Deck.getDeck();
    Worker workerA1 = new Worker();
    Worker workerA2 = new Worker();
    Worker workerM1 = new Worker();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Card card;
    Card card1;
    StateManager stateManager = new StateManager();
    ActionManager actionManager = new ActionManager(stateManager);
    SelectionWorkerManager selectionWorkerManager = new SelectionWorkerManager(stateManager, actionManager);


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
        actionManager.addObserver((ServerObserver) this);
        stateManager.addObserver((ServerObserver) this);
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
    public void update(ServerEvent serverEvent) throws IOException {
        if(serverEvent instanceof MessageEvent)
            return;
        assertTrue(stateManager.getGameState() == GameState.ACTIONSELECTING);
        assertTrue(stateManager.playersManager.getCurrentPlayer()!= null);
        assertTrue(playersManager.getCurrentPlayer().getID() == playersManager.getCurrentWorker().getPlayerID());
        assertFalse(stateManager.playersManager.getCurrentWorker()== null);
        assertTrue(stateManager.playersManager.getCurrentWorker() == workerA1);
    }
}