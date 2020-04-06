package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class BuildActionStandard extends StandardActionBehaviour implements BuildAction {

    LastActionSave lastActionSave = new LastActionSave();

    /**
     * Builds in the given tile the given level block if all the standard build conditions are satisfied
     * @param worker The worker that is building
     * @param tileWhereBuild The tile in which the worker will build
     * @param newLevel The block level you want to build
     */
    @Override
    public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
        if(canBuild(worker, tileWhereBuild, newLevel))
            standardBuild(worker, tileWhereBuild, newLevel);
    }

    /**
     * The standard build. It doesn't check any condition before building
     * @param worker The worker that is building
     * @param tileWhereBuild The tile in which the worker will build
     * @param newLevel The block level you want to build
     */
    public void standardBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
        lastActionSave.saveBeforeBuild(tileWhereBuild);
        tileWhereBuild.setLevel(newLevel);

        //If I'm building a dome, it increases the counter of complete towers
        if(tileWhereBuild.getLevelsSize()==5) {
            Grid.getGrid().increaseCompleteTowersCount();
        }
    }

    /**
     * Builds in the given tile if all the standard build conditions are satisfied. The level of the new block is the current level of the tile plus one
     * @param worker The worker that is building
     * @param tileWhereBuild The tile in which the worker will build
     */
    @Override
    public void build(Worker worker, Tile tileWhereBuild) {
        build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
    }

    /**
     * Standard build conditions
     * @param worker The worker that is building
     * @param tileWhereBuild The tile in which the worker will build
     * @param newLevel The block level you want to build
     * @return True if the conditions are satisfied
     */
    @Override
    public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
        return (!isActionLock() && correctTile(worker.getPosition(), tileWhereBuild) && newLevel<=4 && newLevel == tileWhereBuild.getLevel()+1) && tileWhereBuild.isEmpty();
    }

    /**
     * Returns all the possible tiles in which the worker can build. The conditions checked are the standard ones
     * @param worker The worker that is building
     * @return All the tiles in which the worker can build
     */
    @Override
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
        ArrayList<Tile> neighboursTiles = Grid.getGrid().getNeighbours( worker.getPosition());
        ArrayList<Tile> newNeighboursTiles = new ArrayList<Tile>(neighboursTiles);
        boolean buildable = false;

        for(Tile tile : neighboursTiles) {
            buildable = false;
            for(int i=0; i<=4; i++) {
                if (canBuild(worker, tile, i) )
                    buildable = true;
            }
            if(!buildable)
                newNeighboursTiles.remove(tile);
        }
        return newNeighboursTiles;
    }

    public LastActionSave getLastActionSave() {
        return lastActionSave;
    }

    @Override
    public void undo() {
        lastActionSave.undo();
    }

    /**
     * Returns all the possible tiles in which the worker can build. The conditions checked are the ones in the given action class
     * @param worker The worker that is building
     * @return All the tiles in which the worker can build
     */
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker, Action classWhereCheckBuild) {
        ArrayList<Tile> neighboursTiles = Grid.getGrid().getNeighbours(worker.getPosition());
        ArrayList<Tile> newNeighboursTiles = new ArrayList<Tile>(neighboursTiles);
        boolean buildable = false;

        for(Tile tile : neighboursTiles) {
            buildable = false;
            for(int i=0; i<=4; i++) {
                if(classWhereCheckBuild instanceof BuildAction) {
                    if(((BuildAction) classWhereCheckBuild).canBuild(worker, tile, i))
                        buildable = true;
                }
                else {
                    System.out.println("Wrong action passed to BuildActionStandard");
                    return null;
                }
            }
            if(!buildable)
                newNeighboursTiles.remove(tile);
        }
        return newNeighboursTiles;
    }
}
