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

    /**
     * Moves in the given tile the given worker if all the standard move conditions are satisfied
     * @param worker The worker that is moving
     * @param tileWhereMove The tile in which the worker will move
     */
    @Override
    public void move(Worker worker, Tile tileWhereMove) {
        if(canMove(worker, tileWhereMove)) {
            standardMove(worker, tileWhereMove);
        }
    }

    /**
     * The standard move. It doesn't check any condition before moving
     * @param worker The worker that is moving
     * @param tileWhereMove The tile in which the worker will move
     */
    public void standardMove(Worker worker, Tile tileWhereMove) {
        lastActionSave.saveBeforeMove(worker);
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

    /**
     * Standard move conditions
     * @param worker The worker that is moving
     * @param tileWhereMove The tile in which the worker will move
     * @return True if the conditions are satisfied
     */
    @Override
    public boolean canMove(Worker worker, Tile tileWhereMove) {
        if(actionLock)
            return false;
        Tile currentTile = worker.getPosition();
        if (correctTile(currentTile, tileWhereMove) && tileWhereMove.isEmpty() && !isActionLock()) {
            if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && cantMoveUp)
                return false;
            else return tileWhereMove.getLevel() - currentTile.getLevel() <= 1;
        }
        return false;
    }

    /**
     * Returns all the possible tiles in which the worker can move. The conditions checked are the standard ones
     * @param worker The worker that is moving
     * @return All the tiles in which the worker can move
     */
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

    /**
     * Returns all the possible tiles in which the worker can move. The conditions checked are the ones in the given action class
     * @param worker The worker that is moving
     * @return All the tiles in which the worker can move
     */
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker, Action classWhereCheckMove) {
        ArrayList<Tile> neighboursTiles = Grid.getGrid().getNeighbours(worker.getPosition());
        if(classWhereCheckMove instanceof MoveAction)
            neighboursTiles.removeIf(tile -> (!((MoveAction)classWhereCheckMove).canMove(worker,tile)));
        else {
            System.out.println("Wrong action passed to MoveActionStandard");
            return null;
        }
        return neighboursTiles;
    }
}
