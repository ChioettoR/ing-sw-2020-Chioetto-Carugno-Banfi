package it.polimi.ingsw.Client;

public enum Color {

    ANSI_BLACK("\u001b[30m"),
    ANSI_RED("\u001b[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_MAGENTA("\u001b[35m"),
    ANSI_CYAN("\u001b[36m"),
    ANSI_WHITE("\u001b[37m");

    public static final String RESET = "\u001B[0m";

    private final String escape;

    Color(String escape) { this.escape = escape; }
    public String escape() { return escape; }
}
