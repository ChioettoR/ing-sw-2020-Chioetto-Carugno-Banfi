package it.polimi.ingsw.Client.CLI;

import java.util.ArrayList;

public class CLICard {

    private int cardLength;
    private ArrayList<StringBuilder> upperLines;
    private ArrayList<StringBuilder> effectLines;
    private ArrayList<StringBuilder> lowerLines;

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
