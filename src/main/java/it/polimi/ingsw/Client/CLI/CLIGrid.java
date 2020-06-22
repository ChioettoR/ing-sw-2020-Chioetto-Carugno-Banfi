package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

import java.util.ArrayList;

public class CLIGrid {

    private final CLIGridPrinter CLIGridPrinter = new CLIGridPrinter();
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

    void print() { CLIGridPrinter.print(this, false, new ArrayList<>(), new ArrayList<>()); }

    void print(ArrayList<String> names, ArrayList<Color> colors) { CLIGridPrinter.print(this, true, names, colors); }
}
