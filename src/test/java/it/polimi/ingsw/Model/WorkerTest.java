package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkerTest {

    Tile tile;
    Tile tile1;
    Tile tile2 = new Tile(5,8);
    Tile nullTile = null;

    @Test
    void setPositionTest() {
        setPosition();
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


    public void getTilesFromGrid() {
        System.out.println("TEST: I'm adding tiles to the grid");
        Grid grid = Grid.getGrid();
        grid.createGrid(5,5);
        tile = grid.getTiles().get(0);
        tile1 = grid.getTiles().get(1);
    }
}