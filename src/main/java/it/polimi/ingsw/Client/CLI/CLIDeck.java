package it.polimi.ingsw.Client.CLI;

import java.util.ArrayList;

public class CLIDeck {

    private final int verticalBorder = 3;

    /**
     * Creates the deck that contains all the cards, invoking the methods in this class
     * @param cardsLine the line of the cards
     */
    public void createDeck(ArrayList<StringBuilder> cardsLine) {

        int length = cardsLine.get(0).toString().trim().length() + verticalBorder*2;
        ArrayList<StringBuilder> lines = new ArrayList<>();

        //Create upperLine
        StringBuilder upperLine = new StringBuilder();
        createUpperLine(upperLine, length);
        lines.add(upperLine);

        //Create nameLine
        StringBuilder nameLine = new StringBuilder();
        createNameLine(nameLine, length);
        lines.add(nameLine);

        ArrayList<StringBuilder> newCardsLine = createCardsLine(cardsLine);
        lines.addAll(newCardsLine);

        //Create lowerLine
        StringBuilder lowerLine = new StringBuilder();
        createLowerLine(lowerLine, length);
        lines.add(lowerLine);

        print(lines);
    }

    /**
     * Creates the blank spaces of the deck
     * @param stringBuilder contains information about the string
     * @param length length of the deck
     */
    private void createBlankSpace(StringBuilder stringBuilder, int length) {
        stringBuilder.append(" ".repeat(Math.max(0, length)));
    }

    /**
     * Creates the upper part of the deck
     * @param stringBuilder contains information about the string
     * @param length length of the deck
     */
    private void createUpperLine(StringBuilder stringBuilder, int length) {
        stringBuilder.append('┌');
        stringBuilder.append("─".repeat(Math.max(0, length)));
        stringBuilder.append('┐');
    }

    /**
     * Creates the lower part of the deck
     * @param stringBuilder contains information about the string
     * @param length length of the deck
     */
    private void createLowerLine(StringBuilder stringBuilder, int length) {
        stringBuilder.append('└');
        stringBuilder.append("─".repeat(Math.max(0, length)));
        stringBuilder.append('┘');
    }

    private ArrayList<StringBuilder> createBorder(int length, int height) {

        ArrayList<StringBuilder> borders = new ArrayList<>();
        for(int i=0; i<height; i++) {
            StringBuilder line = new StringBuilder();
            line.append("│");
            createBlankSpace(line, length);
            line.append("│");
            borders.add(line);
        }
        return borders;
    }

    /**
     * Creates the line containing the name of the card
     * @param stringBuilder contains information about the string
     * @param length length of the deck
     */
    private void createNameLine(StringBuilder stringBuilder, int length) {
        stringBuilder.append("│");
        String name = "Deck";
        stringBuilder.append(StringUtils.center(name, length));
        stringBuilder.append("│");
    }

    /**
     * Creates the all lines that belong to the card inside the deck
     * @param stringBuilders contain information about the strings
     * @return returns the lines created
     */
    private ArrayList<StringBuilder> createCardsLine(ArrayList<StringBuilder> stringBuilders) {

        ArrayList<StringBuilder> newLines = new ArrayList<>();

        for(StringBuilder s : stringBuilders) {
            StringBuilder newS = new StringBuilder();
            newS.append("│");
            newS.append(" ".repeat(Math.max(0, verticalBorder)));
            newS.append(s.toString().trim());
            newS.append(" ".repeat(Math.max(0, verticalBorder)));
            newS.append("│");
            newLines.add(newS);
        }
        return newLines;
    }

    /**
     * Prints all the deck
     * @param lines lines that belong to the deck
     */
    private void print(ArrayList<StringBuilder> lines) {
        for(StringBuilder s : lines) { System.out.println(s.toString().trim()); }
    }
}
