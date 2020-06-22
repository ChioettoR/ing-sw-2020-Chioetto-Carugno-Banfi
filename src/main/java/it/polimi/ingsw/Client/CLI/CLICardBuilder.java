package it.polimi.ingsw.Client.CLI;

import java.util.*;
import java.util.stream.Collectors;

public class CLICardBuilder {

    Map<String, String> effectsMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    int cardLength = 17;
    int blankSpaceHeight = 3;
    int maxLineSize = 15;
    int cardsSpacing = 3;

    /**
     * This method creates all the cards invoking the other methods in this class and bringing all the variables from the CLICard class
     * @param names name of the card
     * @param effects type of the effect
     * @param descriptions description of the effect
     * @return the created cards
     */
    public ArrayList<StringBuilder> createCards(ArrayList<String> names, ArrayList<String> effects, ArrayList<String> descriptions) {

        ArrayList<CLICard> cards = new ArrayList<>();

        for(int j=0; j<names.size(); j++) {

            CLICard cliCard = new CLICard(names.get(j), effects.get(j), descriptions.get(j));
            effectsMap.put(names.get(j), descriptions.get(j));
            cards.add(cliCard);
            String name = names.get(j);
            String effect = effects.get(j);

            ArrayList<StringBuilder> upperLines = new ArrayList<>();
            ArrayList<StringBuilder> effectLines = new ArrayList<>();
            ArrayList<StringBuilder> lowerLines = new ArrayList<>();

            cliCard.setUpperLines(upperLines);
            cliCard.setEffectLines(effectLines);
            cliCard.setLowerLines(lowerLines);

            if ((cardLength - name.length()) % 2 != 0) cardLength++;

            cliCard.setCardLength(cardLength);

            //Create upper border
            StringBuilder upperBorder = new StringBuilder();
            createUpperBorder(upperBorder, cardLength);
            upperLines.add(upperBorder);

            //Create upper name border
            StringBuilder upperNameBorder = new StringBuilder();
            upperNameBorder.append("│ ");
            createUpperBorder(upperNameBorder, cardLength - 4);
            upperNameBorder.append(" │");
            upperLines.add(upperNameBorder);

            //Create name
            StringBuilder stringBuilderName = new StringBuilder();
            stringBuilderName.append("│ │");
            stringBuilderName.append(StringUtils.center(name, cardLength - 4));
            stringBuilderName.append("│ │");
            upperLines.add(stringBuilderName);

            //Create lower name border
            StringBuilder lowerNameBorder = new StringBuilder();
            lowerNameBorder.append("│ ");
            createLowerBorder(lowerNameBorder, cardLength - 4);
            lowerNameBorder.append(" │");
            upperLines.add(lowerNameBorder);

            //Create blank spaces after name
            for (int i = 0; i < blankSpaceHeight; i++) {
                StringBuilder blankSpace = new StringBuilder();
                blankSpace.append("│");
                createBlankSpace(blankSpace, cardLength);
                blankSpace.append("│");
                upperLines.add(blankSpace);
            }

            //Create middle space
            StringBuilder middleBorder = new StringBuilder();
            createMiddleSpace(middleBorder, cardLength);
            effectLines.add(middleBorder);

            effectLines.addAll(createEffectSpace(effect));

            //Create lower border
            StringBuilder lowerBorder = new StringBuilder();
            createLowerBorder(lowerBorder, cardLength);
            lowerLines.add(lowerBorder);
        }

        ArrayList<StringBuilder> lines;
        lines = mergeCards(cards);
        return lines;
    }

    public void addDescription(String cardName, String description) {
        effectsMap.put(cardName, description);
    }

    public String getDescription(String cardName) {
        return effectsMap.get(cardName);
    }

    /**
     * Creates the upper part of the card
     * @param stringBuilder string containing information about the card
     * @param length length of the card
     */
    private void createUpperBorder(StringBuilder stringBuilder, int length) {
        stringBuilder.append("┌");
        stringBuilder.append("─".repeat(Math.max(0, length)));
        stringBuilder.append("┐");
    }

    /**
     * Creates the lower part of the card
     * @param stringBuilder string containing information about the card
     * @param length length of the card
     */
    private void createLowerBorder(StringBuilder stringBuilder, int length) {
        stringBuilder.append("└");
        stringBuilder.append("─".repeat(Math.max(0, length)));
        stringBuilder.append("┘");
    }

