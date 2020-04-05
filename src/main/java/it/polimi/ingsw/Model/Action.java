package it.polimi.ingsw.Model;

import java.util.ArrayList;

public interface Action {
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker);
    public boolean isOptional();
    public void setOptional(boolean isOptional);
    public void undo();
    public void setActionLock(boolean actionLock);
    public boolean isActionLock();
}
