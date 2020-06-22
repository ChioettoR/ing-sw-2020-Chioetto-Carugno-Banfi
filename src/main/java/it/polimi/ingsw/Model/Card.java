package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Card {
    private final String name;
    private final CardsBuilder.GodPower godPower;
    private String effectName;
    private String description;
    private boolean alreadyPicked;
    private ArrayList<Action> actionOrder = new ArrayList<Action>();
    private final boolean completeTowersObserver;

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CardsBuilder.GodPower getGodPower() {
        return godPower;
    }

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
    public Card(String name, CardsBuilder.GodPower godPower, boolean completeTowersObserver) {
        this.name = name;
        this.godPower = godPower;
        this.completeTowersObserver = completeTowersObserver;
    }

    /**
     * Creates a card
     * @param name The name of the card
     */
    public Card(String name, CardsBuilder.GodPower godPower) {
        this.name = name;
        this.godPower = godPower;
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

    public CardSimplified simplify() {
        return new CardSimplified(name, effectName, description);
    }
}
