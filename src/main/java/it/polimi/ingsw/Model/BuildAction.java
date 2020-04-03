package it.polimi.ingsw.Model;

public interface BuildAction extends Action {

    public void build(Worker worker, Tile tileWhereBuild, int newLevel);
    public void build(Worker worker, Tile tileWhereBuild);
    public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel);
}
