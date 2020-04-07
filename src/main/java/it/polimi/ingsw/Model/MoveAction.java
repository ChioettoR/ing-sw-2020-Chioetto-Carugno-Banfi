package it.polimi.ingsw.Model;

import java.util.ArrayList;

public interface MoveAction extends Action {
    public ArrayList<Tile> getAvailableTilesForAction(Worker worker);
    public void move(Worker worker, Tile tileWhereMove);
    public boolean canMove(Worker worker, Tile tileWhereMove);
    public void setCantMoveUp(boolean canMoveUp);
    public boolean isCantMoveUp();
}
