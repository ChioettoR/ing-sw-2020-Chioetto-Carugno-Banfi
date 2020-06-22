package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

public class CLIPlayer {
    private final String name;
    private Color color;
    private String cardName;

    public CLIPlayer(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
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
