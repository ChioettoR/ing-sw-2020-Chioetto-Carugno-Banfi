package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class BuildActionStandard extends StandardActionBehaviour implements BuildAction {

    Grid grid = Grid.getGrid();
    LastActionSave lastActionSave = new LastActionSave();

    @Override
    public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
        if(canBuild(worker, tileWhereBuild, newLevel))
            standardBuild(worker, tileWhereBuild, newLevel);
    }

    public void standardBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
        lastActionSave.saveBeforeBuild(tileWhereBuild);
        tileWhereBuild.setLevel(newLevel);

        //If I'm building a dome, it increases the counter of complete towers (For Chronus)
        if(tileWhereBuild.getLevelsSize()==5) {
            grid.increaseCompleteTowersCount();
        }
    }

    public void build(Worker worker, Tile tileWhereBuild, int newLevel, Action action) {
        if(action instanceof BuildAction) {
            if(((BuildAction) action).canBuild(worker, tileWhereBuild, newLevel))
                standardBuild(worker, tileWhereBuild, newLevel);
        }
        else
            System.out.println("Wrong action passed to BuildActionStandard");
    }

    @Override
    public void build(Worker worker, Tile tileWhereBuild) {
        build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
    }

    public void build(Worker worker, Tile tileWhereBuild, Action action) {
        build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1, action);
    }

    @Override
    public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
        return (correctTile(worker.getPosition(), tileWhereBuild) && newLevel == tileWhereBuild.getLevel()+1);
    }

    @Override
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
        ArrayList<Tile> neighboursTiles = grid.getNeighbours( worker.getPosition());
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

    @Override
    public void undo() {
        lastActionSave.undo();
    }

    public ArrayList<Tile> getAvailableTilesForAction(Worker worker, Action action) {
        ArrayList<Tile> neighboursTiles = grid.getNeighbours(worker.getPosition());
        ArrayList<Tile> newNeighboursTiles = new ArrayList<Tile>(neighboursTiles);
        boolean buildable = false;

        for(Tile tile : neighboursTiles) {
            buildable = false;
            for(int i=0; i<=4; i++) {
                if(action instanceof BuildAction) {
                    if(((BuildAction) action).canBuild(worker, tile, i))
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
