package it.polimi.ingsw.Model;

public abstract class StandardActionBehaviour {
    private boolean isOptional;
    private boolean actionLock;

    public boolean isActionLock() {
        return actionLock;
    }

    public void setActionLock(boolean actionLock) {
        this.actionLock = actionLock;
    }

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
