package it.polimi.ingsw.Model;

public interface BuildAction extends UserAction {
    void build(Worker worker, Tile tileWhereBuild, int newLevel);
    void build(Worker worker, Tile tileWhereBuild);
    boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel);
    boolean canBuild(Worker worker, Tile tileWhereBuild);
}
