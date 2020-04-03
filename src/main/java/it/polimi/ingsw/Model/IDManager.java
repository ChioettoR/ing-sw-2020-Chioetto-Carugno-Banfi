package it.polimi.ingsw.Model;

public class IDManager {

    static int uniqueID = 0;

    /**
     * Generate a unique numeric ID that will identify each player
     * @return A unique ID
     */
    public int pickID() {
        int IDCopy = uniqueID;
        uniqueID++;
        return IDCopy;
    }
}

