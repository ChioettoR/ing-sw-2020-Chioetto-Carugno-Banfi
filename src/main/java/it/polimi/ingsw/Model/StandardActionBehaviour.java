package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Grid;

public abstract class StandardActionBehaviour {
    private boolean isOptional;
    Grid grid = Grid.getGrid();

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public boolean correctTile(Tile currentTile, Tile tile){
        if (tile.getLevel() == 4 || !tile.isEmpty())
            return false;
        return grid.isNeighbour(currentTile, tile);
    }
}
