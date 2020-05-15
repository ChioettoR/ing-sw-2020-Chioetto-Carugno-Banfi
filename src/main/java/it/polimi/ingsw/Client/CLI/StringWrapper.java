public class StringWrapper {

    private final String string;
    private boolean colored;

    public boolean isColored() {
        return colored;
    }

    public void setColored(boolean colored) {
        this.colored = colored;
    }

    public StringWrapper(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}