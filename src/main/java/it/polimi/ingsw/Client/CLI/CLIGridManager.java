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

    public void changeGrid(ArrayList<TileSimplified> tiles) {
        CLIGridChanger.change(CLIGrid, tiles);
    }

    public void borderColorTile(int x, int y) {
        CLIGrid.getTile(x,y).color();
    }

    public void printGrid(ArrayList<String> names, ArrayList<Color> colors) {
        CLIGrid.print(names, colors);
    }

    public void resetColorTile(int x, int y) {
        CLIGrid.getTile(x,y).deColor();
    }
}
