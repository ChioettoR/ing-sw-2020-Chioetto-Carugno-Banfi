package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkerTest {

    Grid grid = Grid.getGrid();
    Tile tile;
    Tile tile1;
    Tile tile2 = new Tile(5,8);
    Tile nullTile = null;

    @BeforeEach
    void setUp() {
        grid.createGrid(5,5);
    }

    @Test
    void setPositionTest() {
        setPosition();
    }

    @AfterEach
    void tearDown() {
        grid.reset();
    }

    /**
     * Sets the position of the worker
     */
    void setPosition() {

        getTilesFromGrid();
        System.out.println("TEST: I'm setting workers position");
        Worker worker = new Worker();
        worker.setPosition(nullTile);

        worker.setPosition(tile);
        assertEquals(tile, worker.getPosition());

        worker.setPosition(tile1);
        assertEquals(tile1, worker.getPosition());

        //I'm trying to add a tile not in the grid
        worker.setPosition(tile2);
        assertEquals(tile1, worker.getPosition());
    }

    /**
     * Gets the tiles from grid
     */
    public void getTilesFromGrid() {
        System.out.println("TEST: I'm getting tiles to the grid");
        tile = grid.getTiles().get(0);
        tile1 = grid.getTiles().get(1);
    }
}