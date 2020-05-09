package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.TileSimplified;

import java.io.Serializable;
import java.util.ArrayList;

public class ChangeEvent extends ServerEvent implements Serializable {
    private final ArrayList<TileSimplified> tiles;

    public ArrayList<TileSimplified> getTiles() {
        return tiles;
    }

    public ChangeEvent(ArrayList<TileSimplified> tiles) {
        this.tiles = tiles;
        super.playerID = -1;
    }
}
