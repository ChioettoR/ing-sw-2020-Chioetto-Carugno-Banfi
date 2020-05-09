package it.polimi.ingsw.Model;

import java.util.ArrayList;

public interface UserAction extends Action{
    void undo();
    boolean isOptional();
    void setOptional(boolean isOptional);
    ArrayList<Tile> getAvailableTilesForAction(Worker worker);
}
