package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class MoveActionStandard extends StandardActionBehaviour implements MoveAction{
    private boolean canMoveUp = true;
    private WinManager winManager = new WinManager();
    private Grid grid = Grid.getGrid();
    private LastActionSave lastActionSave = new LastActionSave();

    public void setCanMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    @Override
    public void move(Worker worker, Tile tileWhereMove) {
        lastActionSave.saveBeforeMove(worker, tileWhereMove);
        Tile currentTile = worker.getPosition();
        currentTile.setEmpty(true);
        tileWhereMove.setWorker(worker);
        worker.setPosition(tileWhereMove);
        if(currentTile.getLevel()==2 && worker.getPosition().getLevel() == 2) {
            winManager.winCurrentPlayer();
        }
    }

    @Override
    public boolean canMove(Worker worker, Tile tileWhereMove) {
        Tile currentTile = worker.getPosition();
        if (correctTile(currentTile, tileWhereMove)) {
            if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && !canMoveUp)
                return false;
            return (tileWhereMove.getLevel() - currentTile.getLevel() < 1);
        }
        return false;
    }

    @Override
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
        ArrayList<Tile> neighboursTiles = grid.getNeighbours(worker.getPosition());
        neighboursTiles.removeIf(tile -> (!canMove(worker,tile)));
        return neighboursTiles;
    }

    @Override
    public void undo() {
        lastActionSave.undo();
    }

    public ArrayList<Tile> getAvailableTilesForAction(Worker worker, Action action) {
        ArrayList<Tile> neighboursTiles = grid.getNeighbours(worker.getPosition());
        if(action instanceof MoveAction)
            neighboursTiles.removeIf(tile -> (!((MoveAction)action).canMove(worker,tile)));
        else {
            System.out.println("Wrong action passed to MoveActionStandard");
            return null;
        }
        return neighboursTiles;
    }
}