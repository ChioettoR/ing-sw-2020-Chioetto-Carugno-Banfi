package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class GridSimplified implements Serializable {
    private final ArrayList<TileSimplified> tiles ;

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
