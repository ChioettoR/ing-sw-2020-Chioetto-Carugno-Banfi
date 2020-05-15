package it.polimi.ingsw.Client.CLI;

public class CLIGridManager {

    private CLIGrid CLIGrid;
    private CLIGridChanger CLIGridChanger = new CLIGridChanger();

    public CLIGridManager() {
        CLIGrid = new CLIGridBuilder().createGrid();
    }

    public void changeGrid() {
        CLIGridChanger.change(CLIGrid);
    }

    public void borderColorTile(int x, int y) {
        CLIGrid.getTile(x,y).color();
    }

    public void printGrid() {
        CLIGrid.print();
    }

    public void resetColorTile(int x, int y) {
        CLIGrid.getTile(x,y).deColor();
    }
}
