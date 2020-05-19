package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

public class CLIPlayer {
    String name;
    Color color;
    String cardName;

    public CLIPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getCardName() {
        return cardName;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
