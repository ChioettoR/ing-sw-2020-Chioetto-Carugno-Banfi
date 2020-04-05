package it.polimi.ingsw.Model;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Grid {
    private static Grid grid;
    private int length;
    private int width;
    private int completeTowersCount;
    private ArrayList<Tile> tiles;

    public int getCompleteTowersCount() {
        return completeTowersCount;
    }

    public void increaseCompleteTowersCount() {
        completeTowersCount++;
        if(completeTowersCount==5) {
            Card card = Deck.getDeck().getCompleteTowersObserver();
            PlayersManager playersManager = PlayersManager.getPlayersManager();
            Player player = playersManager.getPlayerWithCard(card);
            if(player==null)
                return;
            else
                new WinManager().win(player);
        }
    }

    public void setCompleteTowersCount(int completeTowersCount) {
        this.completeTowersCount = completeTowersCount;
    }

    private Grid() {
        grid = this;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Creation of a grid instance
     */
    public void createGrid(int length, int width) {
        this.length = length;
        this.width = width;
        tiles = new ArrayList<Tile>();
        for(int i=0; i<length; i++){
            for (int j=0; j<width; j++) {
                tiles.add(new Tile(i,j));
            }
        }
    }

    /**
     * @return The instance of Grid
     */
    public static Grid getGrid() {
        if(grid==null)
            grid = new Grid();

        return grid;
    }

    public void destroyGrid() {
        grid = null;
        tiles.clear();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Get the adjacent tiles to the given one
     * @param tile Get adjacent tiles to this one
     * @return List of adjacent tiles to the given one
     */
    public ArrayList<Tile> getNeighbours (Tile tile) {
        if(!getTiles().contains(tile)) {
            System.out.println("The grid doesn't contain this tile");
            return null;
        }
        ArrayList<Tile> tilesNeighbour = new ArrayList<Tile>();
        for(Tile t : tiles) {
            if(Math.abs(tile.getX() - t.getX()) <= 1 && Math.abs(tile.getY() - t.getY()) <= 1 && tile!=t)
                tilesNeighbour.add(t);
        }
        return tilesNeighbour;
    }

    public Tile getOppositeTile(Tile currentTile, Tile nextTile) {
        int oppositeTileX;
        int oppositeTileY;
        oppositeTileX = nextTile.getX() + (nextTile.getX() - currentTile.getX());
        oppositeTileY = nextTile.getY() + (nextTile.getY() - currentTile.getY());
        return getTile(oppositeTileX, oppositeTileY);
    }

    public Tile getTile(int x, int y) {
        for(Tile tile : tiles) {
            if(tile.getX()==x && tile.getY()==y)
                return tile;
        }
        System.out.println("There are no tiles with that x and y");
        return null;
    }

    public boolean isNeighbour (Tile currentTile, Tile nextTile){
        ArrayList<Tile> neighbours = getNeighbours(currentTile);
        if(neighbours == null)
            return false;
        return neighbours.contains(nextTile);
    }
}