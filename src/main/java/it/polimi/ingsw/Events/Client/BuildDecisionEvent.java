package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class BuildDecisionEvent extends ClientEvent implements Serializable {
    private final int x;
    private final int y;
    private final int buildLevel;
    /**
     * Event that gives information about the coordinates of the build
     * @param x X axis of the tile
     * @param y Y axis of the tile
     */
    public BuildDecisionEvent(int x, int y) {
        this.x = x;
        this.y = y;
        this.buildLevel = -1;
    }
    /**
     * Event that gives information about the coordinates of the build with also the level of the new building
     * @param x X axis of the tile
     * @param y Y axis of the tile
     * @param buildLevel level of the new building
     */
    public BuildDecisionEvent(int x, int y, int buildLevel) {
        this.x = x;
        this.y = y;
        this.buildLevel = buildLevel;
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
}
