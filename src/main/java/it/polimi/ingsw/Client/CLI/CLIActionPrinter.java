package it.polimi.ingsw.Client.CLI;

import java.util.ArrayList;

public class CLIActionPrinter {

    public void printAction(ArrayList<String> names) {

        ArrayList<ArrayList<StringBuilder>> actionsLines = new ArrayList<>();

        for (String name : names) {

            final String repeat = "─".repeat(Math.max(0, name.length() + 2));
            ArrayList<StringBuilder> actionLines = new ArrayList<>();

            StringBuilder upperBorder = new StringBuilder();
            upperBorder.append("┌");
            upperBorder.append(repeat);
            upperBorder.append("┐");
            actionLines.add(upperBorder);

            StringBuilder middleBorder = new StringBuilder();
            middleBorder.append("│ ");
            middleBorder.append(name);
            middleBorder.append(" │");
            actionLines.add(middleBorder);

            StringBuilder lowerBorder = new StringBuilder();
            lowerBorder.append("└");
            lowerBorder.append(repeat);
            lowerBorder.append("┘");
            actionLines.add(lowerBorder);

            actionsLines.add(actionLines);
        }

        print(actionsLines);
    }

    private void print(ArrayList<ArrayList<StringBuilder>> actionsLines) {

        ArrayList<StringBuilder> mergedLines = new ArrayList<>();
        int length = actionsLines.get(0).size();

        for(int i=0; i<length; i++) {

            StringBuilder stringBuilder = new StringBuilder();
            mergedLines.add(stringBuilder);

            for (ArrayList<StringBuilder> allActionsLines : actionsLines) {
                stringBuilder.append(allActionsLines.get(i));
                stringBuilder.append(" ".repeat(Math.max(0, 1)));
            }
        }

        for(StringBuilder s : mergedLines) System.out.println(s);
    }
}
