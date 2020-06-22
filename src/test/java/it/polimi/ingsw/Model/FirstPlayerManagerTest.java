package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.FirstPlayerEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.ServerEvent;
import it.polimi.ingsw.Observer.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FirstPlayerManagerTest implements ServerObserver {

    final Grid grid = Grid.getGrid();
    final PlayersManager playersManager = PlayersManager.getPlayersManager();
    final Deck deck = Deck.getDeck();
    final Player player = new Player("Alberto");
    final Player player1 = new Player("Marcello");
    final Player player2 = new Player("Franco");
    final StateManager stateManager = new StateManager();
    final ColorPoolManager colorPoolManager = new ColorPoolManager(stateManager);
    final FirstPlayerManager firstPlayerManager = new FirstPlayerManager(stateManager, colorPoolManager);
    int updateCounter;
    int playerID;

    @BeforeEach
    void setUp() throws IOException {
        new Builder().build();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        playersManager.addPlayer(player2);
        firstPlayerManager.addObserver(this);
        stateManager.addObserver(this);
        stateManager.setGameState(GameState.FIRSTPLAYERSELECTION);
        firstPlayerTest();
    }

    @Test
    void firstPlayerTest() throws IOException {
        firstPlayerManager.transition();
        firstPlayerManager.firstPlayerChosen(player.getID(), player.getName());
        playerID = player.getID();
        assertEquals(playersManager.getCurrentPlayer(), player);
        assertEquals(stateManager.getGameState(), GameState.COLORSELECTING);
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Override
    public void update(ServerEvent serverEvent) {
        if (serverEvent instanceof MessageEvent)
            return;

        if (updateCounter == 0) {
            assertTrue(serverEvent instanceof FirstPlayerEvent);
            assertEquals(stateManager.getGameState(), GameState.FIRSTPLAYERSELECTION);
            assertEquals(playerID, player.getID());
            updateCounter++;
        }
    }
}
