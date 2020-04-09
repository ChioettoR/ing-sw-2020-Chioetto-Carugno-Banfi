package it.polimi.ingsw.Model;

import java.util.ArrayList;

public interface Action {
    public void undo();
    public void setActionLock(boolean actionLock);
    public boolean isActionLock();
}
