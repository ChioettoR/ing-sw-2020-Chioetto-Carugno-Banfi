package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class MoveActionStandard extends StandardActionBehaviour implements MoveAction{
    private boolean canMoveUp = true;
    private WinManager winManager = new WinManager();
    private LastActionSave lastActionSave = new LastActionSave();

    public void setCanMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    public boolean isCanMoveUp() {
        return canMoveUp;
    }

    @Override
    public void move(Worker worker, Tile tileWhereMove) {
        if(canMove(worker, tileWhereMove)) {
            standardMove(worker, tileWhereMove);
        }
    }

    public void move(Worker worker, Tile tileWhereMove, Action action) {
        if(action instanceof MoveAction) {
            if(((MoveAction) action).canMove(worker, tileWhereMove))
                standardMove(worker, tileWhereMove);
        }
        else
            System.out.println("Wrong action passed to MoveActionStandard");
    }

    public void standardMove(Worker worker, Tile tileWhereMove) {
        lastActionSave.saveBeforeMove(worker, tileWhereMove);
        Tile currentTile = worker.getPosition();
        currentTile.setEmpty(true);
        tileWhereMove.setWorker(worker);
        worker.setPosition(tileWhereMove);
        if (currentTile.getLevel() == 2 && tileWhereMove.getLevel() == 3) {
            winManager.winCurrentPlayer();
        }
    }

    public LastActionSave getLastActionSave() {
        return lastActionSave;
    }

    @Override
    public boolean canMove(Worker worker, Tile tileWhereMove) {
        Tile currentTile = worker.getPosition();
        if (correctTile(currentTile, tileWhereMove) && tileWhereMove.isEmpty()) {
            if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && !canMoveUp)
                return false;
            else if (tileWhereMove.getLevel() - currentTile.getLevel() <= 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
        ArrayList<Tile> neighboursTiles = Grid.getGrid().getNeighbours(worker.getPosition());
        neighboursTiles.removeIf(tile -> (!canMove(worker,tile)));
        return neighboursTiles;
    }

    @Override
    public void undo() {
        lastActionSave.undo();
    }

    public ArrayList<Tile> getAvailableTilesForAction(Worker worker, Action action) {
        ArrayList<Tile> neighboursTiles = Grid.getGrid().getNeighbours(worker.getPosition());
        if(action instanceof MoveAction)
            neighboursTiles.removeIf(tile -> (!((MoveAction)action).canMove(worker,tile)));
        else {
            System.out.println("Wrong action passed to MoveActionStandard");
            return null;
        }
        return neighboursTiles;
    }
}
