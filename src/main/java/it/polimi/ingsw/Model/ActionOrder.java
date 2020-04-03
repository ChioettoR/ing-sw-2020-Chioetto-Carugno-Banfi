package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class ActionOrder {
    private ArrayList<Action> actions = new ArrayList<Action>();

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
