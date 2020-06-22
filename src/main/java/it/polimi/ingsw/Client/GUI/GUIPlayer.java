package it.polimi.ingsw.Client.GUI;

import javafx.scene.paint.Color;

public class GUIPlayer {
    final String name;
    javafx.scene.paint.Color color;
    String cardName;

    public GUIPlayer(String name) {
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
