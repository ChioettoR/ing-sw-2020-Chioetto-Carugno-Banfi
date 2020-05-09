package it.polimi.ingsw.Model;

import java.io.Serializable;

public class TileSimplified implements Serializable {

    private final int x;
    private final int y;
    private int buildLevel;
    private WorkerSimplified workerSimplified;

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

    public void setBuildLevel(int buildLevel) {
        this.buildLevel = buildLevel;
    }

    public WorkerSimplified getWorkerSimplified() {
        return workerSimplified;
    }

    public void setWorkerSimplified(WorkerSimplified workerSimplified) {
        this.workerSimplified = workerSimplified;
    }
}
