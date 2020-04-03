package it.polimi.ingsw.Model;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class PlayersManagerTest {

    Grid grid = Grid.getGrid();

    Player player = new Player("Marcello");
    Card card = new Card("Carlo");
    Worker worker = new Worker();
    Worker worker4 = new Worker();

    Player player1 = new Player("Alberto");
    Card card1 = new Card("Marco");
    Worker worker1 = new Worker();
    Worker worker5 = new Worker();

    Player player2 = new Player("Federico");
    Card card2 = new Card("Giordano");
    Worker worker2 = new Worker();
    Worker worker6 = new Worker();

    Card card3 = new Card("Silvio");
    Worker worker3 = new Worker();
    PlayersManager playersManager = PlayersManager.getPlayersManager();

    @Test
    @Order(4)
    void addAndDeletePlayersTest() {
        addAndDeletePlayers();
    }

    void addAndDeletePlayers() {
        grid.createGrid(5,5);
        addPlayers();
        nextRound();
        checkCurrentPlayer();
        getPlayerWithCard();
        deleteWorker();
        deleteCurrentWorker();
        deletePlayers();
    }

    public void addPlayers() {
        System.out.println("TEST: I'm adding players to the game");
        player.setCard(card);
        playersManager.addPlayer(player);
        worker.setPosition(grid.getTiles().get(0));
        worker4.setPosition(grid.getTiles().get(4));
        player.setWorker(worker);
        player.setWorker(worker4);
        assert(playersManager.getPlayers().contains(player));
        assertEquals(1, playersManager.getPlayersNumber());

        playersManager.addPlayer(player1);
        player1.setCard(card1);
        worker1.setPosition(grid.getTiles().get(1));
        worker5.setPosition(grid.getTiles().get(5));
        player1.setWorker(worker1);
        player1.setWorker(worker5);
        assert(playersManager.getPlayers().contains(player));
        assertEquals(2, playersManager.getPlayersNumber());

        player2.setCard(card3);
        worker2.setPosition(grid.getTiles().get(2));
        worker6.setPosition(grid.getTiles().get(6));
        player2.setWorker(worker2);

        worker3.setPosition(grid.getTiles().get(3));

        //I'm trying to add a player with an existing name
        playersManager.addPlayer(player);
        assertEquals(2, playersManager.getPlayersNumber());

        //Verifies all the indexes are different
        assert(player.getID()!=player1.getID());
    }

    public void deletePlayers() {
        System.out.println("TEST: I'm deleting players from the game");
        playersManager.deletePlayer(player);
        assert(!playersManager.getPlayers().contains(player));
        assertEquals(null, playersManager.getCurrentPlayer());
        assertEquals("Alberto", playersManager.getNextPlayerAndStartRound().getName());

        //I'm deleting the last worker of a player and check if the player is deleted from the game
        playersManager.deleteWorker(worker4);
        assertTrue(!playersManager.getPlayers().contains(player));

        //I'm trying to delete a player not in the game
        playersManager.deletePlayer(player2);
        assertEquals(1, playersManager.getPlayersNumber());
        assert (playersManager.getPlayers().contains(player1));
    }

    public void nextRound() {
        playersManager.getNextPlayerAndStartRound();
    }

    public void checkCurrentPlayer() {
        assertEquals("Alberto", playersManager.getCurrentPlayer().getName());
    }

    public void getPlayerWithCard() {
        System.out.println("TEST: I'm getting players with card");
        assertEquals(player, playersManager.getPlayerWithCard(card));

        assertEquals(player1, playersManager.getPlayerWithCard(card1));

        //I'm trying to get a card not assigned to any player
        assertEquals(null, playersManager.getPlayerWithCard(card2));

        //I'm trying to get a card not assigned to any player in the game
        assertEquals(null, playersManager.getPlayerWithCard(card3));
    }

    @Test
    void deleteWorker() {
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

    @Test
    void deleteCurrentWorker() {
        System.out.println("TEST: I'm deleting current workers");
        playersManager.setCurrentWorker(worker);
        assert(playersManager.getCurrentWorker()==worker);

        playersManager.deleteCurrentWorker();
        assert(!player.getWorkers().contains(worker));
    }
}