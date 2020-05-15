package it.polimi.ingsw.Client.CLI;

public class StringWrapper {

    private final String string;
    private boolean colored;

    boolean isColored() {
        return colored;
    }

    void setColored(boolean colored) {
        this.colored = colored;
    }

    StringWrapper(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}