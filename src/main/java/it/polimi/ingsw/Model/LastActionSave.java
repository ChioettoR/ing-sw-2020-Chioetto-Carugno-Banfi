package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class LastActionSave {

    public enum ActionType {
        MOVE, BUILD
    }

    private ActionType lastActionType;
    private ArrayList<Worker> workers;
    private ArrayList<Tile> tiles;
    private Tile savedTile;

    public ActionType getLastActionType() {
        return lastActionType;
    }

    /**
     * Saves the tile in which the worker will build
     * @param savedTile The tile in which the worker will build
     */
    public void saveBeforeBuild(Tile savedTile) {
        lastActionType = ActionType.BUILD;
        this.savedTile = savedTile;
    }

    /**
     * Saves the tile in which the worker will move and the current worker
     * @param currentWorker The worker that is moving
     */
    public void saveBeforeMove(Worker currentWorker) {

        lastActionType = ActionType.MOVE;
        savedTile = currentWorker.getPosition();
        workers = new ArrayList<>();
        tiles = new ArrayList<>();
        workers.add(currentWorker);
        tiles.add(savedTile);
    }

    /**
     * Saves additional worker positions
     * @param worker The worker you want to save the position
     */
    public void saveAdditionalWorker(Worker worker) {
        workers.add(worker);
        tiles.add((worker.getPosition()));
    }

    /**
     * Returns the saved tile. The tile in which the worker built or the tile in which the worker moved depending on the last action did
     * @return The saved tile
     */
    public Tile getSavedTile() {
        return savedTile;
    }

    /**
     * Undoes the last action
     */
    public void undo() {
        if(lastActionType.equals(ActionType.MOVE))
            undoMovement();
        else if(lastActionType.equals(ActionType.BUILD))
            undoBuild();
        else
            System.out.println("Unknown action type");
    }

    /**
     * Resets the position of all the workers that need a reset
     */
    private void undoMovement() {
        for (int i = 0; i < workers.size(); i++) {
            undoPosition(workers.get(i), tiles.get(i));
        }
    }

    /**
     * Resets the position of the given worker
     * @param worker The worker you want to reset the position
     * @param position The previous position of the worker
     */
    private void undoPosition(Worker worker, Tile position) {
        worker.getPosition().setEmpty(true);
        worker.setPosition(position);
        position.setWorker(worker);
    }

    /**
     * Deletes the last level of the tile in which the worker built
     */
    private void undoBuild() {
        savedTile.removeLastLevel();
    }
}
