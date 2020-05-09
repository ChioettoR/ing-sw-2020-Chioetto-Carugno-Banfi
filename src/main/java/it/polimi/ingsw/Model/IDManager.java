package it.polimi.ingsw.Model;

public class IDManager {

    private int uniqueID = 0;

    /**
     * Generates a unique numeric ID that will identify each player
     * @return A unique ID
     */
    public int pickID() {
        int IDCopy = uniqueID;
        uniqueID++;
        return IDCopy;
    }
}

