package it.polimi.ingsw.Events.Client;

public abstract class ClientEvent {
    int playerID;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
