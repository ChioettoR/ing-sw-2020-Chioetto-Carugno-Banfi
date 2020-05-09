package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class MoveDecisionEvent extends ClientEvent implements Serializable {
    private final int x;
    private final int y;

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
