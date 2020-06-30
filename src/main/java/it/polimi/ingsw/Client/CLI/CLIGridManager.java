package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public class CLIGridManager {

    private final CLIGrid CLIGrid;
    private final CLIGridChanger CLIGridChanger;

    public CLIGridManager(CLIPlayersManager cliPlayersManager) {
        CLIGrid = new CLIGridBuilder().createGrid();
        CLIGridChanger = new CLIGridChanger(cliPlayersManager);
    }

    /**
     * Applies the changes to the grid
     * @param tiles list of tiles to change
     */
    public void changeGrid(ArrayList<TileSimplified> tiles) {
        CLIGridChanger.change(CLIGrid, tiles);
    }

    /**
     * Colors the border of the available tiles
     * @param x x coord. of the tile
     * @param y y coord. of the tile
     */
    public void borderColorTile(int x, int y) {
        CLIGrid.getTile(x,y).color();
    }

    /**
     * Prints the whole grid
     * @param names list of names of the players
     * @param colors list of colors of the players
     */
    public void printGrid(ArrayList<String> names, ArrayList<Color> colors) {
        CLIGrid.print(names, colors);
    }

    /**
     * Resets the color of the available tiles no longer available for the new action
     * @param x x coord. of the tile
     * @param y y coord. of the tile
     */
    public void resetColorTile(int x, int y) {
        CLIGrid.getTile(x,y).deColor();
    }
}
