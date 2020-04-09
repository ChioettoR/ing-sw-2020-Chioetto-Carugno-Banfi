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

    /**
     * Testing the creation of the grid (gaming board)
     */
    public void createGrid() {
        System.out.println("TEST: I'm creating the grid");
        //Checking if the size of the grid is correct
        assertEquals(width * length, grid.getTiles().size());

        int k=0;
        for(int i=1; i<=length; i++)
            for(int j=1; j<=width; j++, k++) {
                //Checking if every tile from the grid gets the corrects [x,y]
                assertEquals(j,grid.getTiles().get(k).getX());
                assertEquals(i, grid.getTiles().get(k).getY());
            }
    }

    /**
     * Testing if isNeighbour works correctly
     */
    @Test
    void checkNeighbour() {
        assertTrue(grid.isNeighbour(grid.getTiles().get(6), grid.getTiles().get(7)));
        assertTrue(grid.isNeighbour(grid.getTiles().get(1), grid.getTiles().get(6)));
        assertTrue(grid.isNeighbour(grid.getTiles().get(2), grid.getTiles().get(8)));
        assertTrue(grid.isNeighbour(grid.getTiles().get(1), grid.getTiles().get(5)));
        assertFalse(grid.isNeighbour(grid.getTiles().get(5), grid.getTiles().get(8)));
    }
}