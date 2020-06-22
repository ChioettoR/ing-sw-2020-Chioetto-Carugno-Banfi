package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class GridSimplified implements Serializable {
    private final ArrayList<TileSimplified> tiles ;

    /**
     * This is a simplified version of the Grid, used to have a faster and lighter communication with client
     * @param tilesList list of the tiles in the grid
     */
    public GridSimplified(ArrayList<Tile> tilesList) {
        tiles = new ArrayList<>();
        for(Tile t : tilesList) {
            tiles.add(t.simplify());
        }
    }

    public ArrayList<TileSimplified> getTiles() {
        return tiles;
    }
}
