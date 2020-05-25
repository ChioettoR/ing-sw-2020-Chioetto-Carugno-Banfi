package it.polimi.ingsw.Client.CLI;

import java.util.ArrayList;

public class CLICard {

    private int cardLength;
    private final String name;
    private final String effectName;
    private final String description;
    private ArrayList<StringBuilder> upperLines;
    private ArrayList<StringBuilder> effectLines;
    private ArrayList<StringBuilder> lowerLines;

    /**
     * Contains all the variables that belong to the card
     * @param name name of the card
     * @param effectName type of the effect
     * @param description description of the effect
     */
    public CLICard(String name, String effectName, String description) {
        this.name = name;
        this.effectName = effectName;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getEffectName() {
        return effectName;
    }

    public String getDescription() {
        return description;
    }

    public int getCardLength() {
        return cardLength;
    }

    public void setCardLength(int cardLength) {
        this.cardLength = cardLength;
    }

    public ArrayList<StringBuilder> getUpperLines() {
        return upperLines;
    }

    public void setUpperLines(ArrayList<StringBuilder> upperLines) {
        this.upperLines = upperLines;
    }

    public ArrayList<StringBuilder> getEffectLines() {
        return effectLines;
    }

    public void setEffectLines(ArrayList<StringBuilder> effectLines) {
        this.effectLines = effectLines;
    }

    public ArrayList<StringBuilder> getLowerLines() {
        return lowerLines;
    }

    public void setLowerLines(ArrayList<StringBuilder> lowerLines) {
        this.lowerLines = lowerLines;
    }
}
