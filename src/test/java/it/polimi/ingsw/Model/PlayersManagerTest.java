package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayersManagerTest {

    Grid grid = Grid.getGrid();
    PlayersManager playersManager = PlayersManager.getPlayersManager();

    Player player = new Player("Marcello");
    Card card = new Card("Carlo", null);
    Worker worker = new Worker();
    Worker worker4 = new Worker();

    Player player1 = new Player("Alberto");
    Card card1 = new Card("Marco", null);
    Worker worker1 = new Worker();
    Worker worker5 = new Worker();

    Player player2 = new Player("Federico");
    Card card2 = new Card("Giordano", null);
    Worker worker2 = new Worker();
    Worker worker6 = new Worker();

    Card card3 = new Card("Silvio", null);
    Worker worker3 = new Worker();

    @AfterEach
    void tearDown() {
        playersManager.reset();
        grid.reset();
    }

    @Test
    void addAndDeletePlayersTest() throws IOException {
        addAndDeletePlayers();
    }

    void addAndDeletePlayers() throws IOException {
        playersManager = PlayersManager.getPlayersManager();
        grid = Grid.getGrid();
        grid.createGrid(5,5);
        addPlayers();
        nextRound();
        checkCurrentPlayer();
        getPlayerWithCard();
        deleteWorker();
        deleteCurrentWorker();
        deletePlayers();
    }

    /**
     * Testing addPlayers
     */
    public void addPlayers() {
        System.out.println("TEST: I'm adding players to the game");
        player.setCard(card);
        playersManager.addPlayer(player);
        worker.setPosition(grid.getTiles().get(0));
        worker4.setPosition(grid.getTiles().get(4));
        player.setWorker(worker);
        player.setWorker(worker4);
        assert(playersManager.getPlayers().contains(player));
        //I'm trying to add a player
        assertEquals(1, playersManager.getPlayersNumber());

        playersManager.addPlayer(player1);
        player1.setCard(card1);
        worker1.setPosition(grid.getTiles().get(1));
        worker5.setPosition(grid.getTiles().get(5));
        player1.setWorker(worker1);
        player1.setWorker(worker5);
        assert(playersManager.getPlayers().contains(player));
        //I'm trying to add a 2nd player
        assertEquals(2, playersManager.getPlayersNumber());

        player2.setCard(card3);
        worker2.setPosition(grid.getTiles().get(2));
        worker6.setPosition(grid.getTiles().get(6));
        player2.setWorker(worker2);

        worker3.setPosition(grid.getTiles().get(3));

        //Verifies all the indexes are different
        assert(player.getID()!=player1.getID());
    }

    /**
     * Testing deletePlayers
     */
    public void deletePlayers() throws IOException {
        System.out.println("TEST: I'm deleting players from the game");
        playersManager.deletePlayer(player);
        //Checking if the Player "player" is no longer in the Player's list after deletePlayer
        assert(!playersManager.getPlayers().contains(player));
        assertNull(playersManager.getCurrentPlayer());
        assertEquals("Alberto", playersManager.nextPlayerAndStartRound().getName());

        //I'm deleting the last worker of a player and check if the player is deleted from the game
        playersManager.deleteWorker(worker4);
        assertFalse(playersManager.getPlayers().contains(player));

        //I'm trying to delete a player not in the game
        playersManager.deletePlayer(player2);
        assertEquals(1, playersManager.getPlayersNumber());
        assert (playersManager.getPlayers().contains(player1));
    }

    public void nextRound() {
        playersManager.nextPlayerAndStartRound();
    }

    /**
     * Testing if checkCurrentPlayer works in every round
     */
    public void checkCurrentPlayer() {
        assertEquals("Marcello", playersManager.getCurrentPlayer().getName());
        nextRound();
        assertEquals("Alberto", playersManager.getCurrentPlayer().getName());
        nextRound();
        assertEquals("Marcello", playersManager.getCurrentPlayer().getName());
        nextRound();
        assertEquals("Alberto", playersManager.getCurrentPlayer().getName());
    }

    /**
     * Testing getPlayerWithCard trying to get cards with no player assigned
     */
    public void getPlayerWithCard() {
        System.out.println("TEST: I'm getting players with card");
        assertEquals(player, playersManager.getPlayerWithCard(card));

        assertEquals(player1, playersManager.getPlayerWithCard(card1));

        //I'm trying to get a card not assigned to any player
        assertNull(playersManager.getPlayerWithCard(card2));

        //I'm trying to get a card not assigned to any player in the game
        assertNull(playersManager.getPlayerWithCard(card3));
    }

    /**
     * Testing deleteWorker trying to delete workers with no player assigned
     */
    void deleteWorker() throws IOException {
        System.out.println("TEST: I'm deleting workers");
        playersManager.deleteWorker(worker);
        assert(!player.getWorkers().contains(worker));

        playersManager.deleteWorker(worker1);
        assert(!player1.getWorkers().contains(worker1));

        //I'm trying to delete a worker not assigned to any player in the game
        playersManager.deleteWorker(worker2);

        //I'm trying to delete a worker not assigned to any player
        playersManager.deleteWorker(worker3);
    }

    /**
     * Testing deleteCurrentWorker and setCurrentWorker
     */
    void deleteCurrentWorker() throws IOException {
        System.out.println("TEST: I'm deleting current workers");
        playersManager.setCurrentWorker(worker);
        assert(playersManager.getCurrentWorker()==worker);

        playersManager.deleteCurrentWorker();
        assert(!player.getWorkers().contains(worker));
    }
}