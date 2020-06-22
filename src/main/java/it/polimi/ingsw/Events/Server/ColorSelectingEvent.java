package it.polimi.ingsw.Events.Server;

import it.polimi.ingsw.Model.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;

public class ColorSelectingEvent extends ServerEvent implements Serializable {
    ArrayList<PlayerColor> colorsAvailable;

    public ArrayList<PlayerColor> getColorsAvailable() {
        return colorsAvailable;
    }

    public ColorSelectingEvent(ArrayList<PlayerColor> colorsAvailable, int playerID) {
        this.colorsAvailable = colorsAvailable;
        super.playerID = playerID;
    }
}
