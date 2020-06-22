package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

import java.util.ArrayList;
import java.util.Collections;

public class CLIGridPrinter {

    int leftSpacing = 3;

    /**
     * Prints all the grid merging it with the methods in this class
     * @param CLIGrid grid to print
     * @param printNames true if need to print player name, false otherwise
     * @param names list of names
     * @param colors list of colors
     */
    void print(CLIGrid CLIGrid, boolean printNames, ArrayList<String> names, ArrayList<Color> colors) {
        int namesLength = names.size();
        int length = CLIGrid.getLength();
        int width = CLIGrid.getWidth();
        ArrayList<StringWrapper> zeroLine = new ArrayList<>();
        ArrayList<StringWrapper> firstLine = new ArrayList<>();
        ArrayList<StringWrapper> secondLine = new ArrayList<>();
        ArrayList<StringWrapper> thirdLine = new ArrayList<>();
        ArrayList<StringWrapper> fourthLine = new ArrayList<>();
        ArrayList<StringWrapper> fifthLine = new ArrayList<>();

        //clearScreen();
        createZeroLine(zeroLine);
        for(StringWrapper s : zeroLine)
            System.out.print(s.getString());
        System.out.println("");

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

            printLine(firstLine, y, false);
            if(printNames && namesLength>0 && y==1) printName(names.get(0), colors.get(0));
            System.out.println("");
            printLine(secondLine, y, false);
            if(printNames && namesLength>1 && y==1) printName(names.get(1), colors.get(1));
            System.out.println("");
            printLine(thirdLine, y, true);
            if(printNames && namesLength>2 && y==1) printName(names.get(2), colors.get(2));
            System.out.println("");
            printLine(fourthLine, y, false);

            if(y==5) {
                printHorizontalBorder(CLIGrid, 5, y, false, fifthLine);
                System.out.println("");
                printLine(fifthLine, y, false);
            }
            System.out.println("");
        }
    }

    /**
     * Prints the blank lines in the grid
     * @param cliGrid grid
     * @param length length of the grid
     * @param y spacer
     * @param lineNumber number of line to modify
     * @param line line to modify
     */
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

    /**
     * Prints the corrected tile
     * @param line line to print
     * @param y spacer
     * @param writeNumber true if
     */
    private void printLine(ArrayList<StringWrapper> line, int y, boolean writeNumber) {
        if(writeNumber) line.add(0, new StringWrapper(y +  "  "));
        else line.add(0, new StringWrapper("   "));
        for(StringWrapper s : line) {
            if(s.isColored()) System.out.print(Color.ANSI_GREEN.escape() + s.getString() + Color.RESET);
            else System.out.print(s.getString());
        }
        line.clear();
    }

    /**
     * Prints the Horizontal border corrected with all the numbers
     * @param cliGrid grid
     * @param length length of the grid
     * @param y spacer
     * @param up true if upper border needs to be copied, false otherwise
     * @param line line to modify
     */
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

    /**
     * Creates the numbers line
     * @param line line to modify
     */
    private void createZeroLine(ArrayList<StringWrapper> line) {
        for (int i = 0; i < leftSpacing; i++)
            line.add(new StringWrapper(" "));

        for(int x=1; x<6; x++) {
            for (int i = 0; i < 5; i++) line.add(new StringWrapper(" "));
            line.add(new StringWrapper(Integer.toString(x)));
            for (int i = 0; i < 4; i++) line.add(new StringWrapper(" "));
        }
    }

    private void printName(String name, Color color) {
        if(color==null) System.out.print("  "  + "───");
        else System.out.print("  " + color.escape() + "───" + Color.RESET);
        System.out.print(" " + name);
    }
    private synchronized void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
