package it.polimi.ingsw.Model;

public class Worker {
    private int playerID;

    //Identifies the worker among other workers of the same player
    private int localID;

    private Tile position;
    private boolean isAvailable = true;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Sets the worker on the given tile
     * @param tile The new worker position on the grid
     */
    public void setPosition(Tile tile) {
        if(tile==null) {
            System.out.println("The tile is null");
        }

        else if(!Grid.getGrid().getTiles().contains(tile)) {
            System.out.println("The grid doesn't contain the tile");
        }

        else {
            tile.setWorker(this);
            position = tile;
            tile.setEmpty(false);
        }
    }

    public Tile getPosition() {
        return position;
    }

    public WorkerSimplified simplify() {
        return new WorkerSimplified(PlayersManager.getPlayersManager().getPlayerWithID(playerID).getName(), localID);
    }
}
