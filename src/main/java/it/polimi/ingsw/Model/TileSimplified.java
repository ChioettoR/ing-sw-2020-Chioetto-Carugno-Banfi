package it.polimi.ingsw.Model;

import java.io.Serializable;

public class TileSimplified implements Serializable {

    private final int x;
    private final int y;
    private final int buildLevel;
    private final WorkerSimplified workerSimplified;

    /**
     * This is a simplified version of the Tile, used to have a faster and lighter communication with client
     * @param x X axis of the grid
     * @param y Y axis of the grid
     * @param buildLevel Level of the buildings on this tile
     * @param workerSimplified worker's id on this tile
     */
    public TileSimplified(int x, int y, int buildLevel, WorkerSimplified workerSimplified) {
        this.x = x;
        this.y = y;
        this.buildLevel = buildLevel;
        this.workerSimplified = workerSimplified;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBuildLevel() {
        return buildLevel;
    }

    public WorkerSimplified getWorkerSimplified() {
        return workerSimplified;
    }
}
