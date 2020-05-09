package it.polimi.ingsw.Model;

public interface MoveAction extends UserAction {
    void move(Worker worker, Tile tileWhereMove);
    boolean canMove(Worker worker, Tile tileWhereMove);
    void setCantMoveUp(boolean canMoveUp);
    boolean isCantMoveUp();
}
