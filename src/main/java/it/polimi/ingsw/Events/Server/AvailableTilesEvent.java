package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.ActionType;
import it.polimi.ingsw.Model.TileSimplified;

import java.io.Serializable;
import java.util.ArrayList;

public class AvailableTilesEvent extends ServerEvent implements Serializable {
    private final ArrayList<TileSimplified> tiles;
    private final ActionType actionType;

    /**
     * This is an event for the available tiles for the selected action
     * @param tiles list of tiles
     * @param playerID player's ID
     * @param actionType type of the action
     */
    public AvailableTilesEvent(ArrayList<TileSimplified> tiles, int playerID, ActionType actionType) {
        this.tiles = tiles;
        super.playerID = playerID;
        this.actionType = actionType;
    }

    public ArrayList<TileSimplified> getTiles() {
        return tiles;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
