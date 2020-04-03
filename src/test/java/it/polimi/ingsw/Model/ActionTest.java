package it.polimi.ingsw.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
    Grid grid = Grid.getGrid();
    Worker worker = new Worker();
    Worker worker1 = new Worker();
    Deck deck = Deck.getDeck();
    MoveActionDecorator moveActionDecorator;
    BuildActionDecorator buildActionDecorator;
    MoveActionStandard moveActionStandard = new MoveActionStandard();
    BuildActionStandard buildActionStandard = new BuildActionStandard();
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    int completeTowersNumber;
    Player player = new Player("Marcello");
    Player player1 = new Player("Gianfilippo");
    Card apolloCard = new Card("Apollo");
    Card anonymousCard = new Card("Anonymous");
    Card minotaurCard = new Card("Minotaur", true, false);

    @AfterEach
    void tearDown() {
        PlayersManager.getPlayersManager().deletePlayer(player);
        PlayersManager.getPlayersManager().deletePlayer(player1);
        deck.deleteAllCards();
        grid.destroyGrid();
    }

    @BeforeEach
    public void setUp() {
        grid.createGrid(5,5);
        PlayersManager playersManager = PlayersManager.getPlayersManager();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        player1.setCard(anonymousCard);
        player.setWorker(worker);
        player1.setWorker(worker1);
        playersManager.getPlayerWithID(worker.getPlayerID()).setCard(minotaurCard);

        /**
         * Decorator with the same functionality of the standard move
         */
        moveActionDecorator = new MoveActionDecorator() {
            MoveActionStandard moveActionStandard = new MoveActionStandard();
            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                moveActionStandard.move(worker, tileWhereMove);
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return moveActionStandard.canMove(worker, tileWhereMove);
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return moveActionStandard.getAvailableTilesForAction(worker,this);
            }

            @Override
            public boolean isOptional() {
                return moveActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                moveActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                moveActionStandard.undo();
            }
        };

        /**
         * The player can build a dome also at ground level
         */
        buildActionDecorator = new BuildActionDecorator() {
            BuildActionStandard buildActionStandard = new BuildActionStandard();
            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                buildActionStandard.build(worker,tileWhereBuild,newLevel, this);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                buildActionStandard.build(worker, tileWhereBuild, this);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) || ((buildActionStandard.correctTile(worker.getPosition(), tileWhereBuild)) && newLevel==4 && tileWhereBuild.getLevel()==0));
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return buildActionStandard.getAvailableTilesForAction(worker, this);
            }

            @Override
            public boolean isOptional() {
                return buildActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                buildActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }
        };
    }

    /**
     * Tests standard move and standard build of a worker and the undo function. It also tests a simply decorator of the standard move
     * and build
     */
    @Test
    public void actionTestStarter() {
        buildAndMoveTest();
        buildCompleteTowers();
        moveAndBuildToWin();
    }

    public void buildAndMoveTest() {
        System.out.println("TEST: I'm testing building and movements");
        Tile currentPosition = grid.getTiles().get(0);
        Tile currentPosition1 = grid.getTiles().get(23);
        worker1.setPosition(currentPosition1);
        worker.setPosition(currentPosition);
        Tile nextPosition = grid.getTiles().get(5);
        Tile nextPosition1 = grid.getTiles().get(24);
        Tile buildPosition = grid.getTiles().get(1);

        // Verifies if the available tiles for the decorator worker movement are correct
        assertEquals(grid.getNeighbours(worker.getPosition()), moveActionDecorator.getAvailableTilesForAction(worker));

        // Verifies if the available tiles for the decorator worker build are correct
        assertEquals(grid.getNeighbours(worker.getPosition()), buildActionDecorator.getAvailableTilesForAction(worker));

        // Verifies if the available tiles for the standard worker movement are correct
        assertEquals(grid.getNeighbours(worker.getPosition()), moveActionStandard.getAvailableTilesForAction(worker));

        // Verifies if the available tiles for the standard worker build are correct
        assertEquals(grid.getNeighbours(worker.getPosition()), buildActionStandard.getAvailableTilesForAction(worker));

        /*
         Verifies if the standard worker movement has been successful : the previous tile is empty, it doesn't contain
         the reference to the worker anymore, the next tile is not empty and now contains the reference
         to the worker
         */
        moveActionStandard.move(worker, nextPosition);
        assertEquals(nextPosition, worker.getPosition());
        assertNull(currentPosition.getWorker());
        assertTrue(currentPosition.isEmpty());
        assertEquals(worker, nextPosition.getWorker());
        assertFalse(nextPosition.isEmpty());

        new MoveActionStandard().move(worker1, nextPosition1);


        // Verifies if the undo functionality for the movement has been successful
        moveActionStandard.undo();
        assertEquals(currentPosition, worker.getPosition());
        assertEquals(worker, currentPosition.getWorker());
        assertNull(nextPosition.getWorker());
        assertTrue(nextPosition.isEmpty());
        assertFalse(currentPosition.isEmpty());
        assertFalse(currentPosition.isEmpty());

        assertEquals(currentPosition1, worker1.getPosition());
        assertEquals(worker1, currentPosition1.getWorker());
        assertNull(nextPosition1.getWorker());
        assertTrue(nextPosition1.isEmpty());
        assertFalse(currentPosition1.isEmpty());

        player.setCard(apolloCard);
        moveActionStandard.move(worker, nextPosition);
        moveActionStandard.undo();

        // Verifies if the standard worker building has been successful : the new level of the tile is updated
        //I'm trying to build a wrong level
        buildActionStandard.build(worker, buildPosition, 2);
        assertEquals(0, buildPosition.getLevel());
        assertEquals(1, buildPosition.getLevelsSize());
        //I'm trying to build a correct level
        buildActionStandard.build(worker, buildPosition);
        assertEquals(1, buildPosition.getLevel());
        assertEquals(2, buildPosition.getLevelsSize());

        // Verifies if the worker can build a dome
        //I'm trying to build a dome on a base block
        assertFalse(buildActionDecorator.canBuild(worker, buildPosition, 4));
        buildActionDecorator.build(worker, buildPosition, 4 );
        assertEquals(1, buildPosition.getLevel());
        assertEquals(2, buildPosition.getLevelsSize());
        //I'm trying to build a dome on the ground level
        Tile newBuildPosition = grid.getTiles().get(5);
        assertTrue(buildActionDecorator.canBuild(worker, newBuildPosition, 4));
        buildActionDecorator.build(worker, newBuildPosition, 4);
        assertEquals(4, newBuildPosition.getLevel());
        assertEquals(2, buildPosition.getLevelsSize());

        // Verifies if the undo functionality for the building has been successful
        buildActionDecorator.undo();
        assertEquals(0, newBuildPosition.getLevel());
        assertEquals(1, newBuildPosition.getLevelsSize());

        buildActionDecorator.build(worker, newBuildPosition);
        assertEquals(1, buildPosition.getLevel());
        assertEquals(2, buildPosition.getLevelsSize());
        buildActionDecorator.undo();
    }

    public void buildCompleteTowers() {
        Card card = new Card("Chronus", false, true);
        deck.addCard(card);
        player.setCard(card);
        Card card1 = new Card("FakeChronus", false, true);
        deck.addCard(card1);
        buildCompleteTower(worker, grid.getTiles().get(1));
        moveActionStandard.move(worker, grid.getTiles().get(6));
        buildCompleteTower(worker, grid.getTiles().get(10));
        buildCompleteTower(worker, grid.getTiles().get(11));
        buildCompleteTower(worker, grid.getTiles().get(12));
        buildCompleteTower(worker, grid.getTiles().get(7));
    }

    public void buildCompleteTower(Worker worker, Tile tileWhereBuildCompleteTower) {
        for(int i = 0; i<4; i++)
            buildActionStandard.build(worker, tileWhereBuildCompleteTower);
        completeTowersNumber++;
        assertEquals(4, tileWhereBuildCompleteTower.getLevel());
        assertEquals(5, tileWhereBuildCompleteTower.getLevelsSize());
        assertEquals(completeTowersNumber, grid.getCompleteTowersCount());
    }

    public void moveAndBuildToWin() {
        BuildActionStandard buildActionStandard = new BuildActionStandard();
        MoveActionStandard moveActionStandard = new MoveActionStandard();
        buildActionStandard.build(worker1, grid.getTiles().get(22));
        moveActionStandard.move(worker1, grid.getTiles().get(22));
        buildActionStandard.build(worker1, grid.getTiles().get(23));
        moveActionStandard.move(worker1, grid.getTiles().get(23));
        buildActionStandard.build(worker1, grid.getTiles().get(22));
        moveActionStandard.move(worker1, grid.getTiles().get(22));
        buildActionStandard.build(worker1, grid.getTiles().get(23));
        moveActionStandard.move(worker1, grid.getTiles().get(23));
        buildActionStandard.build(worker1, grid.getTiles().get(22));
        moveActionStandard.move(worker1, grid.getTiles().get(22));
    }
}