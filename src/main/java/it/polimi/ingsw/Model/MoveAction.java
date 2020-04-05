package it.polimi.ingsw.Model;

public interface MoveAction extends Action {

    public void move(Worker worker, Tile tileWhereMove);
    public boolean canMove(Worker worker, Tile tileWhereMove);
    public void setCantMoveUp(boolean canMoveUp);
    public boolean isCantMoveUp();
}
