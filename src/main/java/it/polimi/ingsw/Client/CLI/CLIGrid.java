package it.polimi.ingsw.Client.CLI;

import java.util.ArrayList;

public class CLIGrid {

    private CLIGridPrinter CLIGridPrinter = new CLIGridPrinter();
    private final ArrayList<CLITile> CLITiles = new ArrayList<>();

    CLITile getTile(int x, int y) {
        for(CLITile CLITile : CLITiles) { if(CLITile.getX()==x && CLITile.getY()==y) return CLITile; }
        return null;
    }

    int getLength() {
        return 5;
    }

    int getWidth() {
        return 5;
    }

    void addTile(CLITile CLITile) {
        CLITiles.add(CLITile);
    }

    public ArrayList<CLITile> getCLITiles() {
        return CLITiles;
    }

    void print() {
        CLIGridPrinter.print(this);
    }
}
