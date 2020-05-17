package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

import java.util.ArrayList;
import java.util.Collections;

public class CLIGridPrinter {

    void print(CLIGrid CLIGrid) {

        int length = CLIGrid.getLength();
        int width = CLIGrid.getWidth();
        ArrayList<StringWrapper> firstLine = new ArrayList<>();
        ArrayList<StringWrapper> secondLine = new ArrayList<>();
        ArrayList<StringWrapper> thirdLine = new ArrayList<>();
        ArrayList<StringWrapper> fourthLine = new ArrayList<>();
        ArrayList<StringWrapper> fifthLine = new ArrayList<>();

        for(int y=1; y<=width; y++) {

            printHorizontalBorder(CLIGrid, length, y, true, firstLine);

            printBlankLine(CLIGrid, length, y, 1, secondLine);

            //Third line
            for(int x=1; x<=length; x++) {
                CLITile cliTile =  CLIGrid.getTile(x, y);
                StringWrapper upBorder1 = cliTile.getLeftBorder()[2];
                if(!thirdLine.contains(upBorder1)) thirdLine.add(upBorder1);
                thirdLine.add(new StringWrapper("   "));
                Collections.addAll(thirdLine, cliTile.getWords());
                thirdLine.add(new StringWrapper("   "));
                StringWrapper rightBorder1 = cliTile.getRightBorder()[2];
                if(!thirdLine.contains(rightBorder1)) thirdLine.add(rightBorder1);
            }

            printBlankLine(CLIGrid, length, y, 3, fourthLine);

            printLine(firstLine);
            System.out.println("");
            printLine(secondLine);
            System.out.println("");
            printLine(thirdLine);
            System.out.println("");
            printLine(fourthLine);

            if(y==5) {
                printHorizontalBorder(CLIGrid, 5, y, false, fifthLine);
                System.out.println("");
                printLine(fifthLine);
            }
            System.out.println("");
        }
    }

    private void printBlankLine(CLIGrid cliGrid, int length, int y, int lineNumber, ArrayList<StringWrapper> line) {

        for(int x=1; x<=length; x++) {
            CLITile cliTile =  cliGrid.getTile(x, y);
            StringWrapper upBorder1 = cliTile.getLeftBorder()[lineNumber];
            if(!line.contains(upBorder1)) line.add(upBorder1);
            line.add(new StringWrapper("         "));
            StringWrapper rightBorder1 = cliTile.getRightBorder()[lineNumber];
            if(!line.contains(rightBorder1)) line.add(rightBorder1);
        }
    }

    private void printLine(ArrayList<StringWrapper> line) {
        for(StringWrapper s : line) {
            if(s.isColored()) System.out.print(Color.ANSI_GREEN.escape() + s.getString() + Color.RESET);
            else System.out.print(s.getString());
        }
        line.clear();
    }

    private void printHorizontalBorder(CLIGrid cliGrid, int length, int y, boolean up, ArrayList<StringWrapper> line) {
        for(int x=1; x<=length; x++) {
            CLITile cliTile =  cliGrid.getTile(x, y);
            StringWrapper[] border;
            if(up) border = cliTile.getUpBorder();
            else border = cliTile.getDownBorder();
            for(StringWrapper s : border) {
                if(!line.contains(s)) line.add(s);
            }
        }
    }
}
