package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public class CLIGridManager {

    private final CLIGrid CLIGrid;
    private final CLIPlayersManager cliPlayersManager = new CLIPlayersManager();
    private final CLIGridChanger CLIGridChanger = new CLIGridChanger(cliPlayersManager);

    public CLIGridManager() {
        CLIGrid = new CLIGridBuilder().createGrid();
    }

    public void changeGrid(ArrayList<TileSimplified> tiles) {
        CLIGridChanger.change(CLIGrid, tiles);
    }

    public void borderColorTile(int x, int y) {
        CLIGrid.getTile(x,y).color();
    }

    public void printGrid() {
        CLIGrid.print();
    }

    public void addPlayers(ArrayList<String> names) {
        for(String name : names)
            cliPlayersManager.addPlayer(name);
    }

    public void resetColorTile(int x, int y) {
        CLIGrid.getTile(x,y).deColor();
    }
}
