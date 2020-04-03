package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Card {
    private String name;
    private boolean alreadyPicked;
    private ActionOrder actionOrder;
    private boolean saveEverythingBeforeMove;
    private boolean completeTowersObserver;

    public boolean isSaveEverythingBeforeMove() {
        return saveEverythingBeforeMove;
    }

    public ActionOrder getActionOrder() {
        return actionOrder;
    }

    public void setActionOrder(ActionOrder actionOrder) {
        this.actionOrder = actionOrder;
    }

    public String getName() {
        return name;
    }

    public Card(String name, boolean saveEverythingBeforeMove, boolean completeTowersObserver) {
        this.name = name;
        this.saveEverythingBeforeMove = saveEverythingBeforeMove;
        this.completeTowersObserver = completeTowersObserver;
    }

    public Card(String name) {
        this.name = name;
        this.saveEverythingBeforeMove = false;
    }

    public boolean isCompleteTowersObserver() {
        return completeTowersObserver;
    }

    public boolean getAlreadyPicked() {
        return alreadyPicked;
    }

    public void setAlreadyPicked(boolean alreadyPicked) {
        this.alreadyPicked = alreadyPicked;
    }

    public ArrayList<MoveAction> getMoveActions() {
        ArrayList<MoveAction> moveActionArrayList = new ArrayList<MoveAction>();
        for(Action action : actionOrder.getActions()) {
            if(action instanceof MoveAction)
                moveActionArrayList.add((MoveAction) action);
        }
        return null;
    }
}
