package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

public class CLIPlayer {
    String name;
    Color color;

    public CLIPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
