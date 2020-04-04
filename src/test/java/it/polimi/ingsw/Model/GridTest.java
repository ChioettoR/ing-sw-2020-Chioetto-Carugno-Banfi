package it.polimi.ingsw.Model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    Grid grid;
    int length;
    int width;

    @BeforeEach
    void setUp() {
        grid = Grid.getGrid();
        length = 5;
        width = 5;
        grid.createGrid(length, width);
    }

    @AfterEach
    void tearDown() {
        grid.destroyGrid();
    }

    @Test
    void createGridAndCheckNeighboursTest() {
        createGridAndCheckNeighbours();
    }

    void createGridAndCheckNeighbours() {
        createGrid();
        checkNeighbour();
    }

    public void createGrid() {
        System.out.println("TEST: I'm creating the grid");
        assertEquals(width * length, grid.getTiles().size());

        int k=0;
        for(int i=0; i<length; i++)
            for(int j=0; j<width; j++, k++) {
                assertEquals(i,grid.getTiles().get(k).getX());
                assertEquals(j, grid.getTiles().get(k).getY());
            }
    }

    @Test
    void checkNeighbour() {
        assertTrue(grid.isNeighbour(grid.getTiles().get(6), grid.getTiles().get(7)));
        assertTrue(grid.isNeighbour(grid.getTiles().get(1), grid.getTiles().get(6)));
        assertTrue(grid.isNeighbour(grid.getTiles().get(2), grid.getTiles().get(8)));
        assertTrue(grid.isNeighbour(grid.getTiles().get(1), grid.getTiles().get(5)));
        assertFalse(grid.isNeighbour(grid.getTiles().get(5), grid.getTiles().get(8)));
    }
}