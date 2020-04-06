package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Card {
    private String name;
    private boolean alreadyPicked;
    private ArrayList<Action> actionOrder = new ArrayList<Action>();
    private boolean completeTowersObserver;

    public ArrayList<Action> getActionOrder() {
        return actionOrder;
    }

    public void setActionOrder(ArrayList<Action> actionOrder) {
        this.actionOrder = actionOrder;
    }

    public String getName() {
        return name;
    }

    /**
     * Creates a card
     * @param name The name of the card
     * @param completeTowersObserver If it is true and there are at least 5 complete towers, the player with this card wins
     */
    public Card(String name, boolean completeTowersObserver) {
        this.name = name;
        this.completeTowersObserver = completeTowersObserver;
    }

    /**
     * Creates a card
     * @param name The name of the card
     */
    public Card(String name) {
        this.name = name;
        this.completeTowersObserver = false;
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

    /**
     * Returns the move actions the player with this card does
     * @return Every move action
     */
    public ArrayList<MoveAction> getMoveActions() {
        ArrayList<MoveAction> moveActionArrayList = new ArrayList<MoveAction>();
        for(Action action : actionOrder) {
            if(action instanceof MoveAction)
                moveActionArrayList.add((MoveAction) action);
        }
        return moveActionArrayList;
    }

    /**
     * Returns the build actions the player with this card does
     * @return Every build action
     */
    public ArrayList<BuildAction> getBuildActions() {
        ArrayList<BuildAction> buildActionArrayList = new ArrayList<BuildAction>();
        for(Action action : actionOrder) {
            if(action instanceof BuildAction)
                buildActionArrayList.add((BuildAction) action);
        }
        return buildActionArrayList;
    }
}
