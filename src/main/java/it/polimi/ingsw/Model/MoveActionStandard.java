package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class MoveActionStandard extends StandardActionBehaviour implements MoveAction{
    private boolean cantMoveUp;
    private WinManager winManager = new WinManager();
    private LastActionSave lastActionSave = new LastActionSave();
    private boolean actionLock = false;

    public void setCantMoveUp(boolean cantMoveUp) {
        this.cantMoveUp = cantMoveUp;
    }

    public boolean isCantMoveUp() {
        return cantMoveUp;
    }

    @Override
    public void move(Worker worker, Tile tileWhereMove) {
        if(canMove(worker, tileWhereMove)) {
            standardMove(worker, tileWhereMove);
        }
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
        if (correctTile(currentTile, tileWhereMove) && tileWhereMove.isEmpty() && !isActionLock()) {
            if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && cantMoveUp)
                return false;
            else return tileWhereMove.getLevel() - currentTile.getLevel() <= 1;
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
