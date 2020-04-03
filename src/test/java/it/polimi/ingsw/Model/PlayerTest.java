package it.polimi.ingsw.Model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    Player player;
    String name;
    int ID;
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Worker worker2 = new Worker();

    @BeforeEach
    void setUp() {
        ID = 5;
        name = "Marcello";
        player = new Player(name);
        player.setID(ID);
    }

    @Test
    void setAndDeleteTest() {
        setAndDelete();
    }

    void setAndDelete() {
        setPlayers();
        deletePlayers();
    }

    /**
     * Adds workers to the player
     */
    public void setPlayers () {
        System.out.println("TEST: I'm adding workers");
        player.setWorker(worker);
        assertEquals(1, player.getWorkers().size());

        player.setWorker(worker1);
        assertEquals(2, player.getWorkers().size());

        player.setWorker(worker2);
        assertEquals(3, player.getWorkers().size());

        player.setWorker(worker);
        assertEquals(3, player.getWorkers().size());

        for (Worker w : player.getWorkers()) {
            assertEquals(ID, w.getPlayerID());
        }
    }

    /**
     * Deletes workers from the player
     */
    public void deletePlayers() {
        System.out.println("TEST: I'm deleting workers");
        player.deleteWorker(worker);
        assertEquals(2, player.getWorkers().size());

        player.deleteWorker(worker1);
        assertEquals(1, player.getWorkers().size());

        player.deleteWorker(new Worker());
        assertEquals(1, player.getWorkers().size());

        player.deleteWorker(worker);
        assertEquals(1, player.getWorkers().size());

        player.deleteWorker(worker2);
        assertEquals(0, player.getWorkers().size());
    }
}