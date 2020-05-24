package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Grid {
    private static Grid grid;
    private int length;
    private int width;
    private int completeTowersCount;
    private ArrayList<Tile> tiles = new ArrayList<>();

    private Grid() {
        grid = this;
    }

    public static Grid getGrid() {
        if(grid==null)
            grid = new Grid();

        return grid;
    }

    /**
     * Resets the grid
     */
    public void reset() {
        completeTowersCount = 0;
        tiles.clear();
    }

    public int getCompleteTowersCount() {
        return completeTowersCount;
    }

    public void resetTiles(ArrayList<Tile> newTiles) {
        tiles = newTiles;
    }

    /**
     * Increases the counter of complete towers. If there is a complete towers observer and the complete
     * towers are 5, he's the winner.
     */
    public void increaseCompleteTowersCount() {
        completeTowersCount++;
        if(completeTowersCount==5) {
            Card card = Deck.getDeck().getCompleteTowersObserver();
            PlayersManager playersManager = PlayersManager.getPlayersManager();
            Player player = playersManager.getPlayerWithCard(card);
            if(player!=null)
                playersManager.winPlayer(player);
        }
    }

    public void setCompleteTowersCount(int completeTowersCount) {
        this.completeTowersCount = completeTowersCount;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Creation of a grid with its tiles
     */
    public void createGrid(int length, int width) {
        this.length = length;
        this.width = width;
        tiles = new ArrayList<>();
        for(int i=1; i<=length; i++){
            for (int j=1; j<=width; j++) {
                tiles.add(new Tile(j,i));
            }
        }
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Returns the adjacent tiles to the given one
     * @param tile Get adjacent tiles to this one
     * @return Adjacent tiles to the given one
     */
    public ArrayList<Tile> getNeighbours (Tile tile) {
        if(!getTiles().contains(tile)) {
            System.out.println("The grid doesn't contain this tile");
            return null;
        }
        ArrayList<Tile> tilesNeighbour = new ArrayList<>();
        for(Tile t : tiles) {
            if(Math.abs(tile.getX() - t.getX()) <= 1 && Math.abs(tile.getY() - t.getY()) <= 1 && tile!=t)
                tilesNeighbour.add(t);
        }
        return tilesNeighbour;
    }

    /**
     * Returns the opposite tile to yours compared to another one
     * @param currentTile The tile you want the opposite one
     * @param nextTile The middle tile
     * @return The opposite tile
     */
    public Tile getOppositeTile(Tile currentTile, Tile nextTile) {
        int oppositeTileX;
        int oppositeTileY;
        oppositeTileX = nextTile.getX() + (nextTile.getX() - currentTile.getX());
        oppositeTileY = nextTile.getY() + (nextTile.getY() - currentTile.getY());
        return getTile(oppositeTileX, oppositeTileY);
    }

    /**
     * Returns the tile with the given x and y
     */
    public Tile getTile(int x, int y) {
        for(Tile tile : tiles) {
            if(tile.getX()==x && tile.getY()==y)
                return tile;
        }
        System.out.println("There are no tiles with that x and y");
        return null;
    }

    /**
     * Returns true if the 2 given tiles are adjacent
     * @param currentTile The first tile
     * @param nextTile The second tile
     * @return True if the first and second tiles are adjacent
     */
    public boolean isNeighbour (Tile currentTile, Tile nextTile){
        ArrayList<Tile> neighbours = getNeighbours(currentTile);
        if(neighbours == null)
            return false;
        return neighbours.contains(nextTile);
    }

    /**
     * Checks if the tile is Perimeter or not
     * @param tileWhereBuild tile checked
     * @return true if is perimeter, false otherwise
     */
    public boolean isPerimeterTile(Tile tileWhereBuild){
        ArrayList<Tile> perimeterTiles = new ArrayList<>(tiles);
        perimeterTiles.removeIf(tile -> tile.getX() == 1 || tile.getX() == getLength() || tile.getY() == 1 || tile.getY() == getWidth());
        return !perimeterTiles.contains(tileWhereBuild);
    }

    public GridSimplified simplify() {
        return new GridSimplified(new ArrayList<Tile>(tiles));
    }
}