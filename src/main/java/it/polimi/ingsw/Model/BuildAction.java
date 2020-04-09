package it.polimi.ingsw.Model;

import java.util.ArrayList;

public interface BuildAction extends Action {
    public boolean isOptional();
    public void setOptional(boolean isOptional);
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker);
    public void build(Worker worker, Tile tileWhereBuild, int newLevel);
    public void build(Worker worker, Tile tileWhereBuild);
    public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel);
}
