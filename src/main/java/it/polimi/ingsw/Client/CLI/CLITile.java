package it.polimi.ingsw.Client.CLI;

public class CLITile {

    private final int x;
    private final int y;
    private StringWrapper[] rightBorder = new StringWrapper[CLIGridBuilder.verticalBorderSize];
    private StringWrapper[] leftBorder = new StringWrapper[CLIGridBuilder.verticalBorderSize];
    private StringWrapper[] upBorder = new StringWrapper[CLIGridBuilder.horizontalBorderSize];
    private StringWrapper[] downBorder = new StringWrapper[CLIGridBuilder.horizontalBorderSize];
    private StringWrapper[] words = new StringWrapper[CLIGridBuilder.wordsSize];

    CLITile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void color() {
        setBorderColor(true);
    }

    void deColor() {
        setBorderColor(false);
    }

    private void setBorderColor(boolean colored) {

        for (StringWrapper stringWrapper : rightBorder) {
            stringWrapper.setColored(colored);
        }

        for (StringWrapper stringWrapper : leftBorder) {
            stringWrapper.setColored(colored);
        }

        for (StringWrapper stringWrapper : upBorder) {
            stringWrapper.setColored(colored);
        }

        for (StringWrapper stringWrapper : downBorder) {
            stringWrapper.setColored(colored);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    StringWrapper[] getRightBorder() {
        return rightBorder;
    }

    void setRightBorder(StringWrapper[] rightBorder) {
        this.rightBorder = rightBorder;
    }

    StringWrapper[] getLeftBorder() {
        return leftBorder;
    }

    void setLeftBorder(StringWrapper[] leftBorder) {
        this.leftBorder = leftBorder;
    }

    StringWrapper[] getUpBorder() {
        return upBorder;
    }

    void setUpBorder(StringWrapper[] upBorder) {
        this.upBorder = upBorder;
    }

    StringWrapper[] getDownBorder() {
        return downBorder;
    }

    void setDownBorder(StringWrapper[] downBorder) {
        this.downBorder = downBorder;
    }

    StringWrapper[] getWords() {
        return words;
    }

    void setWords(StringWrapper[] words) {
        this.words = words;
    }
}
