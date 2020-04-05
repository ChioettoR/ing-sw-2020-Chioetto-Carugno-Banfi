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

    @Override
    public void build(Worker worker, Tile tileWhereBuild) {
        build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
    }

    @Override
    public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
        return (!isActionLock() && correctTile(worker.getPosition(), tileWhereBuild) && newLevel<=4 && newLevel == tileWhereBuild.getLevel()+1) && tileWhereBuild.isEmpty();
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

    public LastActionSave getLastActionSave() {
        return lastActionSave;
    }

    @Override
    public void undo() {
        lastActionSave.undo();
    }

    public ArrayList<Tile> getAvailableTilesForAction(Worker worker, Action classWhereCheckBuild) {
        ArrayList<Tile> neighboursTiles = grid.getNeighbours(worker.getPosition());
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
