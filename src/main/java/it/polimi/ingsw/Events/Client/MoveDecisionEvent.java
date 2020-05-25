package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class MoveDecisionEvent extends ClientEvent implements Serializable {
    private final int x;
    private final int y;

    /**
     * Event that gives information about the coordinates of the move
     * @param x X axis of the tile
     * @param y Y axis of the tile
     */
    public MoveDecisionEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
