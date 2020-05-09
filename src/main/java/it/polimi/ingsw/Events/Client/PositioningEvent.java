package it.polimi.ingsw.Events.Client;

import java.io.Serializable;

public class PositioningEvent extends ClientEvent implements Serializable {
    private final int x;
    private final int y;

    public PositioningEvent(int x, int y) {
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