    /**
     * Creates the blank spaces on the central part of the card
     * @param stringBuilder string containing information about the card
     * @param length length of the card
     */
    private void createBlankSpace(StringBuilder stringBuilder, int length) {
        stringBuilder.append(" ".repeat(Math.max(0, length)));
    }

    /**
     * Creates the middle space on the central part of the card
     * @param stringBuilder string containing information about the card
     * @param length length of the card
     */
    private void createMiddleSpace(StringBuilder stringBuilder, int length) {
        stringBuilder.append("├");
        stringBuilder.append("─".repeat(Math.max(0, length)));
        stringBuilder.append("┤");
    }

    /**
     * This method calculates how long is the effect and creates the correct blank spaces and the correct lines to center it and size it
     * @param cardEffect string containing the effect of the card
     * @return returns the string centered and spaced
     */
    private ArrayList<StringBuilder> createEffectSpace(String cardEffect) {
        String[] separatedEffectWords;
        separatedEffectWords = cardEffect.split("\\s+");

        ArrayList<StringBuilder> effectLines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        //Create different lines for the effect
        for (int i = 0; i < separatedEffectWords.length; i++) {

            //If the word i'm adding fits into the line
            int newLength = line.length() + separatedEffectWords[i].length();
            if (newLength < maxLineSize-1)
                line.append(separatedEffectWords[i]).append(" ");

                //Adds the current line to the lines array and create a new line for the word i'm adding
            else {
                effectLines.add(line);
                line = new StringBuilder();
                line.append(separatedEffectWords[i]).append(" ");
            }

            //Adds the last word i'm adding to the lines array
            if (i == separatedEffectWords.length - 1) effectLines.add(line);
        }

        ArrayList<StringBuilder> centeredEffectLines = new ArrayList<>();

        for(StringBuilder s: effectLines) {
            String effect = s.toString().trim();
            int spaceRight = cardLength-s.length()-1;
            StringBuilder string = new StringBuilder();
            string.append("│  ");
            string.append(effect);
            string.append(" ".repeat(Math.max(0, spaceRight)));
            string.append("│");
            centeredEffectLines.add(string);
        }

        return centeredEffectLines;
    }

    /**
     * This method merges all the string created in the method in this class, creating the cards
     * @param cards address of the card
     * @return returns the merged lines of that card
     */
    private ArrayList<StringBuilder> mergeCards(ArrayList<CLICard> cards) {

        ArrayList<ArrayList<StringBuilder>> allCardsLines = new ArrayList<>();

        List<Integer> effectLinesSize = cards.stream().map(cliCard -> cliCard.getEffectLines().size()).collect(Collectors.toList());
        int maxEffectSize = Collections.max(effectLinesSize);

        for(CLICard c : cards) {
            int effectSizeDifference = maxEffectSize - c.getEffectLines().size();

            if(effectSizeDifference>0) {
                ArrayList<StringBuilder> upperLines = c.getUpperLines();

                for(int i=0; i<effectSizeDifference; i++) {
                    StringBuilder blankLine = new StringBuilder();
                    blankLine.append("│");
                    createBlankSpace(blankLine, c.getCardLength());
                    blankLine.append("│");
                    upperLines.add(blankLine);
                }
                c.setUpperLines(upperLines);
            }

            ArrayList<StringBuilder> allLines = new ArrayList<>();
            allLines.addAll(c.getUpperLines());
            allLines.addAll(c.getEffectLines());
            allLines.addAll(c.getLowerLines());
            allCardsLines.add(allLines);
        }

        ArrayList<StringBuilder> mergedLines = new ArrayList<>();

        int length = allCardsLines.get(0).size();
        for(int i=0; i<length; i++) {

            StringBuilder stringBuilder = new StringBuilder();
            mergedLines.add(stringBuilder);

            for (ArrayList<StringBuilder> allCardsLine : allCardsLines) {
                stringBuilder.append(allCardsLine.get(i));
                stringBuilder.append(" ".repeat(Math.max(0, cardsSpacing)));
            }
        }

        return mergedLines;
    }
}
