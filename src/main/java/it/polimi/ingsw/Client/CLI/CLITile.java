public class CLITile {

    int x;
    int y;
    private StringWrapper[] rightBorder = new StringWrapper[CLIGridBuilder.verticalBorderSize];
    private StringWrapper[] leftBorder = new StringWrapper[CLIGridBuilder.verticalBorderSize];
    private StringWrapper[] upBorder = new StringWrapper[CLIGridBuilder.horizontalBorderSize];
    private StringWrapper[] downBorder = new StringWrapper[CLIGridBuilder.horizontalBorderSize];
    private StringWrapper[] words = new StringWrapper[CLIGridBuilder.wordsSize];

    public CLITile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void color() {
        setBorderColor(true);
    }

    public void deColor() {
        setBorderColor(false);
    }

    public void setBorderColor(boolean colored) {

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

    public StringWrapper[] getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(StringWrapper[] rightBorder) {
        this.rightBorder = rightBorder;
    }

    public StringWrapper[] getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(StringWrapper[] leftBorder) {
        this.leftBorder = leftBorder;
    }

    public StringWrapper[] getUpBorder() {
        return upBorder;
    }

    public void setUpBorder(StringWrapper[] upBorder) {
        this.upBorder = upBorder;
    }

    public StringWrapper[] getDownBorder() {
        return downBorder;
    }

    public void setDownBorder(StringWrapper[] downBorder) {
        this.downBorder = downBorder;
    }

    public StringWrapper[] getWords() {
        return words;
    }

    public void setWords(StringWrapper[] words) {
        this.words = words;
    }
}
