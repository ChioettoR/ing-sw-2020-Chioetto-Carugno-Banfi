package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Grid;

public abstract class StandardActionBehaviour {
    private boolean isOptional;

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public boolean correctTile(Tile currentTile, Tile tile){
        if (tile.getLevel() == 4)
            return false;
        return Grid.getGrid().isNeighbour(currentTile, tile);
    }
}
