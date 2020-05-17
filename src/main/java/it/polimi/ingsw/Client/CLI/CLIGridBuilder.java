package it.polimi.ingsw.Client.CLI;

public class CLIGridBuilder {

    static int horizontalBorderSize = 11;
    static int verticalBorderSize = 5;
    static int wordsSize = 3;

    public CLIGrid createGrid() {
        CLIGrid CLIGrid = new CLIGrid();
        int length = CLIGrid.getLength();
        int width = CLIGrid.getWidth();

        for (int y=1; y<=width; y++) {

            for(int x=1; x<=length; x++) {

                CLITile CLITile = new CLITile(x, y);
                StringWrapper[] borderUp;
                StringWrapper[] borderDown;
                StringWrapper[] borderRight;
                StringWrapper[] borderLeft;

                if(x==1 && y==1) {
                    borderUp = createHorizontalBorder();
                    borderDown = createHorizontalBorder();
                    borderRight = createVerticalBorder(borderUp[horizontalBorderSize-1], borderDown[horizontalBorderSize-1]);
                    borderLeft = createVerticalBorder(borderUp[0], borderDown[0]);
                }

                else if(x == 1) {
                    borderUp = stealBorderDown(CLIGrid, x, y-1);
                    borderDown = createHorizontalBorder();
                    borderRight = createVerticalBorder(borderUp[horizontalBorderSize-1], borderDown[horizontalBorderSize-1]);
                    borderLeft = createVerticalBorder(borderUp[0], borderDown[0]);
                }

                else if(y == 1) {
                    borderLeft = stealBorderRight(CLIGrid,x-1, y);
                    borderRight = createVerticalBorder();
                    borderUp = createHorizontalBorder(borderLeft[0], borderRight[0]);
                    borderDown = createHorizontalBorder(borderLeft[verticalBorderSize-1], borderRight[verticalBorderSize-1]);
                }

                else {
                    borderUp = stealBorderDown(CLIGrid, x, y-1);
                    borderLeft = stealBorderRight(CLIGrid,x-1, y);
                    borderDown = createHorizontalBorder(borderLeft[verticalBorderSize-1]);
                    borderRight = createVerticalBorder(borderUp[horizontalBorderSize-1], borderDown[horizontalBorderSize-1]);
                }

                CLITile.setUpBorder(borderUp);
                CLITile.setDownBorder(borderDown);
                CLITile.setRightBorder(borderRight);
                CLITile.setLeftBorder(borderLeft);
                StringWrapper empty = new StringWrapper(" ");
                StringWrapper[] words = {empty, empty, empty};
                CLITile.setWords(words);
                CLIGrid.addTile(CLITile);
            }
        }
        return CLIGrid;
    }

    private StringWrapper[] createHorizontalBorder() {
        StringWrapper[] horizontalBorder = new StringWrapper[horizontalBorderSize];

        for (int i = 0; i < horizontalBorderSize; i++) {
            if(i==0 || i==horizontalBorderSize-1) horizontalBorder[i] = new StringWrapper("+");
            else horizontalBorder[i] = new StringWrapper("-");
        }

        return horizontalBorder;
    }

    private StringWrapper[] createHorizontalBorder(StringWrapper cornerLeft, StringWrapper cornerRight) {
        StringWrapper[] horizontalBorder = new StringWrapper[horizontalBorderSize];

        for (int i = 0; i < horizontalBorderSize; i++) {
            if(i==0) horizontalBorder[i] = cornerLeft;
            else if(i==horizontalBorderSize-1) horizontalBorder[i] = cornerRight;
            else horizontalBorder[i] = new StringWrapper("-");
        }

        return horizontalBorder;
    }

    private StringWrapper[] createHorizontalBorder(StringWrapper cornerLeft) {
        StringWrapper[] horizontalBorder = new StringWrapper[horizontalBorderSize];

        for (int i = 0; i < horizontalBorderSize; i++) {
            if(i==0) horizontalBorder[i] = cornerLeft;
            else if(i==horizontalBorderSize-1) horizontalBorder[i] = new StringWrapper("+");
            else horizontalBorder[i] = new StringWrapper("-");
        }

        return horizontalBorder;
    }

    private StringWrapper[] createVerticalBorder() {
        StringWrapper[] verticalBorder = new StringWrapper[verticalBorderSize];

        for (int i = 0; i < verticalBorderSize; i++) {
            if(i==0 || i==verticalBorderSize-1) verticalBorder[i] = new StringWrapper("+");
            else verticalBorder[i] = new StringWrapper("|");
        }

        return verticalBorder;
    }

    private StringWrapper[] createVerticalBorder(StringWrapper cornerUp, StringWrapper cornerDown) {
        StringWrapper[] verticalBorder = new StringWrapper[verticalBorderSize];

        for (int i = 0; i < verticalBorderSize; i++) {
            if(i==0) verticalBorder[i] = cornerUp;
            else if(i==verticalBorderSize-1) verticalBorder[i] = cornerDown;
            else verticalBorder[i] = new StringWrapper("|");
        }

        return verticalBorder;
    }

    private StringWrapper[] stealBorderDown(CLIGrid CLIGrid, int x, int y) {
        return CLIGrid.getTile(x,y).getDownBorder();
    }

    private StringWrapper[] stealBorderRight(CLIGrid CLIGrid, int x, int y) {
        return CLIGrid.getTile(x,y).getRightBorder();
    }
}