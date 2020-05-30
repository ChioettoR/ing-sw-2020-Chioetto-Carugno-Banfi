package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.FirstPlayerEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.ServerEvent;
import it.polimi.ingsw.Observer.Server.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FirstPlayerManagerTest implements ServerObserver {

    Grid grid = Grid.getGrid();
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    Deck deck = Deck.getDeck();
    Player player = new Player("Alberto");
    Player player1 = new Player("Marcello");
    Player player2 = new Player("Franco");
    StateManager stateManager = new StateManager();
    ColorPoolManager colorPoolManager = new ColorPoolManager(stateManager);
    FirstPlayerManager firstPlayerManager = new FirstPlayerManager(stateManager, colorPoolManager);
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
    public void update(ServerEvent serverEvent) throws IOException {
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
