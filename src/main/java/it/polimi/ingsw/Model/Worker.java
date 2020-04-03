package it.polimi.ingsw.Model;

public class Worker {
    private int playerID;
    private Tile position;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Place the worker on the given tile
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
}
