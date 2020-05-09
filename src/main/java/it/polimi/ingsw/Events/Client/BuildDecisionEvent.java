package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class BuildDecisionEvent extends ClientEvent implements Serializable {
    private final int x;
    private final int y;
    private final int buildLevel;

    public BuildDecisionEvent(int x, int y) {
        this.x = x;
        this.y = y;
        this.buildLevel = -1;
    }

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
