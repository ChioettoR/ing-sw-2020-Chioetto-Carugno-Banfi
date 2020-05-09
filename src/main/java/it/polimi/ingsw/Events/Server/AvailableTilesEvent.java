package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.TileSimplified;

import java.io.Serializable;
import java.util.ArrayList;

public class AvailableTilesEvent extends ServerEvent implements Serializable {
    private final ArrayList<TileSimplified> tiles;

    public AvailableTilesEvent(ArrayList<TileSimplified> tiles, int playerID) {
        this.tiles = tiles;
        super.playerID = playerID;
    }

    public ArrayList<TileSimplified> getTiles() {
        return tiles;
    }
}
